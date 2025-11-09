package dao;

import model.NumeroProveedor;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NumeroProveedorDAO implements BaseDAO<NumeroProveedor, String> {
    private static final String TABLE = "numero_proveedor";

    private NumeroProveedor map(ResultSet rs) throws SQLException {
        NumeroProveedor n = new NumeroProveedor();
        n.setNumero(rs.getString("numero"));
        n.setDescripcion(rs.getString("descripcion"));
        n.setIdProveedor(rs.getInt("id_proveedor"));
        return n;
    }

    @Override
    public NumeroProveedor insert(NumeroProveedor entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (numero, descripcion, id_proveedor) VALUES (?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNumero());
            ps.setString(2, entity.getDescripcion());
            ps.setInt(3, entity.getIdProveedor());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(NumeroProveedor entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET descripcion=?, id_proveedor=? WHERE numero=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getDescripcion());
            ps.setInt(2, entity.getIdProveedor());
            ps.setString(3, entity.getNumero());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String numero) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE numero=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numero);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<NumeroProveedor> findById(String numero) throws SQLException {
        String sql = "SELECT numero, descripcion, id_proveedor FROM " + TABLE + " WHERE numero=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<NumeroProveedor> findAll() throws SQLException {
        String sql = "SELECT numero, descripcion, id_proveedor FROM " + TABLE;
        List<NumeroProveedor> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
