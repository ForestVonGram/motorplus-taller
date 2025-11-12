package service;

import dao.FacturaDAO;
import dao.MecanicoDAO;
import dao.OrdenTrabajoDAO;
import dao.VehiculoDAO;
import dto.DashboardStatsDTO;

import java.sql.SQLException;

public class DashboardService {
    private final VehiculoDAO vehiculoDAO;
    private final OrdenTrabajoDAO ordenTrabajoDAO;
    private final MecanicoDAO mecanicoDAO;
    private final FacturaDAO facturaDAO;

    public DashboardService() {
        this.vehiculoDAO = new VehiculoDAO();
        this.ordenTrabajoDAO = new OrdenTrabajoDAO();
        this.mecanicoDAO = new MecanicoDAO();
        this.facturaDAO = new FacturaDAO();
    }

    public DashboardStatsDTO getDashboardStats() throws SQLException {
        int totalVehiculos = vehiculoDAO.countAll();
        int ordenesActivas = ordenTrabajoDAO.countActiveOrders();
        int mecanicosDisponibles = mecanicoDAO.countAll();
        double facturacionMes = facturaDAO.getTotalCurrentMonth();

        return new DashboardStatsDTO(totalVehiculos, ordenesActivas, mecanicosDisponibles, facturacionMes);
    }
}
