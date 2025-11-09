package controller;

import model.OrdenTrabajo;
import service.OrdenTrabajoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrdenTrabajoController {
    private final OrdenTrabajoService ordenTrabajoService;

    public OrdenTrabajoController() {
        this.ordenTrabajoService = new OrdenTrabajoService();
    }

    public OrdenTrabajo crearOrdenTrabajo(OrdenTrabajo orden) {
        try {
            return ordenTrabajoService.crearOrdenTrabajo(orden);
        } catch (SQLException e) {
            System.err.println("Error al crear orden de trabajo: " + e.getMessage());
            throw new RuntimeException("Error al crear orden de trabajo", e);
        }
    }

    public boolean actualizarOrdenTrabajo(OrdenTrabajo orden) {
        try {
            return ordenTrabajoService.actualizarOrdenTrabajo(orden);
        } catch (SQLException e) {
            System.err.println("Error al actualizar orden de trabajo: " + e.getMessage());
            throw new RuntimeException("Error al actualizar orden de trabajo", e);
        }
    }

    public boolean eliminarOrdenTrabajo(Integer id) {
        try {
            return ordenTrabajoService.eliminarOrdenTrabajo(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar orden de trabajo: " + e.getMessage());
            throw new RuntimeException("Error al eliminar orden de trabajo", e);
        }
    }

    public Optional<OrdenTrabajo> buscarOrdenTrabajoPorId(Integer id) {
        try {
            return ordenTrabajoService.buscarOrdenTrabajoPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar orden de trabajo: " + e.getMessage());
            throw new RuntimeException("Error al buscar orden de trabajo", e);
        }
    }

    public List<OrdenTrabajo> listarTodasOrdenesTrabajo() {
        try {
            return ordenTrabajoService.listarTodasOrdenesTrabajo();
        } catch (SQLException e) {
            System.err.println("Error al listar órdenes de trabajo: " + e.getMessage());
            throw new RuntimeException("Error al listar órdenes de trabajo", e);
        }
    }
}
