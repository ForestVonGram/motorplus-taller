package dto.reportes;

import java.util.List;

public class FacturacionMensualDTO {
    public static class MesFacturacion {
        public String mes; // formato YYYY-MM
        public double total;
    }

    private double totalAnio;
    private List<MesFacturacion> meses;

    public FacturacionMensualDTO() {}

    public FacturacionMensualDTO(double totalAnio, List<MesFacturacion> meses) {
        this.totalAnio = totalAnio;
        this.meses = meses;
    }

    public double getTotalAnio() { return totalAnio; }
    public void setTotalAnio(double totalAnio) { this.totalAnio = totalAnio; }
    public List<MesFacturacion> getMeses() { return meses; }
    public void setMeses(List<MesFacturacion> meses) { this.meses = meses; }
}
