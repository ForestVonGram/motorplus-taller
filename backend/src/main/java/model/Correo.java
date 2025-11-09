package model;

public class Correo {
    private int idCorreo;
    private String descripcion;
    private int idCliente; // FK -> Cliente

    public Correo() {}

    public Correo(int idCorreo, String descripcion, int idCliente) {
        this.idCorreo = idCorreo;
        this.descripcion = descripcion;
        this.idCliente = idCliente;
    }

    public int getIdCorreo() {
        return idCorreo;
    }

    public void setIdCorreo(int idCorreo) {
        this.idCorreo = idCorreo;
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
