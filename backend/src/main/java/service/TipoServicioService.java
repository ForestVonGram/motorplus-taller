package service;

import dao.TipoServicioDAO;
import model.TipoServicio;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TipoServicioService {
    private final TipoServicioDAO tipoServicioDAO;

    public TipoServicioService() {
        this.tipoServicioDAO = new TipoServicioDAO();
    }

    public TipoServicio crearTipoServicio(TipoServicio t) throws SQLException {
        return tipoServicioDAO.insert(t);
    }

    public boolean actualizarTipoServicio(TipoServicio t) throws SQLException {
        return tipoServicioDAO.update(t);
    }

    public boolean eliminarTipoServicio(Integer id) throws SQLException {
        return tipoServicioDAO.delete(id);
    }

    public Optional<TipoServicio> buscarTipoServicioPorId(Integer id) throws SQLException {
        return tipoServicioDAO.findById(id);
    }

    public List<TipoServicio> listarTodos() throws SQLException {
        return tipoServicioDAO.findAll();
    }

    public List<TipoServicio> buscar(String query) throws SQLException {
        return tipoServicioDAO.search(query);
    }
}
