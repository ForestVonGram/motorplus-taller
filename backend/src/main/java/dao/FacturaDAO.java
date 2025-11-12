package dao;

import model.Factura;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacturaDAO implements BaseDAO<Factura, Integer> {
    private static final String TABLE = "factura";

    private Factura map(ResultSet rs) throws SQLException {
        Factura f = new Factura();
        f.setIdFactura(rs.getInt("id_factura"));
        f.setCostoManoObra(rs.getDouble("costo_mano_obra"));
        f.setTotal(rs.getDouble("total"));
        f.setImpuesto(rs.getDouble("impuesto"));
        Date fe = rs.getDate("fecha_emision");
        f.setFechaEmision(fe != null ? fe.toLocalDate() : null);
        f.setEstadoPago(rs.getString("estado_pago"));
        f.setIdOrden(rs.getInt("id_orden"));
        return f;
    }

    @Override
    public Factura insert(Factura entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_factura, costo_mano_obra, total, impuesto, fecha_emision, estado_pago, id_orden) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdFactura());
            ps.setDouble(2, entity.getCostoManoObra());
            ps.setDouble(3, entity.getTotal());
            ps.setDouble(4, entity.getImpuesto());
            ps.setDate(5, entity.getFechaEmision() != null ? Date.valueOf(entity.getFechaEmision()) : null);
            ps.setString(6, entity.getEstadoPago());
            ps.setInt(7, entity.getIdOrden());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Factura entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET costo_mano_obra=?, total=?, impuesto=?, fecha_emision=?, estado_pago=?, id_orden=? WHERE id_factura=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, entity.getCostoManoObra());
            ps.setDouble(2, entity.getTotal());
            ps.setDouble(3, entity.getImpuesto());
            ps.setDate(4, entity.getFechaEmision() != null ? Date.valueOf(entity.getFechaEmision()) : null);
            ps.setString(5, entity.getEstadoPago());
            ps.setInt(6, entity.getIdOrden());
            ps.setInt(7, entity.getIdFactura());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_factura=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Factura> findById(Integer id) throws SQLException {
        String sql = "SELECT id_factura, costo_mano_obra, total, impuesto, fecha_emision, estado_pago, id_orden FROM " + TABLE + " WHERE id_factura=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Factura> findAll() throws SQLException {
        String sql = "SELECT id_factura, costo_mano_obra, total, impuesto, fecha_emision, estado_pago, id_orden FROM " + TABLE + " ORDER BY id_factura";
        List<Factura> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /**
     * Búsqueda de facturas por múltiples campos: id_factura, id_orden, estado_pago y fecha_emision
     */
    public List<Factura> search(String query) throws SQLException {
        String q = query == null ? "" : query.trim();
        if (q.isEmpty()) return findAll();

        String like = "%" + q.toLowerCase() + "%";
        String sql = "SELECT id_factura, costo_mano_obra, total, impuesto, fecha_emision, estado_pago, id_orden FROM " + TABLE +
                " WHERE lower(CAST(id_factura AS TEXT)) LIKE ?" +
                " OR lower(CAST(id_orden AS TEXT)) LIKE ?" +
                " OR lower(estado_pago) LIKE ?" +
                " OR to_char(fecha_emision, 'YYYY-MM-DD') LIKE ?" +
                " ORDER BY id_factura";

        List<Factura> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, "%" + q + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public double getTotalCurrentMonth() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total), 0) as total_mes FROM " + TABLE + 
                     " WHERE EXTRACT(MONTH FROM fecha_emision) = EXTRACT(MONTH FROM CURRENT_DATE) " +
                     " AND EXTRACT(YEAR FROM fecha_emision) = EXTRACT(YEAR FROM CURRENT_DATE)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total_mes");
            }
            return 0.0;
        }
    }

    // Facturas asociadas a un vehículo por su placa (JOIN factura -> ordentrabajo -> vehiculo)
    public List<Factura> findByPlaca(String placa) throws SQLException {
        String sql = "SELECT f.id_factura, f.costo_mano_obra, f.total, f.impuesto, f.fecha_emision, f.estado_pago, f.id_orden " +
                "FROM " + TABLE + " f JOIN ordentrabajo o ON o.id_orden = f.id_orden " +
                "JOIN vehiculo v ON v.placa = o.placa WHERE v.placa = ? ORDER BY f.fecha_emision DESC, f.id_factura DESC";
        List<Factura> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    // Facturas por idCliente (JOIN a vehiculo y ordentrabajo)
    public List<Factura> findByCliente(int idCliente) throws SQLException {
        String sql = "SELECT f.id_factura, f.costo_mano_obra, f.total, f.impuesto, f.fecha_emision, f.estado_pago, f.id_orden " +
                "FROM " + TABLE + " f JOIN ordentrabajo o ON o.id_orden = f.id_orden " +
                "JOIN vehiculo v ON v.placa = o.placa WHERE v.id_cliente = ? ORDER BY f.fecha_emision DESC, f.id_factura DESC";
        List<Factura> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    // Obtiene nombre y apellido del cliente por id (usado por reporte FacturasPorCliente)
    public String[] getClienteInfo(int idCliente) throws SQLException {
        String sql = "SELECT nombre, apellido FROM cliente WHERE id_cliente = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{rs.getString("nombre"), rs.getString("apellido")};
                }
            }
        }
        return null;
    }
}
