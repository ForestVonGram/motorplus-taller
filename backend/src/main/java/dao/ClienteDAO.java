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

    // Búsqueda de clientes con columna de vehículos (placas agregadas)
    public java.util.List<dto.ClienteSearchDTO> searchWithVehiculos(String query) throws SQLException {
        String q = query == null ? "" : query.trim();
        boolean hasText = !q.isEmpty();

        String select = "SELECT c.id_cliente, c.nombre, c.apellido, " +
                "COALESCE(string_agg(v.placa, ', ' ORDER BY v.placa), '') AS vehiculos " +
                "FROM " + TABLE + " c " +
                "LEFT JOIN vehiculo v ON v.id_cliente = c.id_cliente";

        StringBuilder sb = new StringBuilder(select);
        java.util.List<Object> params = new java.util.ArrayList<>();
        if (hasText) {
            sb.append(" WHERE lower(c.nombre) LIKE ? OR lower(c.apellido) LIKE ? OR lower(CAST(c.id_cliente AS TEXT)) LIKE ? OR lower(v.placa) LIKE ? OR lower(v.marca) LIKE ?");
            String like = "%" + q.toLowerCase() + "%";
            params.add(like); // nombre
            params.add(like); // apellido
            params.add(like); // id_cliente como texto
            params.add(like); // placa
            params.add(like); // marca
        }

        sb.append(" GROUP BY c.id_cliente, c.nombre, c.apellido ORDER BY c.id_cliente");

        java.util.List<dto.ClienteSearchDTO> list = new java.util.ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof String) ps.setString(i + 1, (String) p);
                else if (p instanceof Integer) ps.setInt(i + 1, (Integer) p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dto.ClienteSearchDTO dto = new dto.ClienteSearchDTO();
                    dto.setIdCliente(rs.getInt("id_cliente"));
                    dto.setNombre(rs.getString("nombre"));
                    dto.setApellido(rs.getString("apellido"));
                    dto.setVehiculos(rs.getString("vehiculos"));
                    list.add(dto);
                }
            }
        }
        return list;
    }
}
