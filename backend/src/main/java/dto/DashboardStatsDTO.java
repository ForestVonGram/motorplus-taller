package dto;

public class DashboardStatsDTO {
    private int totalVehiculos;
    private int ordenesActivas;
    private int mecanicosDisponibles;
    private double facturacionMes;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(int totalVehiculos, int ordenesActivas, int mecanicosDisponibles, double facturacionMes) {
        this.totalVehiculos = totalVehiculos;
        this.ordenesActivas = ordenesActivas;
        this.mecanicosDisponibles = mecanicosDisponibles;
        this.facturacionMes = facturacionMes;
    }

    public int getTotalVehiculos() {
        return totalVehiculos;
    }

    public void setTotalVehiculos(int totalVehiculos) {
        this.totalVehiculos = totalVehiculos;
    }

    public int getOrdenesActivas() {
        return ordenesActivas;
    }

    public void setOrdenesActivas(int ordenesActivas) {
        this.ordenesActivas = ordenesActivas;
    }

    public int getMecanicosDisponibles() {
        return mecanicosDisponibles;
    }

    public void setMecanicosDisponibles(int mecanicosDisponibles) {
        this.mecanicosDisponibles = mecanicosDisponibles;
    }

    public double getFacturacionMes() {
        return facturacionMes;
    }

    public void setFacturacionMes(double facturacionMes) {
        this.facturacionMes = facturacionMes;
    }
}
