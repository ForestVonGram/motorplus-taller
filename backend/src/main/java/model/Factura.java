package model;

import java.time.LocalDate;

public class Factura {
    private int idFactura;
    private String costoManoObra;
    private String total;
    private String impuestos;
    private LocalDate fechaEmision;
    private String estadoPago; // pendiente, pagada, etc.
    private int idOrden; // FK -> OrdenTrabajo

    public Factura() {}

    public Factura(int idFactura, String costoManoObra, String total, String impuestos, LocalDate fechaEmision, String estadoPago, int idOrden) {
        this.idFactura = idFactura;
        this.costoManoObra = costoManoObra;
        this.total = total;
        this.impuestos = impuestos;
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

    public String getCostoManoObra() {
        return costoManoObra;
    }

    public void setCostoManoObra(String costoManoObra) {
        this.costoManoObra = costoManoObra;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(String impuestos) {
        this.impuestos = impuestos;
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
