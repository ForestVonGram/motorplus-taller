package dto;

public class VehiculoSearchDTO {
    private String placa;
    private String marca;
    private int anio;
    private int idCliente;
    private String clienteNombre;
    private String clienteApellido;

    public VehiculoSearchDTO() {}

    public VehiculoSearchDTO(String placa, String marca, int anio, int idCliente, String clienteNombre, String clienteApellido) {
        this.placa = placa;
        this.marca = marca;
        this.anio = anio;
        this.idCliente = idCliente;
        this.clienteNombre = clienteNombre;
        this.clienteApellido = clienteApellido;
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public String getClienteApellido() { return clienteApellido; }
    public void setClienteApellido(String clienteApellido) { this.clienteApellido = clienteApellido; }
}