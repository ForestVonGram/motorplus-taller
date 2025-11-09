package model;

public class RepuestoProveedor {
    private int idRepuesto;  // FK -> Repuesto (parte de PK compuesta)
    private int idProveedor; // FK -> Proveedor (parte de PK compuesta)

    public RepuestoProveedor() {}

    public RepuestoProveedor(int idRepuesto, int idProveedor) {
        this.idRepuesto = idRepuesto;
        this.idProveedor = idProveedor;
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
