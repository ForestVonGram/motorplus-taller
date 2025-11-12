package dto.reportes;

public class OrdenesPorEstadoDTO {
    private int pendientes;
    private int enProceso;
    private int completadas;
    private int facturadas;

    public OrdenesPorEstadoDTO() {}

    public OrdenesPorEstadoDTO(int pendientes, int enProceso, int completadas, int facturadas) {
        this.pendientes = pendientes;
        this.enProceso = enProceso;
        this.completadas = completadas;
        this.facturadas = facturadas;
    }

    public int getPendientes() { return pendientes; }
    public void setPendientes(int pendientes) { this.pendientes = pendientes; }
    public int getEnProceso() { return enProceso; }
    public void setEnProceso(int enProceso) { this.enProceso = enProceso; }
    public int getCompletadas() { return completadas; }
    public void setCompletadas(int completadas) { this.completadas = completadas; }
    public int getFacturadas() { return facturadas; }
    public void setFacturadas(int facturadas) { this.facturadas = facturadas; }
}
