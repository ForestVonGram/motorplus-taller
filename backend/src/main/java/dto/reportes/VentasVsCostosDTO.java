package dto.reportes;

public class VentasVsCostosDTO {
    private double ventas;
    private double costos;
    private double margen;
    private double porcentajeMargen;

    public VentasVsCostosDTO() {}

    public VentasVsCostosDTO(double ventas, double costos, double margen, double porcentajeMargen) {
        this.ventas = ventas;
        this.costos = costos;
        this.margen = margen;
        this.porcentajeMargen = porcentajeMargen;
    }

    public double getVentas() { return ventas; }
    public void setVentas(double ventas) { this.ventas = ventas; }
    public double getCostos() { return costos; }
    public void setCostos(double costos) { this.costos = costos; }
    public double getMargen() { return margen; }
    public void setMargen(double margen) { this.margen = margen; }
    public double getPorcentajeMargen() { return porcentajeMargen; }
    public void setPorcentajeMargen(double porcentajeMargen) { this.porcentajeMargen = porcentajeMargen; }
}
