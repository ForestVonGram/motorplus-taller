package dto;

public class ClienteSearchDTO {
    private int idCliente;
    private String nombre;
    private String apellido;
    // Cadena con las placas (u otros datos) de los vehículos del cliente, separadas por coma
    private String vehiculos;

    public ClienteSearchDTO() {}

    public ClienteSearchDTO(int idCliente, String nombre, String apellido, String vehiculos) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.vehiculos = vehiculos;
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getVehiculos() { return vehiculos; }
    public void setVehiculos(String vehiculos) { this.vehiculos = vehiculos; }
}