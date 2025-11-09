package dao;

import model.Direccion;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DireccionDAO implements BaseDAO<Direccion, Integer> {
    private static final String TABLE = "direccion";

    private Direccion map(ResultSet rs) throws SQLException {
        Direccion d = new Direccion();
        d.setIdDireccion(rs.getInt("id_direccion"));
        d.setCiudad(rs.getString("ciudad"));
        d.setBarrio(rs.getString("barrio"));
        d.setCalle(rs.getString("calle"));
        d.setNumero(rs.getString("numero"));
        d.setIdCliente(rs.getInt("id_cliente"));
        return d;
    }

    @Override
    public Direccion insert(Direccion entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_direccion, ciudad, barrio, calle, numero, id_cliente) VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdDireccion());
            ps.setString(2, entity.getCiudad());
            ps.setString(3, entity.getBarrio());
            ps.setString(4, entity.getCalle());
            ps.setString(5, entity.getNumero());
            ps.setInt(6, entity.getIdCliente());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Direccion entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET ciudad=?, barrio=?, calle=?, numero=?, id_cliente=? WHERE id_direccion=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCiudad());
            ps.setString(2, entity.getBarrio());
            ps.setString(3, entity.getCalle());
            ps.setString(4, entity.getNumero());
            ps.setInt(5, entity.getIdCliente());
            ps.setInt(6, entity.getIdDireccion());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_direccion=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Direccion> findById(Integer id) throws SQLException {
        String sql = "SELECT id_direccion, ciudad, barrio, calle, numero, id_cliente FROM " + TABLE + " WHERE id_direccion=?";
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
    public List<Direccion> findAll() throws SQLException {
        String sql = "SELECT id_direccion, ciudad, barrio, calle, numero, id_cliente FROM " + TABLE;
        List<Direccion> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
}
