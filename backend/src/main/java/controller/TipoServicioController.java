package controller;

import model.TipoServicio;
import service.TipoServicioService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TipoServicioController {
    private final TipoServicioService tipoServicioService;

    public TipoServicioController() {
        this.tipoServicioService = new TipoServicioService();
    }

    public TipoServicio crearTipoServicio(TipoServicio t) {
        try {
            return tipoServicioService.crearTipoServicio(t);
        } catch (SQLException e) {
            System.err.println("Error al crear tipo de servicio: " + e.getMessage());
            throw new RuntimeException("Error al crear tipo de servicio", e);
        }
    }

    public boolean actualizarTipoServicio(TipoServicio t) {
        try {
            return tipoServicioService.actualizarTipoServicio(t);
        } catch (SQLException e) {
            System.err.println("Error al actualizar tipo de servicio: " + e.getMessage());
            throw new RuntimeException("Error al actualizar tipo de servicio", e);
        }
    }

    public boolean eliminarTipoServicio(Integer id) {
        try {
            return tipoServicioService.eliminarTipoServicio(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar tipo de servicio: " + e.getMessage());
            throw new RuntimeException("Error al eliminar tipo de servicio", e);
        }
    }

    public Optional<TipoServicio> buscarTipoServicioPorId(Integer id) {
        try {
            return tipoServicioService.buscarTipoServicioPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar tipo de servicio: " + e.getMessage());
            throw new RuntimeException("Error al buscar tipo de servicio", e);
        }
    }

    public List<TipoServicio> listarTodos() {
        try {
            return tipoServicioService.listarTodos();
        } catch (SQLException e) {
            System.err.println("Error al listar tipos de servicio: " + e.getMessage());
            throw new RuntimeException("Error al listar tipos de servicio", e);
        }
    }

    public List<TipoServicio> buscar(String query) {
        try {
            return tipoServicioService.buscar(query);
        } catch (SQLException e) {
            System.err.println("Error al buscar tipos de servicio: " + e.getMessage());
            throw new RuntimeException("Error al buscar tipos de servicio", e);
        }
    }
}
