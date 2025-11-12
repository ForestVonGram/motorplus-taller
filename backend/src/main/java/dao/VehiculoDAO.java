package dao;

import model.Vehiculo;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiculoDAO implements BaseDAO<Vehiculo, String> {
    private static final String TABLE = "vehiculo";

    private Vehiculo map(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo();
        v.setPlaca(rs.getString("placa"));
        v.setMarca(rs.getString("marca"));
        v.setAnio(rs.getInt("anio"));
        v.setIdCliente(rs.getInt("id_cliente"));
        return v;
    }

    @Override
    public Vehiculo insert(Vehiculo entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (placa, marca, anio, id_cliente) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getPlaca());
            ps.setString(2, entity.getMarca());
            ps.setInt(3, entity.getAnio());
            ps.setInt(4, entity.getIdCliente());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(Vehiculo entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET marca=?, anio=?, id_cliente=? WHERE placa=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getMarca());
            ps.setInt(2, entity.getAnio());
            ps.setInt(3, entity.getIdCliente());
            ps.setString(4, entity.getPlaca());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String placa) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE placa=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, placa);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Vehiculo> findById(String placa) throws SQLException {
        String sql = "SELECT placa, marca, anio, id_cliente FROM " + TABLE + " WHERE placa=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Vehiculo> findAll() throws SQLException {
        String sql = "SELECT placa, marca, anio, id_cliente FROM " + TABLE;
        List<Vehiculo> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
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
