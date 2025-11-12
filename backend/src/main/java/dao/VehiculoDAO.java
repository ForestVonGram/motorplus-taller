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

    // Búsqueda simple por placa/marca/año
    public List<Vehiculo> search(String query) throws SQLException {
        String like = "%" + query.toLowerCase() + "%";
        boolean hasYear = false;
        int year = 0;
        try {
            year = Integer.parseInt(query.trim());
            hasYear = true;
        } catch (NumberFormatException ignored) {}

        String base = "SELECT placa, marca, anio, id_cliente FROM " + TABLE +
                " WHERE lower(placa) LIKE ? OR lower(marca) LIKE ?";
        String sql = hasYear ? base + " OR anio = ?" : base;

        List<Vehiculo> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            if (hasYear) ps.setInt(3, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    // Búsqueda con JOIN a cliente: permite filtrar por nombre/apellido de cliente y devuelve esos datos
    public java.util.List<dto.VehiculoSearchDTO> searchWithCliente(String query) throws SQLException {
        String q = query == null ? "" : query.trim();
        boolean hasText = !q.isEmpty();
        Integer number = null;
        try { number = Integer.valueOf(q); } catch (NumberFormatException ignored) {}

        String select = "SELECT v.placa, v.marca, v.anio, v.id_cliente, c.nombre AS cliente_nombre, c.apellido AS cliente_apellido " +
                "FROM " + TABLE + " v JOIN cliente c ON c.id_cliente = v.id_cliente";

        StringBuilder sb = new StringBuilder(select);
        java.util.List<Object> params = new java.util.ArrayList<>();
        if (hasText) {
            sb.append(" WHERE lower(v.placa) LIKE ? OR lower(v.marca) LIKE ? OR lower(c.nombre) LIKE ? OR lower(c.apellido) LIKE ?");
            String like = "%" + q.toLowerCase() + "%";
            params.add(like); params.add(like); params.add(like); params.add(like);
            if (number != null) {
                sb.append(" OR v.anio = ? OR v.id_cliente = ?");
                params.add(number);
                params.add(number);
            }
        }

        // Ordenar por placa para resultados determinísticos
        if (sb.indexOf("WHERE") >= 0) sb.append(" ORDER BY v.placa"); else sb.append(" ORDER BY v.placa");

        java.util.List<dto.VehiculoSearchDTO> list = new java.util.ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof String) ps.setString(i + 1, (String) p);
                else if (p instanceof Integer) ps.setInt(i + 1, (Integer) p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dto.VehiculoSearchDTO dto = new dto.VehiculoSearchDTO();
                    dto.setPlaca(rs.getString("placa"));
                    dto.setMarca(rs.getString("marca"));
                    dto.setAnio(rs.getInt("anio"));
                    dto.setIdCliente(rs.getInt("id_cliente"));
                    dto.setClienteNombre(rs.getString("cliente_nombre"));
                    dto.setClienteApellido(rs.getString("cliente_apellido"));
                    list.add(dto);
                }
            }
        }
        return list;
    }
}
