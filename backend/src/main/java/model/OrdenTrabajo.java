package model;

import java.time.LocalDate;

public class OrdenTrabajo {
    private int idOrden;
    private LocalDate fechaIngreso;
    private String diagnosticoInicial;
    private LocalDate fechaFinalizacion;  // nullable
    private String placa; // FK -> Vehiculo

    public OrdenTrabajo() {}

    public OrdenTrabajo(int idOrden, LocalDate fechaIngreso, String diagnosticoInicial, LocalDate fechaFinalizacion, String placa) {
        this.idOrden = idOrden;
        this.fechaIngreso = fechaIngreso;
        this.diagnosticoInicial = diagnosticoInicial;
        this.fechaFinalizacion = fechaFinalizacion;
        this.placa = placa;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getDiagnosticoInicial() {
        return diagnosticoInicial;
    }

    public void setDiagnosticoInicial(String diagnosticoInicial) {
        this.diagnosticoInicial = diagnosticoInicial;
    }

    public LocalDate getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDate fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
