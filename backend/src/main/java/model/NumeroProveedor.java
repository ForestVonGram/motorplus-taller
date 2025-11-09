package model;

public class NumeroProveedor {
    private String numero;       // PK
    private String descripcion;  // nullable
    private int idProveedor;     // FK -> Proveedor

    public NumeroProveedor() {}

    public NumeroProveedor(String numero, String descripcion, int idProveedor) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.idProveedor = idProveedor;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
}
