package service;

import dao.ClienteDAO;
import dto.ClienteSearchDTO;
import model.Cliente;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public Cliente crearCliente(Cliente cliente) throws SQLException {
        return clienteDAO.insert(cliente);
    }

    public boolean actualizarCliente(Cliente cliente) throws SQLException {
        return clienteDAO.update(cliente);
    }

    public boolean eliminarCliente(Integer id) throws SQLException {
        return clienteDAO.delete(id);
    }

    public Optional<Cliente> buscarClientePorId(Integer id) throws SQLException {
        return clienteDAO.findById(id);
    }

    public List<Cliente> listarTodosClientes() throws SQLException {
        return clienteDAO.findAll();
    }

    public List<ClienteSearchDTO> buscarClientesConVehiculos(String query) throws SQLException {
        return clienteDAO.searchWithVehiculos(query);
    }
}
