package dto.reportes;

import java.util.List;

public class AnalisisRentabilidadDTO {
    public static class ServicioMargen {
        public int idServicio;
        public String nombre;
        public double margenPromedio;
    }

    public static class RepuestoMargen {
        public int idRepuesto;
        public String nombre;
        public double margenPromedio;
    }

    private List<ServicioMargen> servicios;
    private List<RepuestoMargen> repuestos;

    public AnalisisRentabilidadDTO() {}

    public AnalisisRentabilidadDTO(List<ServicioMargen> servicios, List<RepuestoMargen> repuestos) {
        this.servicios = servicios;
        this.repuestos = repuestos;
    }

    public List<ServicioMargen> getServicios() { return servicios; }
    public void setServicios(List<ServicioMargen> servicios) { this.servicios = servicios; }
    public List<RepuestoMargen> getRepuestos() { return repuestos; }
    public void setRepuestos(List<RepuestoMargen> repuestos) { this.repuestos = repuestos; }
}
