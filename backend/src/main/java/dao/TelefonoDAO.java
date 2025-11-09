package dao;

import model.Telefono;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TelefonoDAO implements BaseDAO<Telefono, Integer> {
    private static final String TABLE = "telefono";

    private Telefono map(ResultSet rs) throws SQLException {
        Telefono t = new Telefono();
        t.setIdTelefono(rs.getInt("id_telefono"));
        t.setNumero(rs.getString("numero"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setIdCliente(rs.getInt("id_cliente"));
        return t;
    }

    @Override
    public Telefono insert(Telefono entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_telefono, numero, descripcion, id_cliente) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdTelefono());
            ps.setString(2, entity.getNumero());
            ps.setString(3, entity.getDescripcion());
            ps.setInt(4, entity.getIdCliente());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Telefono entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET numero=?, descripcion=?, id_cliente=? WHERE id_telefono=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNumero());
            ps.setString(2, entity.getDescripcion());
            ps.setInt(3, entity.getIdCliente());
            ps.setInt(4, entity.getIdTelefono());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_telefono=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Telefono> findById(Integer id) throws SQLException {
        String sql = "SELECT id_telefono, numero, descripcion, id_cliente FROM " + TABLE + " WHERE id_telefono=?";
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
    public List<Telefono> findAll() throws SQLException {
        String sql = "SELECT id_telefono, numero, descripcion, id_cliente FROM " + TABLE;
        List<Telefono> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
