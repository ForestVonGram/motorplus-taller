package dao;

import model.DetalleServicio;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetalleServicioDAO implements BaseDAO<DetalleServicio, Integer> {
    private static final String TABLE = "detalle_servicio";

    private DetalleServicio map(ResultSet rs) throws SQLException {
        DetalleServicio d = new DetalleServicio();
        d.setIdDetalleServicio(rs.getInt("id_detalle_servicio"));
        d.setIdServicio(rs.getInt("id_servicio"));
        d.setIdOrden(rs.getInt("id_orden"));
        return d;
    }

    @Override
    public DetalleServicio insert(DetalleServicio entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_detalle_servicio, id_servicio, id_orden) VALUES (?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdDetalleServicio());
            ps.setInt(2, entity.getIdServicio());
            ps.setInt(3, entity.getIdOrden());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(DetalleServicio entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET id_servicio=?, id_orden=? WHERE id_detalle_servicio=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdServicio());
            ps.setInt(2, entity.getIdOrden());
            ps.setInt(3, entity.getIdDetalleServicio());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_detalle_servicio=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<DetalleServicio> findById(Integer id) throws SQLException {
        String sql = "SELECT id_detalle_servicio, id_servicio, id_orden FROM " + TABLE + " WHERE id_detalle_servicio=?";
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
    public List<DetalleServicio> findAll() throws SQLException {
        String sql = "SELECT id_detalle_servicio, id_servicio, id_orden FROM " + TABLE;
        List<DetalleServicio> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
