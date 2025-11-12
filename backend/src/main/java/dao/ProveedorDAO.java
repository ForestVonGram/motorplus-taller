package dao;

import model.Proveedor;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProveedorDAO implements BaseDAO<Proveedor, Integer> {
    private static final String TABLE = "proveedor";

    private Proveedor map(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setIdProveedor(rs.getInt("id_proveedor"));
        p.setNombre(rs.getString("nombre"));
        return p;
    }

    @Override
    public Proveedor insert(Proveedor entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_proveedor, nombre) VALUES (?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdProveedor());
            ps.setString(2, entity.getNombre());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Proveedor entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET nombre=? WHERE id_proveedor=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombre());
            ps.setInt(2, entity.getIdProveedor());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_proveedor=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Proveedor> findById(Integer id) throws SQLException {
        String sql = "SELECT id_proveedor, nombre FROM " + TABLE + " WHERE id_proveedor=?";
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
    public List<Proveedor> findAll() throws SQLException {
        String sql = "SELECT id_proveedor, nombre FROM " + TABLE + " ORDER BY nombre ASC";
        List<Proveedor> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /**
     * Búsqueda de proveedores por nombre (coincidencia parcial, case-insensitive).
     */
    public List<Proveedor> searchByNombre(String termino) throws SQLException {
        if (termino == null || termino.trim().isEmpty()) {
            return findAll();
        }
        String sql = "SELECT id_proveedor, nombre FROM " + TABLE + " WHERE nombre ILIKE ? ORDER BY nombre ASC";
        List<Proveedor> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + termino.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }
}
