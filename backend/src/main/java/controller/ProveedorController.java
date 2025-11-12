package controller;

import model.Proveedor;
import service.ProveedorService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProveedorController {
    private final ProveedorService proveedorService;

    public ProveedorController() {
        this.proveedorService = new ProveedorService();
    }

    public Proveedor crearProveedor(Proveedor proveedor) {
        try {
            return proveedorService.crearProveedor(proveedor);
        } catch (SQLException e) {
            System.err.println("Error al crear proveedor: " + e.getMessage());
            throw new RuntimeException("Error al crear proveedor", e);
        }
    }

    public boolean actualizarProveedor(Proveedor proveedor) {
        try {
            return proveedorService.actualizarProveedor(proveedor);
        } catch (SQLException e) {
            System.err.println("Error al actualizar proveedor: " + e.getMessage());
            throw new RuntimeException("Error al actualizar proveedor", e);
        }
    }

    public boolean eliminarProveedor(Integer id) {
        try {
            return proveedorService.eliminarProveedor(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            throw new RuntimeException("Error al eliminar proveedor", e);
        }
    }

    public Optional<Proveedor> buscarProveedorPorId(Integer id) {
        try {
            return proveedorService.buscarProveedorPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar proveedor: " + e.getMessage());
            throw new RuntimeException("Error al buscar proveedor", e);
        }
    }

    public List<Proveedor> listarTodosProveedores() {
        try {
            return proveedorService.listarTodosProveedores();
        } catch (SQLException e) {
            System.err.println("Error al listar proveedores: " + e.getMessage());
            throw new RuntimeException("Error al listar proveedores", e);
        }
    }

    public List<Proveedor> buscarProveedoresPorNombre(String termino) {
        try {
            return proveedorService.buscarProveedoresPorNombre(termino);
        } catch (SQLException e) {
            System.err.println("Error al buscar proveedores: " + e.getMessage());
            throw new RuntimeException("Error al buscar proveedores", e);
        }
    }
}
