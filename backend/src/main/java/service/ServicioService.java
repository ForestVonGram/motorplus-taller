package service;

import dao.ServicioDAO;
import model.Servicio;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ServicioService {
    private final ServicioDAO servicioDAO;

    public ServicioService() {
        this.servicioDAO = new ServicioDAO();
    }

    public Servicio crearServicio(Servicio servicio) throws SQLException {
        return servicioDAO.insert(servicio);
    }

    public boolean actualizarServicio(Servicio servicio) throws SQLException {
        return servicioDAO.update(servicio);
    }

    public boolean eliminarServicio(Integer id) throws SQLException {
        return servicioDAO.delete(id);
    }

    public Optional<Servicio> buscarServicioPorId(Integer id) throws SQLException {
        return servicioDAO.findById(id);
    }

    public List<Servicio> listarTodosServicios() throws SQLException {
        return servicioDAO.findAll();
    }
}
