package model;

public class TipoServicio {
    private int idTipo;
    private String nombreTipo;
    private String descripcionTipo;  // nullable

    public TipoServicio() {}

    public TipoServicio(int idTipo, String nombreTipo, String descripcionTipo) {
        this.idTipo = idTipo;
        this.nombreTipo = nombreTipo;
        this.descripcionTipo = descripcionTipo;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public void setDescripcionTipo(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }
}
