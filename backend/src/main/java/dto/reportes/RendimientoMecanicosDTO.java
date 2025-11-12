package dto.reportes;

import java.util.List;

public class RendimientoMecanicosDTO {
    public static class MecanicoStats {
        public int idMecanico;
        public String nombre;
        public String apellido;
        public int ordenesCompletadas;
    }

    private int totalOrdenesCompletadas;
    private List<MecanicoStats> mecanicos;

    public RendimientoMecanicosDTO() {}

    public RendimientoMecanicosDTO(int totalOrdenesCompletadas, List<MecanicoStats> mecanicos) {
        this.totalOrdenesCompletadas = totalOrdenesCompletadas;
        this.mecanicos = mecanicos;
    }

    public int getTotalOrdenesCompletadas() { return totalOrdenesCompletadas; }
    public void setTotalOrdenesCompletadas(int totalOrdenesCompletadas) { this.totalOrdenesCompletadas = totalOrdenesCompletadas; }
    public List<MecanicoStats> getMecanicos() { return mecanicos; }
    public void setMecanicos(List<MecanicoStats> mecanicos) { this.mecanicos = mecanicos; }
}
