package dao;

import model.OrdenTrabajo;
import util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdenTrabajoDAO implements BaseDAO<OrdenTrabajo, Integer> {
    private static final String TABLE = "ordentrabajo";

    private OrdenTrabajo map(ResultSet rs) throws SQLException {
        OrdenTrabajo o = new OrdenTrabajo();
        o.setIdOrden(rs.getInt("id_orden"));
        Date fi = rs.getDate("fecha_ingreso");
        o.setFechaIngreso(fi != null ? fi.toLocalDate() : null);
        o.setDiagnosticoInicial(rs.getString("diagnostico_inicial"));
        Date ff = rs.getDate("fecha_finalizacion");
        o.setFechaFinalizacion(ff != null ? ff.toLocalDate() : null);
        o.setPlaca(rs.getString("placa"));
        return o;
    }

    @Override
    public OrdenTrabajo insert(OrdenTrabajo entity) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (id_orden, fecha_ingreso, diagnostico_inicial, fecha_finalizacion, placa) VALUES (?,?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdOrden());
            ps.setDate(2, entity.getFechaIngreso() != null ? Date.valueOf(entity.getFechaIngreso()) : null);
            ps.setString(3, entity.getDiagnosticoInicial());
            ps.setDate(4, entity.getFechaFinalizacion() != null ? Date.valueOf(entity.getFechaFinalizacion()) : null);
            ps.setString(5, entity.getPlaca());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean update(OrdenTrabajo entity) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET fecha_ingreso=?, diagnostico_inicial=?, fecha_finalizacion=?, placa=? WHERE id_orden=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, entity.getFechaIngreso() != null ? Date.valueOf(entity.getFechaIngreso()) : null);
            ps.setString(2, entity.getDiagnosticoInicial());
            ps.setDate(3, entity.getFechaFinalizacion() != null ? Date.valueOf(entity.getFechaFinalizacion()) : null);
            ps.setString(4, entity.getPlaca());
            ps.setInt(5, entity.getIdOrden());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id_orden=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<OrdenTrabajo> findById(Integer id) throws SQLException {
        String sql = "SELECT id_orden, fecha_ingreso, diagnostico_inicial, fecha_finalizacion, placa FROM " + TABLE + " WHERE id_orden=?";
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
    public List<OrdenTrabajo> findAll() throws SQLException {
        String sql = "SELECT id_orden, fecha_ingreso, diagnostico_inicial, fecha_finalizacion, placa FROM " + TABLE;
        List<OrdenTrabajo> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /**
     * Búsqueda de órdenes de trabajo por múltiples campos: id, placa, diagnóstico y fechas.
     * - Coincidencia parcial (LIKE) y case-insensitive para texto usando lower(...)
     * - Fechas comparadas como texto en formato YYYY-MM-DD via to_char
     */
    public List<OrdenTrabajo> search(String query) throws SQLException {
        String q = query == null ? "" : query.trim();
        if (q.isEmpty()) {
            return findAll();
        }
        String like = "%" + q.toLowerCase() + "%";
        String sql = "SELECT id_orden, fecha_ingreso, diagnostico_inicial, fecha_finalizacion, placa FROM " + TABLE +
                " WHERE lower(placa) LIKE ?" +
                " OR lower(diagnostico_inicial) LIKE ?" +
                " OR lower(CAST(id_orden AS TEXT)) LIKE ?" +
                " OR to_char(fecha_ingreso, 'YYYY-MM-DD') LIKE ?" +
                " OR to_char(fecha_finalizacion, 'YYYY-MM-DD') LIKE ?" +
                " ORDER BY id_orden";

        List<OrdenTrabajo> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, "%" + q + "%");
            ps.setString(5, "%" + q + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    /**
     * Búsqueda de órdenes elegibles para facturación:
     *  - Sólo órdenes finalizadas (fecha_finalizacion NOT NULL)
     *  - Sin factura asociada (NOT EXISTS en tabla factura)
     *  - Búsqueda por id/placa/diagnóstico/fechas (mismo criterio que search)
     */
    public List<OrdenTrabajo> searchEligibleForInvoice(String query) throws SQLException {
        String q = query == null ? "" : query.trim();
        String like = "%" + q.toLowerCase() + "%";

        String base = "SELECT o.id_orden, o.fecha_ingreso, o.diagnostico_inicial, o.fecha_finalizacion, o.placa " +
                "FROM " + TABLE + " o " +
                "WHERE o.fecha_finalizacion IS NOT NULL " +
                "AND NOT EXISTS (SELECT 1 FROM factura f WHERE f.id_orden = o.id_orden)";

        StringBuilder sql = new StringBuilder(base);
        List<OrdenTrabajo> list = new ArrayList<>();
        boolean hasQuery = !q.isEmpty();
        if (hasQuery) {
            sql.append(" AND (" +
                    " lower(o.placa) LIKE ?" +
                    " OR lower(o.diagnostico_inicial) LIKE ?" +
                    " OR lower(CAST(o.id_orden AS TEXT)) LIKE ?" +
                    " OR to_char(o.fecha_ingreso, 'YYYY-MM-DD') LIKE ?" +
                    " OR to_char(o.fecha_finalizacion, 'YYYY-MM-DD') LIKE ?" +
                    ")");
        }
        sql.append(" ORDER BY o.id_orden");

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (hasQuery) {
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, "%" + q + "%");
                ps.setString(5, "%" + q + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public int countActiveOrders() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM " + TABLE + " WHERE fecha_finalizacion IS NULL";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    /**
     * Búsqueda de órdenes elegibles para gestión de repuestos:
     *  - Sólo órdenes activas (fecha_finalizacion IS NULL)
     *  - Búsqueda por id/placa/diagnóstico/fechas
     */
    public List<OrdenTrabajo> searchEligibleForParts(String query) throws SQLException {
        String q = query == null ? "" : query.trim();
        String like = "%" + q.toLowerCase() + "%";

        String base = "SELECT o.id_orden, o.fecha_ingreso, o.diagnostico_inicial, o.fecha_finalizacion, o.placa " +
                "FROM " + TABLE + " o " +
                "WHERE o.fecha_finalizacion IS NULL";

        StringBuilder sql = new StringBuilder(base);
        boolean hasQuery = !q.isEmpty();
        if (hasQuery) {
            sql.append(" AND (" +
                    " lower(o.placa) LIKE ?" +
                    " OR lower(o.diagnostico_inicial) LIKE ?" +
                    " OR lower(CAST(o.id_orden AS TEXT)) LIKE ?" +
                    " OR to_char(o.fecha_ingreso, 'YYYY-MM-DD') LIKE ?" +
                    ")");
        }
        sql.append(" ORDER BY o.id_orden");

        List<OrdenTrabajo> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (hasQuery) {
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, "%" + q + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }
}
