package dto.reportes;

import java.time.LocalDate;
import java.util.List;

public class TrazabilidadOrdenesDTO {
    public static class OrdenInfo {
        public int idOrden;
        public String placa;
        public LocalDate fechaIngreso;
        public LocalDate fechaFinalizacion;
        public String diagnosticoInicial;
        public int idMecanico;
        public String nombreMecanico;
    }

    public static class ServicioRealizado {
        public int idServicio;
        public String nombreServicio;
    }

    public static class RepuestoUtilizado {
        public int idRepuesto;
        public String nombreRepuesto;
        public int cantidad;
    }

    private OrdenInfo orden;
    private List<ServicioRealizado> servicios;
    private List<RepuestoUtilizado> repuestos;

    public TrazabilidadOrdenesDTO() {}

    public TrazabilidadOrdenesDTO(OrdenInfo orden, List<ServicioRealizado> servicios, List<RepuestoUtilizado> repuestos) {
        this.orden = orden;
        this.servicios = servicios;
        this.repuestos = repuestos;
    }

    public OrdenInfo getOrden() { return orden; }
    public void setOrden(OrdenInfo orden) { this.orden = orden; }
    public List<ServicioRealizado> getServicios() { return servicios; }
    public void setServicios(List<ServicioRealizado> servicios) { this.servicios = servicios; }
    public List<RepuestoUtilizado> getRepuestos() { return repuestos; }
    public void setRepuestos(List<RepuestoUtilizado> repuestos) { this.repuestos = repuestos; }
}
