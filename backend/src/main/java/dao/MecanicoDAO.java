package dao;

import model.Mecanico;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MecanicoDAO implements BaseDAO<Mecanico, Integer> {
    private static final String TABLE = "mecanico";

    private Mecanico map(ResultSet rs) throws SQLException {
        Mecanico m = new Mecanico();
        m.setIdMecanico(rs.getInt("id_mecanico"));
        m.setNombre(rs.getString("nombre"));
        int sup = rs.getInt("id_supervisor");
        m.setIdSupervisor(rs.wasNull() ? null : sup);
        return m;
    }

    @Override
    public Mecanico insert(Mecanico entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_mecanico, nombre, id_supervisor) VALUES (?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdMecanico());
            ps.setString(2, entity.getNombre());
            if (entity.getIdSupervisor() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, entity.getIdSupervisor());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Mecanico entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET nombre=?, id_supervisor=? WHERE id_mecanico=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombre());
            if (entity.getIdSupervisor() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, entity.getIdSupervisor());
            ps.setInt(3, entity.getIdMecanico());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_mecanico=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Mecanico> findById(Integer id) throws SQLException {
        String sql = "SELECT id_mecanico, nombre, id_supervisor FROM " + TABLE + " WHERE id_mecanico=?";
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
    public List<Mecanico> findAll() throws SQLException {
        String sql = "SELECT id_mecanico, nombre, id_supervisor FROM " + TABLE;
        List<Mecanico> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /**
     * Búsqueda de mecánicos por nombre (coincidencia parcial, case-insensitive).
     */
    public List<Mecanico> searchByNombre(String termino) throws SQLException {
        if (termino == null || termino.trim().isEmpty()) {
            return findAll();
        }
        String sql = "SELECT id_mecanico, nombre, id_supervisor FROM " + TABLE + " WHERE nombre ILIKE ? ORDER BY nombre ASC";
        List<Mecanico> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + termino.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM " + TABLE;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }
}
