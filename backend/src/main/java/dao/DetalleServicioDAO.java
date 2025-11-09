package dao;

import model.DetalleServicio;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetalleServicioDAO implements BaseDAO<DetalleServicio, Object[]> {
    private static final String TABLE = "detalle_servicio";

    private DetalleServicio map(ResultSet rs) throws SQLException {
        DetalleServicio d = new DetalleServicio();
        d.setIdServicio(rs.getInt("id_servicio"));
        d.setIdOrden(rs.getInt("id_orden"));
        return d;
    }

    @Override
    public DetalleServicio insert(DetalleServicio entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_servicio, id_orden) VALUES (?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdServicio());
            ps.setInt(2, entity.getIdOrden());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(DetalleServicio entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET id_orden=? WHERE id_servicio=? AND id_orden=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdOrden());
            ps.setInt(2, entity.getIdServicio());
            ps.setInt(3, entity.getIdOrden());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Object[] ids) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_servicio=? AND id_orden=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (int) ids[0]);
            ps.setInt(2, (int) ids[1]);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<DetalleServicio> findById(Object[] ids) throws SQLException {
        String sql = "SELECT id_servicio, id_orden FROM " + TABLE + " WHERE id_servicio=? AND id_orden=?";
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
    public List<DetalleServicio> findAll() throws SQLException {
        String sql = "SELECT id_servicio, id_orden FROM " + TABLE;
        List<DetalleServicio> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
