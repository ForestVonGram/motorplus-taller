package controller;

import model.Factura;
import service.FacturaService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FacturaController {
    private final FacturaService facturaService;

    public FacturaController() {
        this.facturaService = new FacturaService();
    }

    public Factura crearFactura(Factura factura) {
        try {
            return facturaService.crearFactura(factura);
        } catch (SQLException e) {
            System.err.println("Error al crear factura: " + e.getMessage());
            throw new RuntimeException("Error al crear factura", e);
        }
    }

    public boolean actualizarFactura(Factura factura) {
        try {
            return facturaService.actualizarFactura(factura);
        } catch (SQLException e) {
            System.err.println("Error al actualizar factura: " + e.getMessage());
            throw new RuntimeException("Error al actualizar factura", e);
        }
    }

    public boolean eliminarFactura(Integer id) {
        try {
            return facturaService.eliminarFactura(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar factura: " + e.getMessage());
            throw new RuntimeException("Error al eliminar factura", e);
        }
    }

    public Optional<Factura> buscarFacturaPorId(Integer id) {
        try {
            return facturaService.buscarFacturaPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar factura: " + e.getMessage());
            throw new RuntimeException("Error al buscar factura", e);
        }
    }

    public List<Factura> listarTodasFacturas() {
        try {
            return facturaService.listarTodasFacturas();
        } catch (SQLException e) {
            System.err.println("Error al listar facturas: " + e.getMessage());
            throw new RuntimeException("Error al listar facturas", e);
        }
    }

    public List<Factura> buscarFacturas(String query) {
        try {
            return facturaService.buscarFacturas(query);
        } catch (SQLException e) {
            System.err.println("Error al buscar facturas: " + e.getMessage());
            throw new RuntimeException("Error al buscar facturas", e);
        }
    }
}
