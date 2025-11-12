package api;

import controller.ClienteController;
import controller.VehiculoController;
import controller.MecanicoController;
import controller.OrdenTrabajoController;
import dto.DashboardStatsDTO;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.plugin.bundled.CorsPluginConfig;
import model.Cliente;
import model.Vehiculo;
import service.DashboardService;

import java.sql.SQLException;
import java.util.Optional;

public class Server {
    public static void main(String[] args) {
int port = Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(7001);
        var app = Javalin.create(config -> {
            // CORS para desarrollo (Vite en localhost:5173)
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
            // Mapper JSON con Jackson
            config.jsonMapper(new JavalinJackson());
        }).start(port);

        var clientes = new ClienteController();
        var vehiculos = new VehiculoController();
        var mecanicos = new MecanicoController();
        var ordenes = new OrdenTrabajoController();
        var dashboardService = new DashboardService();
        var facturas = new controller.FacturaController();
        var repuestos = new controller.RepuestoController();
        var proveedores = new controller.ProveedorController();
        var servicios = new controller.ServicioController();

        // Salud
        app.get("/api/health", ctx -> ctx.json("ok"));

        // Dashboard
        app.get("/api/dashboard/stats", ctx -> {
            try {
                DashboardStatsDTO stats = dashboardService.getDashboardStats();
                ctx.json(stats);
            } catch (SQLException e) {
                ctx.status(500).json(java.util.Map.of("error", "Database error: " + e.getMessage()));
            } catch (Exception e) {
                ctx.status(500).json(java.util.Map.of("error", "Internal server error: " + e.getMessage()));
            }
        });

        // Clientes
        app.get("/api/clientes", ctx -> {
            var list = clientes.listarTodosClientes();
            ctx.json(list);
        });

        app.get("/api/clientes/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = clientes.buscarClientesConVehiculos(q);
            ctx.json(list);
        });

        app.get("/api/clientes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Cliente> c = clientes.buscarClientePorId(id);
            if (c.isPresent()) ctx.json(c.get());
            else ctx.status(404).result("Cliente no encontrado");
        });

        app.post("/api/clientes", ctx -> {
            Cliente nuevo = ctx.bodyAsClass(Cliente.class);
            Cliente creado = clientes.crearCliente(nuevo);
            ctx.status(201).json(creado);
        });

        app.put("/api/clientes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Cliente update = ctx.bodyAsClass(Cliente.class);
            update.setIdCliente(id);
            boolean ok = clientes.actualizarCliente(update);
            if (ok) ctx.json(update);
            else ctx.status(404).result("Cliente no encontrado");
        });

        app.delete("/api/clientes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = clientes.eliminarCliente(id);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Cliente no encontrado");
        });

        // Vehículos: búsqueda (incluye datos de cliente via JOIN)
        app.get("/api/vehiculos/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = vehiculos.buscarVehiculosConCliente(q);
            ctx.json(list);
        });

        // Opcional: listar o CRUD básico si se necesita más adelante
        app.get("/api/vehiculos", ctx -> ctx.json(vehiculos.listarTodosVehiculos()));
        app.get("/api/vehiculos/{placa}", ctx -> {
            String placa = ctx.pathParam("placa");
            var vOpt = vehiculos.buscarVehiculoPorPlaca(placa);
            if (vOpt.isPresent()) ctx.json(vOpt.get());
            else ctx.status(404).result("Vehículo no encontrado");
        });
        app.post("/api/vehiculos", ctx -> {
            Vehiculo body = ctx.bodyAsClass(Vehiculo.class);
            Vehiculo creado = vehiculos.crearVehiculo(body);
            ctx.status(201).json(creado);
        });
        app.put("/api/vehiculos/{placa}", ctx -> {
            String placa = ctx.pathParam("placa");
            Vehiculo body = ctx.bodyAsClass(Vehiculo.class);
            body.setPlaca(placa);
            boolean ok = vehiculos.actualizarVehiculo(body);
            if (ok) ctx.json(body);
            else ctx.status(404).result("Vehículo no encontrado");
        });
        app.delete("/api/vehiculos/{placa}", ctx -> {
            String placa = ctx.pathParam("placa");
            boolean ok = vehiculos.eliminarVehiculo(placa);
            if (ok) ctx.status(204);
            else ctx.status(404).result("Vehículo no encontrado");
        });

        // Mecánicos: búsqueda por nombre
        app.get("/api/mecanicos/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = mecanicos.buscarMecanicosPorNombre(q);
            ctx.json(list);
        });

        // Órdenes de trabajo: búsqueda por id/placa/diagnóstico/fechas
        app.get("/api/ordenes/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = ordenes.buscarOrdenes(q);
            ctx.json(list);
        });

        // Facturas: búsqueda de órdenes elegibles para facturar
        app.get("/api/facturas/ordenes/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = ordenes.buscarOrdenesParaFacturar(q);
            ctx.json(list);
        });

        // Repuestos: búsqueda de órdenes activas (aptas para gestión de repuestos)
        app.get("/api/repuestos/ordenes/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = ordenes.buscarOrdenesParaRepuestos(q);
            ctx.json(list);
        });

        // Proveedores: listado y búsqueda
        app.get("/api/proveedores", ctx -> ctx.json(proveedores.listarTodosProveedores()));
        app.get("/api/proveedores/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            var list = proveedores.buscarProveedoresPorNombre(q);
            ctx.json(list);
        });

        // Facturas: listado y búsqueda
        app.get("/api/facturas", ctx -> ctx.json(facturas.listarTodasFacturas()));
        app.get("/api/facturas/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            ctx.json(facturas.buscarFacturas(q));
        });

        // Repuestos: listado y búsqueda
        app.get("/api/repuestos", ctx -> ctx.json(repuestos.listarTodosRepuestos()));
        app.get("/api/repuestos/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            ctx.json(repuestos.buscarRepuestos(q));
        });

        // Servicios: búsqueda
        app.get("/api/servicios/search", ctx -> {
            String q = Optional.ofNullable(ctx.queryParam("q")).orElse("");
            ctx.json(servicios.buscarServicios(q));
        });

        // Listado de mecánicos (opcional)
        app.get("/api/mecanicos", ctx -> ctx.json(mecanicos.listarTodosMecanicos()));
    }
}
