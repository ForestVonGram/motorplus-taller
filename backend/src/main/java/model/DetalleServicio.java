package model;

public class DetalleServicio {
    private int idServicio; // FK -> Servicio (parte de PK compuesta)
    private int idOrden;    // FK -> OrdenTrabajo (parte de PK compuesta)

    public DetalleServicio() {}

    public DetalleServicio(int idServicio, int idOrden) {
        this.idServicio = idServicio;
        this.idOrden = idOrden;
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
