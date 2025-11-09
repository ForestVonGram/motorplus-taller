package util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionTest {

    @Test
    public void testDatabaseConnection() {
        try (Connection conn = ConnectionManager.getConnection()) {
            assertNotNull(conn, "La conexión no debería ser null");
            assertFalse(conn.isClosed(), "La conexión debería estar abierta");
            
            // Verificar que podemos ejecutar una consulta simple
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT version()")) {
                assertTrue(rs.next(), "Debería haber al menos un resultado");
                String version = rs.getString(1);
                System.out.println("Conexión exitosa. Versión de PostgreSQL: " + version);
            }
            
        } catch (Exception e) {
            fail("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}
