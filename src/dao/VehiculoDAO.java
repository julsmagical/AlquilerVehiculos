package dao;

import db.ConexionBD;
import interfaces.IBuscable;
import interfaces.IRegistrable;
import dto.Vehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO implements IBuscable, IRegistrable {
    private Connection con;

    public VehiculoDAO() {
        this.con = ConexionBD.getConexion();
    }

    @Override
    public boolean registrar(Object obj) {
        Vehiculo v = (Vehiculo) obj;
        String sql = "INSERT INTO vehiculo (id_tipo, marca, modelo, anio, placa, color, precio_dia, stock) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, v.getIdTipo());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnio());
            ps.setString(5, v.getPlaca());
            ps.setString(6, v.getColor());
            ps.setDouble(7, v.getPrecio());
            ps.setInt(8, v.getStock());
            ps.executeUpdate();
            System.out.println("Vehículo registrado correctamente.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar vehículo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void listarTodos() {
        String sql = "SELECT v.id_vehiculo, v.id_tipo, t.nombre AS tipo_nombre, "
                + "v.marca, v.modelo, v.anio, v.placa, v.color, v.precio_dia, v.stock "
                + "FROM vehiculo v JOIN tipo_vehiculo t ON v.id_tipo = t.id_tipo "
                + "ORDER BY v.marca";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            boolean hayDatos = false;
            while (rs.next()) {
                hayDatos = true;
                Vehiculo v = mapearVehiculo(rs);
                v.mostrarDetalle();
            }
            if (!hayDatos) System.out.println("No hay vehículos registrados.");

        } catch (SQLException e) {
            System.out.println("Error al listar vehículos: " + e.getMessage());
        }
    }

    public List<Vehiculo> listarConStock() {
        String sql = "SELECT v.id_vehiculo, v.id_tipo, t.nombre AS tipo_nombre, "
                + "v.marca, v.modelo, v.anio, v.placa, v.color, v.precio_dia, v.stock "
                + "FROM vehiculo v JOIN tipo_vehiculo t ON v.id_tipo = t.id_tipo "
                + "WHERE v.stock > 0 ORDER BY v.marca";
        List<Vehiculo> lista = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearVehiculo(rs));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return lista;
    }

    public void listarPorTipo(String tipoNombre) {
        String sql = "SELECT v.id_vehiculo, v.id_tipo, t.nombre AS tipo_nombre, "
                + "v.marca, v.modelo, v.anio, v.placa, v.color, v.precio_dia, v.stock "
                + "FROM vehiculo v JOIN tipo_vehiculo t ON v.id_tipo = t.id_tipo "
                + "WHERE t.nombre LIKE ? ORDER BY v.marca";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tipoNombre + "%");
            ResultSet rs = ps.executeQuery();
            boolean hayDatos = false;
            while (rs.next()) {
                hayDatos = true;
                mapearVehiculo(rs).mostrarDetalle();
            }
            if (!hayDatos) System.out.println("No se encontraron vehículos de ese tipo.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public Object buscarPorId(int id) {
        String sql = "SELECT v.id_vehiculo, v.id_tipo, t.nombre AS tipo_nombre, "
                + "v.marca, v.modelo, v.anio, v.placa, v.color, v.precio_dia, v.stock "
                + "FROM vehiculo v JOIN tipo_vehiculo t ON v.id_tipo = t.id_tipo "
                + "WHERE v.id_vehiculo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearVehiculo(rs);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Object buscarPorNombre(String nombre) {
        String sql = "SELECT v.id_vehiculo, v.id_tipo, t.nombre AS tipo_nombre, "
                + "v.marca, v.modelo, v.anio, v.placa, v.color, v.precio_dia, v.stock "
                + "FROM vehiculo v JOIN tipo_vehiculo t ON v.id_tipo = t.id_tipo "
                + "WHERE v.marca LIKE ? OR v.modelo LIKE ?";
        List<Vehiculo> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearVehiculo(rs));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return lista;
    }

    public boolean descontarStock(Connection conTran, int idVehiculo, int cantidad) {
        String sql = "UPDATE vehiculo SET stock = stock - ? WHERE id_vehiculo = ? AND stock >= ?";
        try (PreparedStatement ps = conTran.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idVehiculo);
            ps.setInt(3, cantidad);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al descontar stock: " + e.getMessage());
            return false;
        }
    }

    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        return new Vehiculo(
                rs.getInt("id_vehiculo"),
                rs.getInt("id_tipo"),
                rs.getString("tipo_nombre"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getInt("anio"),
                rs.getString("placa"),
                rs.getString("color"),
                rs.getDouble("precio_dia"),
                rs.getInt("stock")
        );
    }
}