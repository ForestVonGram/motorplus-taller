package dto.reportes;

import java.util.List;

public class ClientesActivosDTO {
    public static class TopCliente {
        public int idCliente;
        public String nombre;
        public String apellido;
        public double totalFacturado;
    }

    private int totalClientes;
    private List<TopCliente> topClientes;

    public ClientesActivosDTO() {}

    public ClientesActivosDTO(int totalClientes, List<TopCliente> topClientes) {
        this.totalClientes = totalClientes;
        this.topClientes = topClientes;
    }

    public int getTotalClientes() { return totalClientes; }
    public void setTotalClientes(int totalClientes) { this.totalClientes = totalClientes; }
    public List<TopCliente> getTopClientes() { return topClientes; }
    public void setTopClientes(List<TopCliente> topClientes) { this.topClientes = topClientes; }
}
