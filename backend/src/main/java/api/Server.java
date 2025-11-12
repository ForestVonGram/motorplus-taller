package api;

import controller.ClienteController;
import controller.VehiculoController;
import controller.MecanicoController;
import controller.OrdenTrabajoController;
import dto.DashboardStatsDTO;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.plugin.bundled.CorsPluginConfig;
import model.Cliente;
import model.Vehiculo;
import service.DashboardService;

// Jackson for Java Time (LocalDate serialization as ISO-8601)
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.SQLException;
import java.util.Optional;

public class Server {
    public static void main(String[] args) {
int port = Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(7001);
        var app = Javalin.create(config -> {
            // CORS para desarrollo (Vite en localhost:5173)
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
            // Mapper JSON con Jackson (registrando JavaTimeModule para LocalDate)
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            config.jsonMapper(new JavalinJackson(om));
        }).start(port);

        var clientes = new ClienteController();
        var vehiculos = new VehiculoController();
        var mecanicos = new MecanicoController();
        var ordenes = new OrdenTrabajoController();
        var dashboardService = new DashboardService();
        var facturas = new controller.FacturaController();
        var repuestos = new controller.RepuestoController();
        var proveedores = new controller.ProveedorController();
        var servicios = new controller.ServicioController();
        var tiposServicio = new controller.TipoServicioController();
        var reporteService = new service.ReporteService();

        // Salud
        app.get("/api/health", ctx -> ctx.json("ok"));

        // Dashboard
        app.get("/api/dashboard/stats", ctx -> {
            try {
                DashboardStatsDTO stats = dashboardService.getDashboardStats();
                ctx.json(stats);
            } catch (SQLException e) {
                ctx.status(500).json(java.util.Map.of("error", "Database error: " + e.getMessage()));
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Internal server error: " + e.getMessage()));
            }
        });

        // Clientes
        app.get("/api/clientes", ctx -> {
            var list = clientes.listarTodosClientes();
            ctx.json(list);
        });

        app.get("/api/clientes/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = clientes.buscarClientesConVehiculos(q);
            ctx.json(list);
        });

        app.get("/api/clientes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Cliente> c = clientes.buscarClientePorId(id);
            if (c.isPresent()) ctx.json(c.get());
            else ctx.status(404).result("Cliente no encontrado");
        });

        app.post("/api/clientes", ctx -> {
            Cliente nuevo = ctx.bodyAsClass(Cliente.class);
            Cliente creado = clientes.crearCliente(nuevo);
            ctx.status(201).json(creado);
        });

        app.put("/api/clientes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Cliente update = ctx.bodyAsClass(Cliente.class);
            update.setIdCliente(id);
            boolean ok = clientes.actualizarCliente(update);
            if (ok) ctx.json(update);
            else ctx.status(404).result("Cliente no encontrado");
        });

        app.delete("/api/clientes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = clientes.eliminarCliente(id);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Cliente no encontrado");
        });

        // Vehículos: búsqueda (incluye datos de cliente via JOIN)
        app.get("/api/vehiculos/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = vehiculos.buscarVehiculosConCliente(q);
            ctx.json(list);
        });

        // Opcional: listar o CRUD básico si se necesita más adelante
        app.get("/api/vehiculos", ctx -> ctx.json(vehiculos.listarTodosVehiculos()));
        app.get("/api/vehiculos/{placa}", ctx -> {
            String placa = ctx.pathParam("placa");
            var vOpt = vehiculos.buscarVehiculoPorPlaca(placa);
            if (vOpt.isPresent()) ctx.json(vOpt.get());
            else ctx.status(404).result("Vehículo no encontrado");
        });
        app.post("/api/vehiculos", ctx -> {
            Vehiculo body = ctx.bodyAsClass(Vehiculo.class);
            Vehiculo creado = vehiculos.crearVehiculo(body);
            ctx.status(201).json(creado);
        });
        app.put("/api/vehiculos/{placa}", ctx -> {
            String placa = ctx.pathParam("placa");
            Vehiculo body = ctx.bodyAsClass(Vehiculo.class);
            body.setPlaca(placa);
            boolean ok = vehiculos.actualizarVehiculo(body);
            if (ok) ctx.json(body);
            else ctx.status(404).result("Vehículo no encontrado");
        });
        app.delete("/api/vehiculos/{placa}", ctx -> {
            String placa = ctx.pathParam("placa");
            boolean ok = vehiculos.eliminarVehiculo(placa);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Vehículo no encontrado");
        });

        // Mecánicos: búsqueda por nombre
        app.get("/api/mecanicos/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = mecanicos.buscarMecanicosPorNombre(q);
            ctx.json(list);
        });
        // Mecánicos: obtener por id
        app.get("/api/mecanicos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            var opt = mecanicos.buscarMecanicoPorId(id);
            if (opt.isPresent()) ctx.json(opt.get());
            else ctx.status(404).result("Mecánico no encontrado");
        });
        // Mecánicos: crear
        app.post("/api/mecanicos", ctx -> {
            model.Mecanico body = ctx.bodyAsClass(model.Mecanico.class);
            model.Mecanico creado = mecanicos.crearMecanico(body);
            ctx.status(201).json(creado);
        });
        // Mecánicos: actualizar
        app.put("/api/mecanicos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            model.Mecanico body = ctx.bodyAsClass(model.Mecanico.class);
            body.setIdMecanico(id);
            boolean ok = mecanicos.actualizarMecanico(body);
            if (ok) ctx.json(body);
            else ctx.status(404).result("Mecánico no encontrado");
        });
        // Mecánicos: eliminar
        app.delete("/api/mecanicos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = mecanicos.eliminarMecanico(id);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Mecánico no encontrado");
        });

        // Órdenes de trabajo: búsqueda por id/placa/diagnóstico/fechas
        app.get("/api/ordenes/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = ordenes.buscarOrdenes(q);
            ctx.json(list);
        });
        // Órdenes de trabajo: obtener por id
        app.get("/api/ordenes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            var opt = ordenes.buscarOrdenTrabajoPorId(id);
            if (opt.isPresent()) ctx.json(opt.get());
            else ctx.status(404).result("Orden de trabajo no encontrada");
        });

        // Órdenes de trabajo: crear
        app.post("/api/ordenes", ctx -> {
            model.OrdenTrabajo body = ctx.bodyAsClass(model.OrdenTrabajo.class);
            // Validación básica
            if (body.getIdOrden() == 0 || body.getFechaIngreso() == null
                    || body.getDiagnosticoInicial() == null || body.getDiagnosticoInicial().isBlank()
                    || body.getPlaca() == null || body.getPlaca().isBlank()) {
                ctx.status(400).json(java.util.Map.of("error", "Campos obligatorios faltantes o inválidos"));
                return;
            }
            try {
                var creado = ordenes.crearOrdenTrabajo(body);
                ctx.status(201).json(creado);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "No se pudo crear la orden: " + e.getMessage()));
            }
        });
        // Órdenes de trabajo: actualizar
        app.put("/api/ordenes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            model.OrdenTrabajo body = ctx.bodyAsClass(model.OrdenTrabajo.class);
            body.setIdOrden(id);
            boolean ok = ordenes.actualizarOrdenTrabajo(body);
            if (ok) ctx.json(body);
            else ctx.status(404).result("Orden de trabajo no encontrada");
        });
        // Órdenes de trabajo: eliminar
        app.delete("/api/ordenes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = ordenes.eliminarOrdenTrabajo(id);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Orden de trabajo no encontrada");
        });

        // Facturas: búsqueda de órdenes elegibles para facturar
        app.get("/api/facturas/ordenes/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = ordenes.buscarOrdenesParaFacturar(q);
            ctx.json(list);
        });

        // Repuestos: búsqueda de órdenes activas (aptas para gestión de repuestos)
        app.get("/api/repuestos/ordenes/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = ordenes.buscarOrdenesParaRepuestos(q);
            ctx.json(list);
        });

        // Proveedores: listado, búsqueda y creación
        app.get("/api/proveedores", ctx -> ctx.json(proveedores.listarTodosProveedores()));
        app.get("/api/proveedores/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = proveedores.buscarProveedoresPorNombre(q);
            ctx.json(list);
        });
        app.post("/api/proveedores", ctx -> {
            model.Proveedor body = ctx.bodyAsClass(model.Proveedor.class);
            // Validación básica
            if (body.getIdProveedor() == 0 || body.getNombre() == null || body.getNombre().isBlank()) {
                ctx.status(400).json(java.util.Map.of("error", "Campos obligatorios faltantes o inválidos"));
                return;
            }
            try {
                var creado = proveedores.crearProveedor(body);
                ctx.status(201).json(creado);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "No se pudo crear el proveedor: " + e.getMessage()));
            }
        });
        // Proveedores: obtener por id
        app.get("/api/proveedores/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            var opt = proveedores.buscarProveedorPorId(id);
            if (opt.isPresent()) ctx.json(opt.get());
            else ctx.status(404).result("Proveedor no encontrado");
        });
        // Proveedores: actualizar
        app.put("/api/proveedores/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            model.Proveedor body = ctx.bodyAsClass(model.Proveedor.class);
            body.setIdProveedor(id);
            boolean ok = proveedores.actualizarProveedor(body);
            if (ok) ctx.json(body);
            else ctx.status(404).result("Proveedor no encontrado");
        });
        // Proveedores: eliminar
        app.delete("/api/proveedores/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = proveedores.eliminarProveedor(id);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Proveedor no encontrado");
        });

        // Facturas: listado, búsqueda y creación
        app.get("/api/facturas", ctx -> ctx.json(facturas.listarTodasFacturas()));
        app.get("/api/facturas/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            ctx.json(facturas.buscarFacturas(q));
        });
        // Facturas: obtener por id
        app.get("/api/facturas/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            var opt = facturas.buscarFacturaPorId(id);
            if (opt.isPresent()) ctx.json(opt.get());
            else ctx.status(404).result("Factura no encontrada");
        });
        app.post("/api/facturas", ctx -> {
            model.Factura body = ctx.bodyAsClass(model.Factura.class);
            // Validación básica
            if (body.getIdFactura() == 0 || body.getIdOrden() == 0 || body.getFechaEmision() == null) {
                ctx.status(400).json(java.util.Map.of("error", "Campos obligatorios faltantes o inválidos"));
                return;
            }
            // Regla de negocio: total = costoManoObra + impuesto
            double totalCalc = (body.getCostoManoObra()) + (body.getImpuesto());
            body.setTotal(totalCalc);
            try {
                var creado = facturas.crearFactura(body);
                ctx.status(201).json(creado);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "No se pudo crear la factura: " + e.getMessage()));
            }
        });
        // Facturas: actualizar
        app.put("/api/facturas/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            model.Factura body = ctx.bodyAsClass(model.Factura.class);
            body.setIdFactura(id);
            boolean ok = facturas.actualizarFactura(body);
            if (ok) ctx.json(body);
            else ctx.status(404).result("Factura no encontrada");
        });
        // Facturas: eliminar
        app.delete("/api/facturas/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = facturas.eliminarFactura(id);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Factura no encontrada");
        });

        // Repuestos: listado, búsqueda y creación
        app.get("/api/repuestos", ctx -> ctx.json(repuestos.listarTodosRepuestos()));
        app.get("/api/repuestos/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            ctx.json(repuestos.buscarRepuestos(q));
        });
        app.post("/api/repuestos", ctx -> {
            model.Repuesto body = ctx.bodyAsClass(model.Repuesto.class);
            // Validación básica
            if (body.getIdRepuesto() == 0 || body.getNombre() == null || body.getNombre().isBlank()) {
                ctx.status(400).json(java.util.Map.of("error", "Campos obligatorios faltantes o inválidos"));
                return;
            }
            try {
                var creado = repuestos.crearRepuesto(body);
                ctx.status(201).json(creado);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "No se pudo crear el repuesto: " + e.getMessage()));
            }
        });
        // Repuestos: obtener por id
        app.get("/api/repuestos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            var opt = repuestos.buscarRepuestoPorId(id);
            if (opt.isPresent()) ctx.json(opt.get());
            else ctx.status(404).result("Repuesto no encontrado");
        });
        // Repuestos: actualizar
        app.put("/api/repuestos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            model.Repuesto body = ctx.bodyAsClass(model.Repuesto.class);
            body.setIdRepuesto(id);
            boolean ok = repuestos.actualizarRepuesto(body);
            if (ok) ctx.json(body);
            else ctx.status(404).result("Repuesto no encontrado");
        });
        // Repuestos: eliminar
        app.delete("/api/repuestos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = repuestos.eliminarRepuesto(id);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Repuesto no encontrado");
        });

        // Servicios: búsqueda
        app.get("/api/servicios/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            ctx.json(servicios.buscarServicios(q));
        });
        // Servicios: obtener por id
        app.get("/api/servicios/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            var opt = servicios.buscarServicioPorId(id);
            if (opt.isPresent()) ctx.json(opt.get());
            else ctx.status(404).result("Servicio no encontrado");
        });

        // Tipos de servicio: búsqueda y listado
        app.get("/api/tipos-servicio", ctx -> ctx.json(tiposServicio.listarTodos()));
        app.get("/api/tipos-servicio/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            ctx.json(tiposServicio.buscar(q));
        });
        app.get("/api/tipos-servicio/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            var tOpt = tiposServicio.buscarTipoServicioPorId(id);
            if (tOpt.isPresent()) ctx.json(tOpt.get());
            else ctx.status(404).result("Tipo de servicio no encontrado");
        });
        app.post("/api/tipos-servicio", ctx -> {
            model.TipoServicio body = ctx.bodyAsClass(model.TipoServicio.class);
            if (body.getIdTipo() == 0 || body.getNombreTipo() == null || body.getNombreTipo().isBlank()) {
                ctx.status(400).json(java.util.Map.of("error", "Campos obligatorios faltantes o inválidos"));
                return;
            }
            try {
                var creado = tiposServicio.crearTipoServicio(body);
                ctx.status(201).json(creado);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "No se pudo crear el tipo de servicio: " + e.getMessage()));
            }
        });
        // Tipos de servicio: actualizar
        app.put("/api/tipos-servicio/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            model.TipoServicio body = ctx.bodyAsClass(model.TipoServicio.class);
            body.setIdTipo(id);
            boolean ok = tiposServicio.actualizarTipoServicio(body);
            if (ok) ctx.json(body);
            else ctx.status(404).result("Tipo de servicio no encontrado");
        });
        // Tipos de servicio: eliminar
        app.delete("/api/tipos-servicio/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = tiposServicio.eliminarTipoServicio(id);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Tipo de servicio no encontrado");
        });

        // Servicios: creación
        app.post("/api/servicios", ctx -> {
            model.Servicio body = ctx.bodyAsClass(model.Servicio.class);
            // Validación básica
            if (body.getIdServicio() == 0 || body.getNombre() == null || body.getNombre().isBlank() || body.getIdTipo() == 0) {
                ctx.status(400).json(java.util.Map.of("error", "Campos obligatorios faltantes o inválidos"));
                return;
            }
            try {
                var creado = servicios.crearServicio(body);
                ctx.status(201).json(creado);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "No se pudo crear el servicio: " + e.getMessage()));
            }
        });
        // Servicios: actualizar
        app.put("/api/servicios/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            model.Servicio body = ctx.bodyAsClass(model.Servicio.class);
            body.setIdServicio(id);
            boolean ok = servicios.actualizarServicio(body);
            if (ok) ctx.json(body);
            else ctx.status(404).result("Servicio no encontrado");
        });
        // Servicios: eliminar
        app.delete("/api/servicios/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = servicios.eliminarServicio(id);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Servicio no encontrado");
        });

        // Listado de mecánicos (opcional)
        app.get("/api/mecanicos", ctx -> ctx.json(mecanicos.listarTodosMecanicos()));

        // Reportes
        app.get("/api/reportes/vehiculos/{placa}/historial", ctx -> {
            String placa = ctx.pathParam("placa");
            try {
                var reporte = reporteService.historialVehiculo(placa);
                if (reporte != null) ctx.json(reporte);
                else ctx.status(404).json(java.util.Map.of("error", "Vehículo no encontrado"));
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });

        app.get("/api/reportes/clientes/{id}/facturas", ctx -> {
            int idCliente = Integer.parseInt(ctx.pathParam("id"));
            try {
                var reporte = reporteService.facturasPorCliente(idCliente);
                if (reporte != null) ctx.json(reporte);
                else ctx.status(404).json(java.util.Map.of("error", "Cliente no encontrado"));
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });

        app.get("/api/reportes/clientes-activos", ctx -> {
            try {
                var reporte = reporteService.clientesActivos();
                ctx.json(reporte);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });

        app.get("/api/reportes/rendimiento-mecanicos", ctx -> {
            try {
                var reporte = reporteService.rendimientoMecanicos();
                ctx.json(reporte);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });

        app.get("/api/reportes/inventario-proveedor/{id}", ctx -> {
            int idProveedor = Integer.parseInt(ctx.pathParam("id"));
            try {
                var reporte = reporteService.inventarioPorProveedor(idProveedor);
                if (reporte != null) ctx.json(reporte);
                else ctx.status(404).json(java.util.Map.of("error", "Proveedor no encontrado"));
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });

        app.get("/api/reportes/ordenes-por-estado", ctx -> {
            try {
                var reporte = reporteService.ordenesPorEstado();
                ctx.json(reporte);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });

        app.get("/api/reportes/facturacion-mensual", ctx -> {
            try {
                var reporte = reporteService.facturacionMensual();
                ctx.json(reporte);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });

        app.get("/api/reportes/analisis-rentabilidad", ctx -> {
            try {
                var reporte = reporteService.analisisRentabilidad();
                ctx.json(reporte);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });

        app.get("/api/reportes/trazabilidad-ordenes/{id}", ctx -> {
            int idOrden = Integer.parseInt(ctx.pathParam("id"));
            try {
                var reporte = reporteService.trazabilidadOrdenes(idOrden);
                if (reporte != null) ctx.json(reporte);
                else ctx.status(404).json(java.util.Map.of("error", "Orden no encontrada"));
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });

        app.get("/api/reportes/ventas-vs-costos", ctx -> {
            try {
                var reporte = reporteService.ventasVsCostos();
                ctx.json(reporte);
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Error al generar reporte: " + e.getMessage()));
            }
        });
    }
}
