package dao;

import model.DetalleOrden;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetalleOrdenDAO implements BaseDAO<DetalleOrden, Integer> {
    private static final String TABLE = "detalle_orden";

    private DetalleOrden map(ResultSet rs) throws SQLException {
        DetalleOrden d = new DetalleOrden();
        d.setIdDetalleOrden(rs.getInt("id_detalle_orden"));
        d.setIdOrden(rs.getInt("id_orden"));
        d.setIdMecanico(rs.getInt("id_mecanico"));
        d.setRolMecanico(rs.getString("rol_mecanico"));
        return d;
    }

    @Override
    public DetalleOrden insert(DetalleOrden entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_detalle_orden, id_orden, id_mecanico, rol_mecanico) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdDetalleOrden());
            ps.setInt(2, entity.getIdOrden());
            ps.setInt(3, entity.getIdMecanico());
            ps.setString(4, entity.getRolMecanico());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(DetalleOrden entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET id_orden=?, id_mecanico=?, rol_mecanico=? WHERE id_detalle_orden=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdOrden());
            ps.setInt(2, entity.getIdMecanico());
            ps.setString(3, entity.getRolMecanico());
            ps.setInt(4, entity.getIdDetalleOrden());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_detalle_orden=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<DetalleOrden> findById(Integer id) throws SQLException {
        String sql = "SELECT id_detalle_orden, id_orden, id_mecanico, rol_mecanico FROM " + TABLE + " WHERE id_detalle_orden=?";
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
    public List<DetalleOrden> findAll() throws SQLException {
        String sql = "SELECT id_detalle_orden, id_orden, id_mecanico, rol_mecanico FROM " + TABLE;
        List<DetalleOrden> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
