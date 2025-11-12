package dto.reportes;

import java.time.LocalDate;
import java.util.List;

public class FacturasPorClienteDTO {
    public static class ClienteInfo {
        public int idCliente;
        public String nombre;
        public String apellido;
    }
    public static class FacturaItem {
        public int idFactura;
        public LocalDate fechaEmision;
        public double total;
        public String estadoPago;
        public int idOrden;
    }

    private ClienteInfo cliente;
    private List<FacturaItem> facturas;

    public FacturasPorClienteDTO() {}

    public FacturasPorClienteDTO(ClienteInfo cliente, List<FacturaItem> facturas) {
        this.cliente = cliente;
        this.facturas = facturas;
    }

    public ClienteInfo getCliente() { return cliente; }
    public void setCliente(ClienteInfo cliente) { this.cliente = cliente; }
    public List<FacturaItem> getFacturas() { return facturas; }
    public void setFacturas(List<FacturaItem> facturas) { this.facturas = facturas; }
}
