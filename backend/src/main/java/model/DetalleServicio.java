package model;

public class DetalleServicio {
    private int idDetalleServicio;
    private int idServicio; // FK -> Servicio
    private int idOrden;    // FK -> OrdenTrabajo

    public DetalleServicio() {}

    public DetalleServicio(int idDetalleServicio, int idServicio, int idOrden) {
        this.idDetalleServicio = idDetalleServicio;
        this.idServicio = idServicio;
        this.idOrden = idOrden;
    }

    public int getIdDetalleServicio() {
        return idDetalleServicio;
    }

    public void setIdDetalleServicio(int idDetalleServicio) {
        this.idDetalleServicio = idDetalleServicio;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }
}
