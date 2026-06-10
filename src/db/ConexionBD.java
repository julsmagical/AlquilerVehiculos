package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL  = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=db_alquiler_vehiculos;"
            + "encrypt=false;"
            + "trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "Admin1234@";

    private static Connection instancia = null;

    private ConexionBD() {}

    public static Connection getConexion() {
        try {
            if (instancia == null || instancia.isClosed()) {
                instancia = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Conexión establecida con SQL Server.");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return instancia;
    }
}