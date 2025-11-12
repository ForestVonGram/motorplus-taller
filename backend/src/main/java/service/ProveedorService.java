package service;

import dao.ProveedorDAO;
import model.Proveedor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProveedorService {
    private final ProveedorDAO proveedorDAO;

    public ProveedorService() {
        this.proveedorDAO = new ProveedorDAO();
    }

    public Proveedor crearProveedor(Proveedor proveedor) throws SQLException {
        return proveedorDAO.insert(proveedor);
    }

    public boolean actualizarProveedor(Proveedor proveedor) throws SQLException {
        return proveedorDAO.update(proveedor);
    }

    public boolean eliminarProveedor(Integer id) throws SQLException {
        return proveedorDAO.delete(id);
    }

    public Optional<Proveedor> buscarProveedorPorId(Integer id) throws SQLException {
        return proveedorDAO.findById(id);
    }

    public List<Proveedor> listarTodosProveedores() throws SQLException {
        return proveedorDAO.findAll();
    }

    public List<Proveedor> buscarProveedoresPorNombre(String termino) throws SQLException {
        return proveedorDAO.searchByNombre(termino);
    }
}
