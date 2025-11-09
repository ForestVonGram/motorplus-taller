package dao;

import model.RepuestoProveedor;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepuestoProveedorDAO implements BaseDAO<RepuestoProveedor, Object[]> {
    private static final String TABLE = "repuesto_proveedor";

    private RepuestoProveedor map(ResultSet rs) throws SQLException {
        RepuestoProveedor rp = new RepuestoProveedor();
        rp.setIdRepuesto(rs.getInt("id_repuesto"));
        rp.setIdProveedor(rs.getInt("id_proveedor"));
        return rp;
    }

    @Override
    public RepuestoProveedor insert(RepuestoProveedor entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_repuesto, id_proveedor) VALUES (?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdRepuesto());
            ps.setInt(2, entity.getIdProveedor());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(RepuestoProveedor entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET id_proveedor=? WHERE id_repuesto=? AND id_proveedor=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdProveedor());
            ps.setInt(2, entity.getIdRepuesto());
            ps.setInt(3, entity.getIdProveedor());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Object[] ids) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_repuesto=? AND id_proveedor=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (int) ids[0]);
            ps.setInt(2, (int) ids[1]);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<RepuestoProveedor> findById(Object[] ids) throws SQLException {
        String sql = "SELECT id_repuesto, id_proveedor FROM " + TABLE + " WHERE id_repuesto=? AND id_proveedor=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (int) ids[0]);
            ps.setInt(2, (int) ids[1]);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<RepuestoProveedor> findAll() throws SQLException {
        String sql = "SELECT id_repuesto, id_proveedor FROM " + TABLE;
        List<RepuestoProveedor> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
