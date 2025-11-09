package model;

public class Repuesto {
    private int idRepuesto;
    private String nombre;
    private double costoUnitario;
    private int stockDisponible;

    public Repuesto() {}

    public Repuesto(int idRepuesto, String nombre, double costoUnitario, int stockDisponible) {
        this.idRepuesto = idRepuesto;
        this.nombre = nombre;
        this.costoUnitario = costoUnitario;
        this.stockDisponible = stockDisponible;
    }

    public int getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(int idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }
}
