package controller;

import model.Mecanico;
import service.MecanicoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MecanicoController {
    private final MecanicoService mecanicoService;

    public MecanicoController() {
        this.mecanicoService = new MecanicoService();
    }

    public Mecanico crearMecanico(Mecanico mecanico) {
        try {
            return mecanicoService.crearMecanico(mecanico);
        } catch (SQLException e) {
            System.err.println("Error al crear mecánico: " + e.getMessage());
            throw new RuntimeException("Error al crear mecánico", e);
        }
    }

    public boolean actualizarMecanico(Mecanico mecanico) {
        try {
            return mecanicoService.actualizarMecanico(mecanico);
        } catch (SQLException e) {
            System.err.println("Error al actualizar mecánico: " + e.getMessage());
            throw new RuntimeException("Error al actualizar mecánico", e);
        }
    }

    public boolean eliminarMecanico(Integer id) {
        try {
            return mecanicoService.eliminarMecanico(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar mecánico: " + e.getMessage());
            throw new RuntimeException("Error al eliminar mecánico", e);
        }
    }

    public Optional<Mecanico> buscarMecanicoPorId(Integer id) {
        try {
            return mecanicoService.buscarMecanicoPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar mecánico: " + e.getMessage());
            throw new RuntimeException("Error al buscar mecánico", e);
        }
    }

    public List<Mecanico> listarTodosMecanicos() {
        try {
            return mecanicoService.listarTodosMecanicos();
        } catch (SQLException e) {
            System.err.println("Error al listar mecánicos: " + e.getMessage());
            throw new RuntimeException("Error al listar mecánicos", e);
        }
    }
}
