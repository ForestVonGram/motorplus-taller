package model;

public class NumeroProveedor {
    private int idNumero;
    private String numero;
    private String descripcion;
    private int idProveedor; // FK -> Proveedor

    public NumeroProveedor() {}

    public NumeroProveedor(int idNumero, String numero, String descripcion, int idProveedor) {
        this.idNumero = idNumero;
        this.numero = numero;
        this.descripcion = descripcion;
        this.idProveedor = idProveedor;
    }

    public int getIdNumero() {
        return idNumero;
    }

    public void setIdNumero(int idNumero) {
        this.idNumero = idNumero;
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
