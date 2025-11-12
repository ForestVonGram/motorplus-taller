package dao;

import model.TipoServicio;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TipoServicioDAO implements BaseDAO<TipoServicio, Integer> {
    // Nombre real de la tabla en la BD: "tiposervicio" (sin guion bajo)
    private static final String TABLE = "tiposervicio";

    private TipoServicio map(ResultSet rs) throws SQLException {
        TipoServicio t = new TipoServicio();
        t.setIdTipo(rs.getInt("id_tipo"));
        t.setNombreTipo(rs.getString("nombre_tipo"));
        t.setDescripcionTipo(rs.getString("descripcion_tipo"));
        return t;
    }

    @Override
    public TipoServicio insert(TipoServicio entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_tipo, nombre_tipo, descripcion_tipo) VALUES (?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdTipo());
            ps.setString(2, entity.getNombreTipo());
            ps.setString(3, entity.getDescripcionTipo());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(TipoServicio entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET nombre_tipo=?, descripcion_tipo=? WHERE id_tipo=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombreTipo());
            ps.setString(2, entity.getDescripcionTipo());
            ps.setInt(3, entity.getIdTipo());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_tipo=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<TipoServicio> findById(Integer id) throws SQLException {
        String sql = "SELECT id_tipo, nombre_tipo, descripcion_tipo FROM " + TABLE + " WHERE id_tipo=?";
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
    public List<TipoServicio> findAll() throws SQLException {
        String sql = "SELECT id_tipo, nombre_tipo, descripcion_tipo FROM " + TABLE + " ORDER BY id_tipo";
        List<TipoServicio> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /**
     * Búsqueda por id_tipo, nombre_tipo o descripcion_tipo (coincidencia parcial, case-insensitive)
     */
    public List<TipoServicio> search(String query) throws SQLException {
        String q = query == null ? "" : query.trim();
        if (q.isEmpty()) return findAll();
        String like = "%" + q.toLowerCase() + "%";
        String sql = "SELECT id_tipo, nombre_tipo, descripcion_tipo FROM " + TABLE +
                " WHERE lower(nombre_tipo) LIKE ?" +
                " OR lower(COALESCE(descripcion_tipo, '')) LIKE ?" +
                " OR lower(CAST(id_tipo AS TEXT)) LIKE ?" +
                " ORDER BY id_tipo";
        List<TipoServicio> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }
}
