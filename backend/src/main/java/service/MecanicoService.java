package service;

import dao.MecanicoDAO;
import model.Mecanico;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MecanicoService {
    private final MecanicoDAO mecanicoDAO;

    public MecanicoService() {
        this.mecanicoDAO = new MecanicoDAO();
    }

    public Mecanico crearMecanico(Mecanico mecanico) throws SQLException {
        return mecanicoDAO.insert(mecanico);
    }

    public boolean actualizarMecanico(Mecanico mecanico) throws SQLException {
        return mecanicoDAO.update(mecanico);
    }

    public boolean eliminarMecanico(Integer id) throws SQLException {
        return mecanicoDAO.delete(id);
    }

    public Optional<Mecanico> buscarMecanicoPorId(Integer id) throws SQLException {
        return mecanicoDAO.findById(id);
    }

    public List<Mecanico> listarTodosMecanicos() throws SQLException {
        return mecanicoDAO.findAll();
    }

    public List<Mecanico> buscarMecanicosPorNombre(String termino) throws SQLException {
        return mecanicoDAO.searchByNombre(termino);
    }
}
