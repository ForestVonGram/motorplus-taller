package service;

import dao.RepuestoDAO;
import model.Repuesto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RepuestoService {
    private final RepuestoDAO repuestoDAO;

    public RepuestoService() {
        this.repuestoDAO = new RepuestoDAO();
    }

    public Repuesto crearRepuesto(Repuesto repuesto) throws SQLException {
        return repuestoDAO.insert(repuesto);
    }

    public boolean actualizarRepuesto(Repuesto repuesto) throws SQLException {
        return repuestoDAO.update(repuesto);
    }

    public boolean eliminarRepuesto(Integer id) throws SQLException {
        return repuestoDAO.delete(id);
    }

    public Optional<Repuesto> buscarRepuestoPorId(Integer id) throws SQLException {
        return repuestoDAO.findById(id);
    }

    public List<Repuesto> listarTodosRepuestos() throws SQLException {
        return repuestoDAO.findAll();
    }
}
