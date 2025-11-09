package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTester {
    
    public static void main(String[] args) {
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("✓ Conexión establecida");
            
            // Listar todas las tablas en tu base de datos
            String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
            ResultSet rs = stmt.executeQuery(query);
            
            System.out.println("\nTablas en la base de datos:");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("table_name"));
            }
            
            // Aquí puedes agregar más consultas de prueba
            // Por ejemplo:
            // ResultSet clientes = stmt.executeQuery("SELECT * FROM cliente LIMIT 5");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
