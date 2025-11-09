package model;

public class DetalleOrden {
    private int idOrden;     // FK -> OrdenTrabajo (parte de PK compuesta)
    private int idMecanico;  // FK -> Mecanico (parte de PK compuesta)
    private String rolMecanico;

    public DetalleOrden() {}

    public DetalleOrden(int idOrden, int idMecanico, String rolMecanico) {
        this.idOrden = idOrden;
        this.idMecanico = idMecanico;
        this.rolMecanico = rolMecanico;
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
