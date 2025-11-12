package api;

import controller.ClienteController;
import dto.DashboardStatsDTO;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.plugin.bundled.CorsPluginConfig;
import model.Cliente;
import service.DashboardService;

import java.sql.SQLException;
import java.util.Optional;

public class Server {
    public static void main(String[] args) {
        int port = Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(8080);
        var app = Javalin.create(config -> {
            // CORS para desarrollo (Vite en localhost:5173)
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
            // Mapper JSON con Jackson
            config.jsonMapper(new JavalinJackson());
        }).start(port);

        var clientes = new ClienteController();
        var dashboardService = new DashboardService();

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
    }
}
