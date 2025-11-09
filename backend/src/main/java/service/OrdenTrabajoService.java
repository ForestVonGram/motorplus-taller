package service;

import dao.OrdenTrabajoDAO;
import model.OrdenTrabajo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrdenTrabajoService {
    private final OrdenTrabajoDAO ordenTrabajoDAO;

    public OrdenTrabajoService() {
        this.ordenTrabajoDAO = new OrdenTrabajoDAO();
    }

    public OrdenTrabajo crearOrdenTrabajo(OrdenTrabajo orden) throws SQLException {
        return ordenTrabajoDAO.insert(orden);
    }

    public boolean actualizarOrdenTrabajo(OrdenTrabajo orden) throws SQLException {
        return ordenTrabajoDAO.update(orden);
    }

    public boolean eliminarOrdenTrabajo(Integer id) throws SQLException {
        return ordenTrabajoDAO.delete(id);
    }

    public Optional<OrdenTrabajo> buscarOrdenTrabajoPorId(Integer id) throws SQLException {
        return ordenTrabajoDAO.findById(id);
    }

    public List<OrdenTrabajo> listarTodasOrdenesTrabajo() throws SQLException {
        return ordenTrabajoDAO.findAll();
    }
}
