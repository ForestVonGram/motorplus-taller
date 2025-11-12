package dto.reportes;

import java.time.LocalDate;
import java.util.List;

public class HistorialVehiculoDTO {
    public static class VehiculoInfo {
        public String placa;
        public String marca;
        public Integer anio;
        public Integer idCliente;
    }
    public static class OrdenItem {
        public int idOrden;
        public LocalDate fechaIngreso;
        public String diagnosticoInicial;
        public LocalDate fechaFinalizacion;
    }
    public static class FacturaItem {
        public int idFactura;
        public LocalDate fechaEmision;
        public double total;
        public String estadoPago;
        public int idOrden;
    }

    private VehiculoInfo vehiculo;
    private List<OrdenItem> ordenes;
    private List<FacturaItem> facturas;

    public HistorialVehiculoDTO() {}

    public HistorialVehiculoDTO(VehiculoInfo vehiculo, List<OrdenItem> ordenes, List<FacturaItem> facturas) {
        this.vehiculo = vehiculo;
        this.ordenes = ordenes;
        this.facturas = facturas;
    }

    public VehiculoInfo getVehiculo() { return vehiculo; }
    public void setVehiculo(VehiculoInfo vehiculo) { this.vehiculo = vehiculo; }
    public List<OrdenItem> getOrdenes() { return ordenes; }
    public void setOrdenes(List<OrdenItem> ordenes) { this.ordenes = ordenes; }
    public List<FacturaItem> getFacturas() { return facturas; }
    public void setFacturas(List<FacturaItem> facturas) { this.facturas = facturas; }
}
