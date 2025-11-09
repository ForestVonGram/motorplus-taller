package controller;

import model.Repuesto;
import service.RepuestoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RepuestoController {
    private final RepuestoService repuestoService;

    public RepuestoController() {
        this.repuestoService = new RepuestoService();
    }

    public Repuesto crearRepuesto(Repuesto repuesto) {
        try {
            return repuestoService.crearRepuesto(repuesto);
        } catch (SQLException e) {
            System.err.println("Error al crear repuesto: " + e.getMessage());
            throw new RuntimeException("Error al crear repuesto", e);
        }
    }

    public boolean actualizarRepuesto(Repuesto repuesto) {
        try {
            return repuestoService.actualizarRepuesto(repuesto);
        } catch (SQLException e) {
            System.err.println("Error al actualizar repuesto: " + e.getMessage());
            throw new RuntimeException("Error al actualizar repuesto", e);
        }
    }

    public boolean eliminarRepuesto(Integer id) {
        try {
            return repuestoService.eliminarRepuesto(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar repuesto: " + e.getMessage());
            throw new RuntimeException("Error al eliminar repuesto", e);
        }
    }

    public Optional<Repuesto> buscarRepuestoPorId(Integer id) {
        try {
            return repuestoService.buscarRepuestoPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar repuesto: " + e.getMessage());
            throw new RuntimeException("Error al buscar repuesto", e);
        }
    }

    public List<Repuesto> listarTodosRepuestos() {
        try {
            return repuestoService.listarTodosRepuestos();
        } catch (SQLException e) {
            System.err.println("Error al listar repuestos: " + e.getMessage());
            throw new RuntimeException("Error al listar repuestos", e);
        }
    }
}
