package dao;

import model.Correo;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CorreoDAO implements BaseDAO<Correo, Integer> {
    private static final String TABLE = "correo";

    private Correo map(ResultSet rs) throws SQLException {
        Correo c = new Correo();
        c.setIdCorreo(rs.getInt("id_correo"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setIdCliente(rs.getInt("id_cliente"));
        return c;
    }

    @Override
    public Correo insert(Correo entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_correo, descripcion, id_cliente) VALUES (?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdCorreo());
            ps.setString(2, entity.getDescripcion());
            ps.setInt(3, entity.getIdCliente());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Correo entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET descripcion=?, id_cliente=? WHERE id_correo=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getDescripcion());
            ps.setInt(2, entity.getIdCliente());
            ps.setInt(3, entity.getIdCorreo());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_correo=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Correo> findById(Integer id) throws SQLException {
        String sql = "SELECT id_correo, descripcion, id_cliente FROM " + TABLE + " WHERE id_correo=?";
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
    public List<Correo> findAll() throws SQLException {
        String sql = "SELECT id_correo, descripcion, id_cliente FROM " + TABLE;
        List<Correo> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
