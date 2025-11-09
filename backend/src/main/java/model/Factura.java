package model;

import java.time.LocalDate;

public class Factura {
    private int idFactura;
    private double costoManoObra;
    private double total;
    private double impuesto;
    private LocalDate fechaEmision;
    private String estadoPago; // pendiente, pagada, etc.
    private int idOrden; // FK -> OrdenTrabajo

    public Factura() {}

    public Factura(int idFactura, double costoManoObra, double total, double impuesto, LocalDate fechaEmision, String estadoPago, int idOrden) {
        this.idFactura = idFactura;
        this.costoManoObra = costoManoObra;
        this.total = total;
        this.impuesto = impuesto;
        this.fechaEmision = fechaEmision;
        this.estadoPago = estadoPago;
        this.idOrden = idOrden;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public double getCostoManoObra() {
        return costoManoObra;
    }

    public void setCostoManoObra(double costoManoObra) {
        this.costoManoObra = costoManoObra;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }
}
