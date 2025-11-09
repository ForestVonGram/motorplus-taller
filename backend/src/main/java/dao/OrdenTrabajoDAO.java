package dao;

import model.OrdenTrabajo;
import util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdenTrabajoDAO implements BaseDAO<OrdenTrabajo, Integer> {
    private static final String TABLE = "orden_trabajo";

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
}
