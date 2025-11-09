package model;

public class Repuesto {
    private int idRepuesto;
    private String nombre;
    private String costoUnitario;
    private int stockDisponible;

    public Repuesto() {}

    public Repuesto(int idRepuesto, String nombre, String costoUnitario, int stockDisponible) {
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

    public String getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(String costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }
}
