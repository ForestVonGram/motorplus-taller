package model;

public class DetalleOrden {
    private int idDetalleOrden;
    private int idOrden;     // FK -> OrdenTrabajo
    private int idMecanico;  // FK -> Mecanico
    private String rolMecanico;

    public DetalleOrden() {}

    public DetalleOrden(int idDetalleOrden, int idOrden, int idMecanico, String rolMecanico) {
        this.idDetalleOrden = idDetalleOrden;
        this.idOrden = idOrden;
        this.idMecanico = idMecanico;
        this.rolMecanico = rolMecanico;
    }

    public int getIdDetalleOrden() {
        return idDetalleOrden;
    }

    public void setIdDetalleOrden(int idDetalleOrden) {
        this.idDetalleOrden = idDetalleOrden;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdMecanico() {
        return idMecanico;
    }

    public void setIdMecanico(int idMecanico) {
        this.idMecanico = idMecanico;
    }

    public String getRolMecanico() {
        return rolMecanico;
    }

    public void setRolMecanico(String rolMecanico) {
        this.rolMecanico = rolMecanico;
    }
}
