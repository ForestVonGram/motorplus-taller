package controller;

import dto.ClienteSearchDTO;
import model.Cliente;
import service.ClienteService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController() {
        this.clienteService = new ClienteService();
    }

    public Cliente crearCliente(Cliente cliente) {
        try {
            return clienteService.crearCliente(cliente);
        } catch (SQLException e) {
            System.err.println("Error al crear cliente: " + e.getMessage());
            throw new RuntimeException("Error al crear cliente", e);
        }
    }

    public boolean actualizarCliente(Cliente cliente) {
        try {
            return clienteService.actualizarCliente(cliente);
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            throw new RuntimeException("Error al actualizar cliente", e);
        }
    }

    public boolean eliminarCliente(Integer id) {
        try {
            return clienteService.eliminarCliente(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            throw new RuntimeException("Error al eliminar cliente", e);
        }
    }

    public Optional<Cliente> buscarClientePorId(Integer id) {
        try {
            return clienteService.buscarClientePorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
            throw new RuntimeException("Error al buscar cliente", e);
        }
    }

    public List<Cliente> listarTodosClientes() {
        try {
            return clienteService.listarTodosClientes();
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
            throw new RuntimeException("Error al listar clientes", e);
        }
    }

    public List<ClienteSearchDTO> buscarClientesConVehiculos(String query) {
        try {
            return clienteService.buscarClientesConVehiculos(query);
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
            throw new RuntimeException("Error al buscar clientes", e);
        }
    }
}
