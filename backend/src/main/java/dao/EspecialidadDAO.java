package dao;

import model.Especialidad;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EspecialidadDAO implements BaseDAO<Especialidad, Integer> {
    private static final String TABLE = "especialidad";

    private Especialidad map(ResultSet rs) throws SQLException {
        Especialidad e = new Especialidad();
        e.setIdEspecialidad(rs.getInt("id_especialidad"));
        e.setNombre(rs.getString("nombre"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setIdMecanico(rs.getInt("id_mecanico"));
        return e;
    }

    @Override
    public Especialidad insert(Especialidad entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_especialidad, nombre, descripcion, id_mecanico) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdEspecialidad());
            ps.setString(2, entity.getNombre());
            ps.setString(3, entity.getDescripcion());
            ps.setInt(4, entity.getIdMecanico());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Especialidad entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET nombre=?, descripcion=?, id_mecanico=? WHERE id_especialidad=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombre());
            ps.setString(2, entity.getDescripcion());
            ps.setInt(3, entity.getIdMecanico());
            ps.setInt(4, entity.getIdEspecialidad());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_especialidad=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Especialidad> findById(Integer id) throws SQLException {
        String sql = "SELECT id_especialidad, nombre, descripcion, id_mecanico FROM " + TABLE + " WHERE id_especialidad=?";
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
    public List<Especialidad> findAll() throws SQLException {
        String sql = "SELECT id_especialidad, nombre, descripcion, id_mecanico FROM " + TABLE;
        List<Especialidad> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
