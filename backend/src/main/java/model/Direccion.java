package model;

public class Direccion {
    private int idDireccion;
    private String ciudad;
    private String barrio;
    private String calle;
    private String numero;
    private int idCliente; // FK -> Cliente

    public Direccion() {}

    public Direccion(int idDireccion, String ciudad, String barrio, String calle, String numero, int idCliente) {
        this.idDireccion = idDireccion;
        this.ciudad = ciudad;
        this.barrio = barrio;
        this.calle = calle;
        this.numero = numero;
        this.idCliente = idCliente;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
