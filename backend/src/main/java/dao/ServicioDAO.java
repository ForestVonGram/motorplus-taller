package dao;

import model.Servicio;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicioDAO implements BaseDAO<Servicio, Integer> {
    private static final String TABLE = "servicio";

    private Servicio map(ResultSet rs) throws SQLException {
        Servicio s = new Servicio();
        s.setIdServicio(rs.getInt("id_servicio"));
        s.setNombre(rs.getString("nombre"));
        s.setDescripcion(rs.getString("descripcion"));
        s.setIdTipo(rs.getInt("id_tipo"));
        return s;
    }

    @Override
    public Servicio insert(Servicio entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_servicio, nombre, descripcion, id_tipo) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdServicio());
            ps.setString(2, entity.getNombre());
            ps.setString(3, entity.getDescripcion());
            ps.setInt(4, entity.getIdTipo());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Servicio entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET nombre=?, descripcion=?, id_tipo=? WHERE id_servicio=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombre());
            ps.setString(2, entity.getDescripcion());
            ps.setInt(3, entity.getIdTipo());
            ps.setInt(4, entity.getIdServicio());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_servicio=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Servicio> findById(Integer id) throws SQLException {
        String sql = "SELECT id_servicio, nombre, descripcion, id_tipo FROM " + TABLE + " WHERE id_servicio=?";
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
    public List<Servicio> findAll() throws SQLException {
        String sql = "SELECT id_servicio, nombre, descripcion, id_tipo FROM " + TABLE;
        List<Servicio> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
