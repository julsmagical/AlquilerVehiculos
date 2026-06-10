package dao;

import db.ConexionBD;
import interfaces.IRegistrable;
import dto.Alquiler;
import dto.DetalleAlquiler;

import java.sql.*;

public class AlquilerDAO implements IRegistrable {
    private Connection con;
    private VehiculoDAO vehiculoDAO;

    public AlquilerDAO() {
        this.con = ConexionBD.getConexion();
        this.vehiculoDAO = new VehiculoDAO();
    }

    //Registra el alquiler completo usando transacción SQL.
    //Si algo falla - rollback, si todo va bien - commit.
    @Override
    public boolean registrar(Object obj) {
        Alquiler a = (Alquiler) obj;

        try {
            con.setAutoCommit(false);

            String sqlAlq = "INSERT INTO alquiler (id_cliente, fecha_devolucion, total, estado) "
                    + "VALUES (?, ?, ?, ?)";
            int idAlquiler;
            try (PreparedStatement ps = con.prepareStatement(sqlAlq, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, a.getCliente().getIdCliente());
                ps.setDate(2, Date.valueOf(a.getFechaDevolucion()));
                ps.setDouble(3, a.getTotal());
                ps.setString(4, a.getEstado());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                idAlquiler = keys.getInt(1);
                a.setIdAlquiler(idAlquiler);
            }

            String sqlDet = "INSERT INTO detalle_alquiler (id_alquiler, id_vehiculo, cantidad_dias, precio_dia) "
                    + "VALUES (?, ?, ?, ?)";
            for (DetalleAlquiler d : a.getDetalles()) {
                try (PreparedStatement ps = con.prepareStatement(sqlDet)) {
                    ps.setInt(1, idAlquiler);
                    ps.setInt(2, d.getVehiculo().getIdVehiculo());
                    ps.setInt(3, d.getCantidadDias());
                    ps.setDouble(4, d.getPrecioDia());
                    ps.executeUpdate();
                }

                boolean ok = vehiculoDAO.descontarStock(con, d.getVehiculo().getIdVehiculo(), 1);
                if (!ok) {
                    con.rollback();
                    con.setAutoCommit(true);
                    System.out.println("Sin stock para: " + d.getVehiculo().getMarca()
                            + " " + d.getVehiculo().getModelo() + ". Alquiler cancelado.");
                    return false;
                }
            }

            con.commit();
            con.setAutoCommit(true);
            System.out.println("Alquiler registrado. ID: " + idAlquiler);
            return true;

        } catch (SQLException e) {
            try { con.rollback(); con.setAutoCommit(true); } catch (SQLException ex) { /* ignorar */ }
            System.out.println("Error en alquiler (ROLLBACK): " + e.getMessage());
            return false;
        }
    }

    @Override
    public void listarTodos() {
    }

    public void listarPorCliente(int idCliente) {
        String sql = "SELECT a.id_alquiler, a.fecha_alquiler, a.fecha_devolucion, "
                + "a.total, a.estado, "
                + "d.id_vehiculo, v.marca, v.modelo, d.cantidad_dias, d.precio_dia, d.subtotal "
                + "FROM alquiler a "
                + "JOIN detalle_alquiler d ON a.id_alquiler = d.id_alquiler "
                + "JOIN vehiculo v ON d.id_vehiculo = v.id_vehiculo "
                + "WHERE a.id_cliente = ? "
                + "ORDER BY a.id_alquiler DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            int lastId = -1;
            boolean hayDatos = false;

            while (rs.next()) {
                hayDatos = true;
                int idAlq = rs.getInt("id_alquiler");

                if (idAlq != lastId) {
                    lastId = idAlq;
                    System.out.println("══════════════════════════════════════");
                    System.out.println("Alquiler #" + idAlq
                            + " | F.Alq: " + rs.getString("fecha_alquiler")
                            + " | Dev: " + rs.getDate("fecha_devolucion")
                            + " | Estado: " + rs.getString("estado")
                            + " | Total: $" + String.format("%.2f", rs.getDouble("total")));
                    System.out.println("  Detalle:");
                }
                System.out.printf("    - %s %s | %d días | $%.2f/día | Subtotal: $%.2f%n",
                        rs.getString("marca"), rs.getString("modelo"),
                        rs.getInt("cantidad_dias"), rs.getDouble("precio_dia"),
                        rs.getDouble("subtotal"));
            }

            if (!hayDatos) System.out.println("Este cliente no tiene alquileres.");

        } catch (SQLException e) {
            System.out.println("Error al consultar alquileres: " + e.getMessage());
        }
    }
}