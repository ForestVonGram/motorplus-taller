package controller;

import model.Servicio;
import service.ServicioService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ServicioController {
    private final ServicioService servicioService;

    public ServicioController() {
        this.servicioService = new ServicioService();
    }

    public Servicio crearServicio(Servicio servicio) {
        try {
            return servicioService.crearServicio(servicio);
        } catch (SQLException e) {
            System.err.println("Error al crear servicio: " + e.getMessage());
            throw new RuntimeException("Error al crear servicio", e);
        }
    }

    public boolean actualizarServicio(Servicio servicio) {
        try {
            return servicioService.actualizarServicio(servicio);
        } catch (SQLException e) {
            System.err.println("Error al actualizar servicio: " + e.getMessage());
            throw new RuntimeException("Error al actualizar servicio", e);
        }
    }

    public boolean eliminarServicio(Integer id) {
        try {
            return servicioService.eliminarServicio(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar servicio: " + e.getMessage());
            throw new RuntimeException("Error al eliminar servicio", e);
        }
    }

    public Optional<Servicio> buscarServicioPorId(Integer id) {
        try {
            return servicioService.buscarServicioPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar servicio: " + e.getMessage());
            throw new RuntimeException("Error al buscar servicio", e);
        }
    }

    public List<Servicio> listarTodosServicios() {
        try {
            return servicioService.listarTodosServicios();
        } catch (SQLException e) {
            System.err.println("Error al listar servicios: " + e.getMessage());
            throw new RuntimeException("Error al listar servicios", e);
        }
    }

    public List<Servicio> buscarServicios(String query) {
        try {
            return servicioService.buscarServicios(query);
        } catch (SQLException e) {
            System.err.println("Error al buscar servicios: " + e.getMessage());
            throw new RuntimeException("Error al buscar servicios", e);
        }
    }
}
