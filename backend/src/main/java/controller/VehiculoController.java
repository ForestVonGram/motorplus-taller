package controller;

import dto.VehiculoSearchDTO;
import model.Vehiculo;
import service.VehiculoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class VehiculoController {
    private final VehiculoService vehiculoService;

    public VehiculoController() {
        this.vehiculoService = new VehiculoService();
    }

    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        try {
            return vehiculoService.crearVehiculo(vehiculo);
        } catch (SQLException e) {
            System.err.println("Error al crear vehículo: " + e.getMessage());
            throw new RuntimeException("Error al crear vehículo", e);
        }
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        try {
            return vehiculoService.actualizarVehiculo(vehiculo);
        } catch (SQLException e) {
            System.err.println("Error al actualizar vehículo: " + e.getMessage());
            throw new RuntimeException("Error al actualizar vehículo", e);
        }
    }

    public boolean eliminarVehiculo(String placa) {
        try {
            return vehiculoService.eliminarVehiculo(placa);
        } catch (SQLException e) {
            System.err.println("Error al eliminar vehículo: " + e.getMessage());
            throw new RuntimeException("Error al eliminar vehículo", e);
        }
    }

    public Optional<Vehiculo> buscarVehiculoPorPlaca(String placa) {
        try {
            return vehiculoService.buscarVehiculoPorPlaca(placa);
        } catch (SQLException e) {
            System.err.println("Error al buscar vehículo: " + e.getMessage());
            throw new RuntimeException("Error al buscar vehículo", e);
        }
    }

    public List<Vehiculo> listarTodosVehiculos() {
        try {
            return vehiculoService.listarTodosVehiculos();
        } catch (SQLException e) {
            System.err.println("Error al listar vehículos: " + e.getMessage());
            throw new RuntimeException("Error al listar vehículos", e);
        }
    }

    public List<Vehiculo> buscarVehiculos(String query) {
        try {
            return vehiculoService.buscarVehiculos(query);
        } catch (SQLException e) {
            System.err.println("Error al buscar vehículos: " + e.getMessage());
            throw new RuntimeException("Error al buscar vehículos", e);
        }
    }

    public List<VehiculoSearchDTO> buscarVehiculosConCliente(String query) {
        try {
            return vehiculoService.buscarVehiculosConCliente(query);
        } catch (SQLException e) {
            System.err.println("Error al buscar vehículos (join cliente): " + e.getMessage());
            throw new RuntimeException("Error al buscar vehículos (join cliente)", e);
        }
    }
}
