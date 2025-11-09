package service;

import dao.FacturaDAO;
import model.Factura;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FacturaService {
    private final FacturaDAO facturaDAO;

    public FacturaService() {
        this.facturaDAO = new FacturaDAO();
    }

    public Factura crearFactura(Factura factura) throws SQLException {
        return facturaDAO.insert(factura);
    }

    public boolean actualizarFactura(Factura factura) throws SQLException {
        return facturaDAO.update(factura);
    }

    public boolean eliminarFactura(Integer id) throws SQLException {
        return facturaDAO.delete(id);
    }

    public Optional<Factura> buscarFacturaPorId(Integer id) throws SQLException {
        return facturaDAO.findById(id);
    }

    public List<Factura> listarTodasFacturas() throws SQLException {
        return facturaDAO.findAll();
    }
}
