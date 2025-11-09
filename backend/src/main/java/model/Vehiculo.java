package model;

public class Vehiculo {
    private String placa; // PK
    private String marca;
    private int anio;
    private int idCliente; // FK -> Cliente

    public Vehiculo() {}

    public Vehiculo(String placa, String marca, int anio, int idCliente) {
        this.placa = placa;
        this.marca = marca;
        this.anio = anio;
        this.idCliente = idCliente;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
