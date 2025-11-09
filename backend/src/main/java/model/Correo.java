package model;

public class Correo {
    private String drcCorreo;  // PK
    private int idCliente;     // FK -> Cliente

    public Correo() {}

    public Correo(String drcCorreo, int idCliente) {
        this.drcCorreo = drcCorreo;
        this.idCliente = idCliente;
    }

    public String getDrcCorreo() {
        return drcCorreo;
    }

    public void setDrcCorreo(String drcCorreo) {
        this.drcCorreo = drcCorreo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
