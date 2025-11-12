package dao;

import model.Repuesto;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepuestoDAO implements BaseDAO<Repuesto, Integer> {
    private static final String TABLE = "repuesto";

    private Repuesto map(ResultSet rs) throws SQLException {
        Repuesto r = new Repuesto();
        r.setIdRepuesto(rs.getInt("id_repuesto"));
        r.setNombre(rs.getString("nombre"));
        r.setCostoUnitario(rs.getDouble("costo_unitario"));
        r.setStockDisponible(rs.getInt("stock_disponible"));
        return r;
    }

    @Override
    public Repuesto insert(Repuesto entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_repuesto, nombre, costo_unitario, stock_disponible) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdRepuesto());
            ps.setString(2, entity.getNombre());
            ps.setDouble(3, entity.getCostoUnitario());
            ps.setInt(4, entity.getStockDisponible());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Repuesto entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET nombre=?, costo_unitario=?, stock_disponible=? WHERE id_repuesto=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombre());
            ps.setDouble(2, entity.getCostoUnitario());
            ps.setInt(3, entity.getStockDisponible());
            ps.setInt(4, entity.getIdRepuesto());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_repuesto=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Repuesto> findById(Integer id) throws SQLException {
        String sql = "SELECT id_repuesto, nombre, costo_unitario, stock_disponible FROM " + TABLE + " WHERE id_repuesto=?";
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
    public List<Repuesto> findAll() throws SQLException {
        String sql = "SELECT id_repuesto, nombre, costo_unitario, stock_disponible FROM " + TABLE + " ORDER BY id_repuesto";
        List<Repuesto> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /**
     * Búsqueda de repuestos por id o nombre (case-insensitive) y soporte de coincidencia parcial.
     */
    public List<Repuesto> search(String query) throws SQLException {
        String q = query == null ? "" : query.trim();
        if (q.isEmpty()) return findAll();

        String like = "%" + q.toLowerCase() + "%";
        String sql = "SELECT id_repuesto, nombre, costo_unitario, stock_disponible FROM " + TABLE +
                " WHERE lower(nombre) LIKE ? OR lower(CAST(id_repuesto AS TEXT)) LIKE ?" +
                " ORDER BY id_repuesto";

        List<Repuesto> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }
}
