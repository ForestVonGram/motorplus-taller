package dto.reportes;

import java.util.List;

public class InventarioPorProveedorDTO {
    public static class ProveedorInfo {
        public int idProveedor;
        public String nombre;
    }

    public static class RepuestoItem {
        public int idRepuesto;
        public String nombre;
        public int stockActual;
        public double precioUnitario;
    }

    private ProveedorInfo proveedor;
    private List<RepuestoItem> repuestos;

    public InventarioPorProveedorDTO() {}

    public InventarioPorProveedorDTO(ProveedorInfo proveedor, List<RepuestoItem> repuestos) {
        this.proveedor = proveedor;
        this.repuestos = repuestos;
    }

    public ProveedorInfo getProveedor() { return proveedor; }
    public void setProveedor(ProveedorInfo proveedor) { this.proveedor = proveedor; }
    public List<RepuestoItem> getRepuestos() { return repuestos; }
    public void setRepuestos(List<RepuestoItem> repuestos) { this.repuestos = repuestos; }
}
