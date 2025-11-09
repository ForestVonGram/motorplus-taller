package model;

public class Mecanico {
    private int idMecanico;
    private String nombre;
    private Integer idSupervisor; // FK -> Mecanico (self reference)

    public Mecanico() {}

    public Mecanico(int idMecanico, String nombre, Integer idSupervisor) {
        this.idMecanico = idMecanico;
        this.nombre = nombre;
        this.idSupervisor = idSupervisor;
    }

    public int getIdMecanico() {
        return idMecanico;
    }

    public void setIdMecanico(int idMecanico) {
        this.idMecanico = idMecanico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(Integer idSupervisor) {
        this.idSupervisor = idSupervisor;
    }
}
