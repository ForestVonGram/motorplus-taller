package dao;

import model.Cliente;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO implements BaseDAO<Cliente, Integer> {
    private static final String TABLE = "cliente";

    private Cliente map(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setNombre(rs.getString("nombre"));
        c.setApellido(rs.getString("apellido"));
        c.setContrasenia(rs.getString("contrasenia"));
        return c;
    }

    @Override
    public Cliente insert(Cliente entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_cliente, nombre, apellido, contrasenia) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdCliente());
            ps.setString(2, entity.getNombre());
            ps.setString(3, entity.getApellido());
            ps.setString(4, entity.getContrasenia());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Cliente entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET nombre=?, apellido=?, contrasenia=? WHERE id_cliente=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombre());
            ps.setString(2, entity.getApellido());
            ps.setString(3, entity.getContrasenia());
            ps.setInt(4, entity.getIdCliente());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_cliente=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Cliente> findById(Integer id) throws SQLException {
        String sql = "SELECT id_cliente, nombre, apellido, contrasenia FROM " + TABLE + " WHERE id_cliente=?";
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
    public List<Cliente> findAll() throws SQLException {
        String sql = "SELECT id_cliente, nombre, apellido, contrasenia FROM " + TABLE;
        List<Cliente> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
