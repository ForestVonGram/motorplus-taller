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
        f.setCostoManoObra(rs.getString("costo_mano_obra"));
        f.setTotal(rs.getString("total"));
        f.setImpuestos(rs.getString("impuestos"));
        Date fe = rs.getDate("fecha_emision");
        f.setFechaEmision(fe != null ? fe.toLocalDate() : null);
        f.setEstadoPago(rs.getString("estado_pago"));
        f.setIdOrden(rs.getInt("id_orden"));
        return f;
    }

    @Override
    public Factura insert(Factura entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_factura, costo_mano_obra, total, impuestos, fecha_emision, estado_pago, id_orden) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdFactura());
            ps.setString(2, entity.getCostoManoObra());
            ps.setString(3, entity.getTotal());
            ps.setString(4, entity.getImpuestos());
            ps.setDate(5, entity.getFechaEmision() != null ? Date.valueOf(entity.getFechaEmision()) : null);
            ps.setString(6, entity.getEstadoPago());
            ps.setInt(7, entity.getIdOrden());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Factura entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET costo_mano_obra=?, total=?, impuestos=?, fecha_emision=?, estado_pago=?, id_orden=? WHERE id_factura=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCostoManoObra());
            ps.setString(2, entity.getTotal());
            ps.setString(3, entity.getImpuestos());
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
        String sql = "SELECT id_factura, costo_mano_obra, total, impuestos, fecha_emision, estado_pago, id_orden FROM " + TABLE + " WHERE id_factura=?";
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
        String sql = "SELECT id_factura, costo_mano_obra, total, impuestos, fecha_emision, estado_pago, id_orden FROM " + TABLE;
        List<Factura> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
