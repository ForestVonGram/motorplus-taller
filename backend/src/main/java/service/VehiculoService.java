package service;

import dao.VehiculoDAO;
import model.Vehiculo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class VehiculoService {
    private final VehiculoDAO vehiculoDAO;

    public VehiculoService() {
        this.vehiculoDAO = new VehiculoDAO();
    }

    public Vehiculo crearVehiculo(Vehiculo vehiculo) throws SQLException {
        return vehiculoDAO.insert(vehiculo);
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) throws SQLException {
        return vehiculoDAO.update(vehiculo);
    }

    public boolean eliminarVehiculo(String placa) throws SQLException {
        return vehiculoDAO.delete(placa);
    }

    public Optional<Vehiculo> buscarVehiculoPorPlaca(String placa) throws SQLException {
        return vehiculoDAO.findById(placa);
    }

    public List<Vehiculo> listarTodosVehiculos() throws SQLException {
        return vehiculoDAO.findAll();
    }
}
