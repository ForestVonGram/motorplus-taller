package model;

public class Telefono {
    private int idTelefono;
    private String numero;
    private String descripcion;
    private int idCliente; // FK -> Cliente

    public Telefono() {}

    public Telefono(int idTelefono, String numero, String descripcion, int idCliente) {
        this.idTelefono = idTelefono;
        this.numero = numero;
        this.descripcion = descripcion;
        this.idCliente = idCliente;
    }

    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
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

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
