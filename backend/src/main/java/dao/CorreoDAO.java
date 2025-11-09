package dao;

import model.Correo;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CorreoDAO implements BaseDAO<Correo, String> {
    private static final String TABLE = "correo";

    private Correo map(ResultSet rs) throws SQLException {
        Correo c = new Correo();
        c.setDrcCorreo(rs.getString("drc_correo"));
        c.setIdCliente(rs.getInt("id_cliente"));
        return c;
    }

    @Override
    public Correo insert(Correo entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (drc_correo, id_cliente) VALUES (?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getDrcCorreo());
            ps.setInt(2, entity.getIdCliente());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Correo entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET id_cliente=? WHERE drc_correo=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdCliente());
            ps.setString(2, entity.getDrcCorreo());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String drcCorreo) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE drc_correo=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, drcCorreo);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Correo> findById(String drcCorreo) throws SQLException {
        String sql = "SELECT drc_correo, id_cliente FROM " + TABLE + " WHERE drc_correo=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, drcCorreo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Correo> findAll() throws SQLException {
        String sql = "SELECT drc_correo, id_cliente FROM " + TABLE;
        List<Correo> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
