package service;

import dao.*;
import dto.reportes.*;
import model.*;
import util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReporteService {
    private final VehiculoDAO vehiculoDAO;
    private final OrdenTrabajoDAO ordenTrabajoDAO;
    private final FacturaDAO facturaDAO;
    private final ClienteDAO clienteDAO;
    private final MecanicoDAO mecanicoDAO;
    private final RepuestoDAO repuestoDAO;
    private final ProveedorDAO proveedorDAO;

    public ReporteService() {
        this.vehiculoDAO = new VehiculoDAO();
        this.ordenTrabajoDAO = new OrdenTrabajoDAO();
        this.facturaDAO = new FacturaDAO();
        this.clienteDAO = new ClienteDAO();
        this.mecanicoDAO = new MecanicoDAO();
        this.repuestoDAO = new RepuestoDAO();
        this.proveedorDAO = new ProveedorDAO();
    }

    public HistorialVehiculoDTO historialVehiculo(String placa) throws SQLException {
        Optional<Vehiculo> vOpt = vehiculoDAO.findById(placa);
        if (vOpt.isEmpty()) return null;

        Vehiculo v = vOpt.get();
        HistorialVehiculoDTO.VehiculoInfo vi = new HistorialVehiculoDTO.VehiculoInfo();
        vi.placa = v.getPlaca();
        vi.marca = v.getMarca();
        vi.anio = v.getAnio();
        vi.idCliente = v.getIdCliente();

        List<OrdenTrabajo> ordenes = ordenTrabajoDAO.findByPlaca(placa);
        List<HistorialVehiculoDTO.OrdenItem> ordenItems = new ArrayList<>();
        for (OrdenTrabajo o : ordenes) {
            HistorialVehiculoDTO.OrdenItem oi = new HistorialVehiculoDTO.OrdenItem();
            oi.idOrden = o.getIdOrden();
            oi.fechaIngreso = o.getFechaIngreso();
            oi.diagnosticoInicial = o.getDiagnosticoInicial();
            oi.fechaFinalizacion = o.getFechaFinalizacion();
            ordenItems.add(oi);
        }

        List<Factura> facturas = facturaDAO.findByPlaca(placa);
        List<HistorialVehiculoDTO.FacturaItem> facturaItems = new ArrayList<>();
        for (Factura f : facturas) {
            HistorialVehiculoDTO.FacturaItem fi = new HistorialVehiculoDTO.FacturaItem();
            fi.idFactura = f.getIdFactura();
            fi.fechaEmision = f.getFechaEmision();
            fi.total = f.getTotal();
            fi.estadoPago = f.getEstadoPago();
            fi.idOrden = f.getIdOrden();
            facturaItems.add(fi);
        }

        return new HistorialVehiculoDTO(vi, ordenItems, facturaItems);
    }

    public FacturasPorClienteDTO facturasPorCliente(int idCliente) throws SQLException {
        // Cliente básico a través de VehiculoDAO no aplica; idealmente ClienteDAO, pero para evitar dependencias, usamos FacturaDAO JOIN para traer nombre/apellido.
        var clienteInfo = facturaDAO.getClienteInfo(idCliente);
        if (clienteInfo == null) return null;

        List<Factura> facturas = facturaDAO.findByCliente(idCliente);
        List<FacturasPorClienteDTO.FacturaItem> items = new ArrayList<>();
        for (Factura f : facturas) {
            FacturasPorClienteDTO.FacturaItem it = new FacturasPorClienteDTO.FacturaItem();
            it.idFactura = f.getIdFactura();
            it.fechaEmision = f.getFechaEmision();
            it.total = f.getTotal();
            it.estadoPago = f.getEstadoPago();
            it.idOrden = f.getIdOrden();
            items.add(it);
        }

        FacturasPorClienteDTO.ClienteInfo ci = new FacturasPorClienteDTO.ClienteInfo();
        ci.idCliente = idCliente;
        ci.nombre = clienteInfo[0];
        ci.apellido = clienteInfo[1];
        return new FacturasPorClienteDTO(ci, items);
    }

    public ClientesActivosDTO clientesActivos() throws SQLException {
        int totalClientes = clienteDAO.findAll().size();
        
        // Top clientes por facturación
        String sql = "SELECT c.id_cliente, c.nombre, c.apellido, COALESCE(SUM(f.total), 0) as total_facturado " +
                     "FROM cliente c " +
                     "LEFT JOIN vehiculo v ON v.id_cliente = c.id_cliente " +
                     "LEFT JOIN ordentrabajo o ON o.placa = v.placa " +
                     "LEFT JOIN factura f ON f.id_orden = o.id_orden " +
                     "GROUP BY c.id_cliente, c.nombre, c.apellido " +
                     "ORDER BY total_facturado DESC LIMIT 10";
        
        List<ClientesActivosDTO.TopCliente> topClientes = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClientesActivosDTO.TopCliente tc = new ClientesActivosDTO.TopCliente();
                tc.idCliente = rs.getInt("id_cliente");
                tc.nombre = rs.getString("nombre");
                tc.apellido = rs.getString("apellido");
                tc.totalFacturado = rs.getDouble("total_facturado");
                topClientes.add(tc);
            }
        }
        return new ClientesActivosDTO(totalClientes, topClientes);
    }

    public RendimientoMecanicosDTO rendimientoMecanicos() throws SQLException {
        // Sólo listar mecánicos actuales sin métricas por orden
        List<RendimientoMecanicosDTO.MecanicoStats> mecanicos = new ArrayList<>();
        for (Mecanico m : mecanicoDAO.findAll()) {
            RendimientoMecanicosDTO.MecanicoStats ms = new RendimientoMecanicosDTO.MecanicoStats();
            ms.idMecanico = m.getIdMecanico();
            ms.nombre = m.getNombre();
            ms.ordenesCompletadas = 0; // sin cálculo de rendimiento
            mecanicos.add(ms);
        }
        return new RendimientoMecanicosDTO(0, mecanicos);
    }

    public InventarioPorProveedorDTO inventarioPorProveedor(int idProveedor) throws SQLException {
        Optional<Proveedor> provOpt = proveedorDAO.findById(idProveedor);
        if (provOpt.isEmpty()) return null;
        
        Proveedor p = provOpt.get();
        InventarioPorProveedorDTO.ProveedorInfo pi = new InventarioPorProveedorDTO.ProveedorInfo();
        pi.idProveedor = p.getIdProveedor();
        pi.nombre = p.getNombre();
        
        String sql = "SELECT r.id_repuesto, r.nombre, r.stock_disponible, r.costo_unitario " +
                     "FROM repuesto r " +
                     "JOIN repuestoproveedor rp ON rp.id_repuesto = r.id_repuesto " +
                     "WHERE rp.id_proveedor = ? " +
                     "ORDER BY r.nombre";
        
        List<InventarioPorProveedorDTO.RepuestoItem> repuestos = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventarioPorProveedorDTO.RepuestoItem ri = new InventarioPorProveedorDTO.RepuestoItem();
                    ri.idRepuesto = rs.getInt("id_repuesto");
                    ri.nombre = rs.getString("nombre");
                    ri.stockActual = rs.getInt("stock_disponible");
                    ri.precioUnitario = rs.getDouble("costo_unitario");
                    repuestos.add(ri);
                }
            }
        }
        
        return new InventarioPorProveedorDTO(pi, repuestos);
    }

    public OrdenesPorEstadoDTO ordenesPorEstado() throws SQLException {
        String sql = "SELECT " +
                     "COUNT(CASE WHEN o.fecha_finalizacion IS NULL THEN 1 END) as pendientes, " +
                     "COUNT(CASE WHEN o.fecha_finalizacion IS NOT NULL AND NOT EXISTS (SELECT 1 FROM factura f WHERE f.id_orden = o.id_orden) THEN 1 END) as completadas, " +
                     "COUNT(CASE WHEN EXISTS (SELECT 1 FROM factura f WHERE f.id_orden = o.id_orden) THEN 1 END) as facturadas " +
                     "FROM ordentrabajo o";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int pendientes = rs.getInt("pendientes");
                int enProceso = 0; // Podríamos definir esto como un subset de pendientes si hubiera más granularidad
                int completadas = rs.getInt("completadas");
                int facturadas = rs.getInt("facturadas");
                return new OrdenesPorEstadoDTO(pendientes, enProceso, completadas, facturadas);
            }
        }
        return new OrdenesPorEstadoDTO(0, 0, 0, 0);
    }

    public FacturacionMensualDTO facturacionMensual() throws SQLException {
        String sql = "SELECT to_char(fecha_emision, 'YYYY-MM') as mes, SUM(total) as total " +
                     "FROM factura " +
                     "WHERE fecha_emision >= CURRENT_DATE - INTERVAL '6 months' " +
                     "GROUP BY mes " +
                     "ORDER BY mes DESC";
        
        List<FacturacionMensualDTO.MesFacturacion> meses = new ArrayList<>();
        double totalAnio = 0;
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                FacturacionMensualDTO.MesFacturacion mf = new FacturacionMensualDTO.MesFacturacion();
                mf.mes = rs.getString("mes");
                mf.total = rs.getDouble("total");
                totalAnio += mf.total;
                meses.add(mf);
            }
        }
        
        return new FacturacionMensualDTO(totalAnio, meses);
    }

    public AnalisisRentabilidadDTO analisisRentabilidad() throws SQLException {
        // Margen por servicio (simplificado: asumimos margen = precio - costo promedio)
        String sqlServicios = "SELECT s.id_servicio, s.nombre, AVG(s.precio) as margen " +
                              "FROM servicio s " +
                              "GROUP BY s.id_servicio, s.nombre " +
                              "ORDER BY margen DESC LIMIT 10";
        
        List<AnalisisRentabilidadDTO.ServicioMargen> servicios = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlServicios);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AnalisisRentabilidadDTO.ServicioMargen sm = new AnalisisRentabilidadDTO.ServicioMargen();
                sm.idServicio = rs.getInt("id_servicio");
                sm.nombre = rs.getString("nombre");
                sm.margenPromedio = rs.getDouble("margen");
                servicios.add(sm);
            }
        }
        
        // Margen por repuesto (simplificado)
        String sqlRepuestos = "SELECT r.id_repuesto, r.nombre, AVG(r.costo_unitario * 0.3) as margen " +
                              "FROM repuesto r " +
                              "GROUP BY r.id_repuesto, r.nombre " +
                              "ORDER BY margen DESC LIMIT 10";
        
        List<AnalisisRentabilidadDTO.RepuestoMargen> repuestos = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlRepuestos);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AnalisisRentabilidadDTO.RepuestoMargen rm = new AnalisisRentabilidadDTO.RepuestoMargen();
                rm.idRepuesto = rs.getInt("id_repuesto");
                rm.nombre = rs.getString("nombre");
                rm.margenPromedio = rs.getDouble("margen");
                repuestos.add(rm);
            }
        }
        
        return new AnalisisRentabilidadDTO(servicios, repuestos);
    }

    public TrazabilidadOrdenesDTO trazabilidadOrdenes(int idOrden) throws SQLException {
        Optional<OrdenTrabajo> ordenOpt = ordenTrabajoDAO.findById(idOrden);
        if (ordenOpt.isEmpty()) return null;
        
        OrdenTrabajo o = ordenOpt.get();
        TrazabilidadOrdenesDTO.OrdenInfo oi = new TrazabilidadOrdenesDTO.OrdenInfo();
        oi.idOrden = o.getIdOrden();
        oi.placa = o.getPlaca();
        oi.fechaIngreso = o.getFechaIngreso();
        oi.fechaFinalizacion = o.getFechaFinalizacion();
        oi.diagnosticoInicial = o.getDiagnosticoInicial();
        
        // Obtener mecánico responsable
        String sqlMecanico = "SELECT m.id_mecanico, m.nombre FROM mecanico m " +
                             "JOIN detalleorden do ON do.id_mecanico = m.id_mecanico " +
                             "WHERE do.id_orden = ? LIMIT 1";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlMecanico)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    oi.idMecanico = rs.getInt("id_mecanico");
                    oi.nombreMecanico = rs.getString("nombre");
                }
            }
        }
        
        // Servicios realizados
        String sqlServicios = "SELECT s.id_servicio, s.nombre FROM servicio s " +
                              "JOIN detalleservicio ds ON ds.id_servicio = s.id_servicio " +
                              "WHERE ds.id_orden = ?";
        
        List<TrazabilidadOrdenesDTO.ServicioRealizado> servicios = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlServicios)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TrazabilidadOrdenesDTO.ServicioRealizado sr = new TrazabilidadOrdenesDTO.ServicioRealizado();
                    sr.idServicio = rs.getInt("id_servicio");
                    sr.nombreServicio = rs.getString("nombre");
                    servicios.add(sr);
                }
            }
        }
        
        // Repuestos utilizados
        String sqlRepuestos = "SELECT r.id_repuesto, r.nombre, dr.cantidad FROM repuesto r " +
                              "JOIN detallerepuesto dr ON dr.id_repuesto = r.id_repuesto " +
                              "WHERE dr.id_orden = ?";
        
        List<TrazabilidadOrdenesDTO.RepuestoUtilizado> repuestos = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlRepuestos)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TrazabilidadOrdenesDTO.RepuestoUtilizado ru = new TrazabilidadOrdenesDTO.RepuestoUtilizado();
                    ru.idRepuesto = rs.getInt("id_repuesto");
                    ru.nombreRepuesto = rs.getString("nombre");
                    ru.cantidad = rs.getInt("cantidad");
                    repuestos.add(ru);
                }
            }
        }
        
        return new TrazabilidadOrdenesDTO(oi, servicios, repuestos);
    }

    public VentasVsCostosDTO ventasVsCostos() throws SQLException {
        String sql = "SELECT " +
                     "COALESCE(SUM(f.total), 0) as ventas, " +
                     "COALESCE(SUM(f.costo_mano_obra), 0) as costos " +
                     "FROM factura f";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                double ventas = rs.getDouble("ventas");
                double costos = rs.getDouble("costos");
                double margen = ventas - costos;
                double porcentajeMargen = ventas > 0 ? (margen / ventas) * 100 : 0;
                return new VentasVsCostosDTO(ventas, costos, margen, porcentajeMargen);
            }
        }
        return new VentasVsCostosDTO(0, 0, 0, 0);
    }
}
