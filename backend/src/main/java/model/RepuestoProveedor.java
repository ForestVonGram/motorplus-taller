package model;

public class RepuestoProveedor {
    private int idDetalleProveedor;
    private int idRepuesto;  // FK -> Repuesto
    private int idProveedor; // FK -> Proveedor

    public RepuestoProveedor() {}

    public RepuestoProveedor(int idDetalleProveedor, int idRepuesto, int idProveedor) {
        this.idDetalleProveedor = idDetalleProveedor;
        this.idRepuesto = idRepuesto;
        this.idProveedor = idProveedor;
    }

    public int getIdDetalleProveedor() {
        return idDetalleProveedor;
    }

    public void setIdDetalleProveedor(int idDetalleProveedor) {
        this.idDetalleProveedor = idDetalleProveedor;
    }

    public int getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(int idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
}
