package dao;

import db.ConexionBD;
import interfaces.IBuscable;
import interfaces.IRegistrable;
import dto.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IBuscable, IRegistrable {
    private Connection con;

    public ClienteDAO() {
        this.con = ConexionBD.getConexion();
    }

    @Override
    public boolean registrar(Object obj) {
        Cliente c = (Cliente) obj;
        String sql = "INSERT INTO cliente (cedula, nombres, apellidos, telefono, email, direccion, licencia) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCedula());
            ps.setString(2, c.getNombres());
            ps.setString(3, c.getApellidos());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getLicencia());
            ps.executeUpdate();
            System.out.println("Cliente registrado correctamente.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void listarTodos() {
        String sql = "SELECT id_cliente, cedula, nombres, apellidos, telefono, "
                + "email, direccion, licencia, CONVERT(VARCHAR, fecha_reg, 120) AS fecha_reg "
                + "FROM cliente ORDER BY apellidos";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            boolean hayDatos = false;
            while (rs.next()) {
                hayDatos = true;
                Cliente c = mapearCliente(rs);
                c.mostrarInfo();
            }
            if (!hayDatos) System.out.println("No hay clientes registrados.");

        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
    }

    @Override
    public Object buscarPorId(int id) {
        String sql = "SELECT id_cliente, cedula, nombres, apellidos, telefono, "
                + "email, direccion, licencia, CONVERT(VARCHAR, fecha_reg, 120) AS fecha_reg "
                + "FROM cliente WHERE id_cliente = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearCliente(rs);
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    public Cliente buscarPorCedula(String cedula) {
        String sql = "SELECT id_cliente, cedula, nombres, apellidos, telefono, "
                + "email, direccion, licencia, CONVERT(VARCHAR, fecha_reg, 120) AS fecha_reg "
                + "FROM cliente WHERE cedula = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearCliente(rs);
        } catch (SQLException e) {
            System.out.println("Error al buscar por cédula: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Object buscarPorNombre(String nombre) {
        String sql = "SELECT id_cliente, cedula, nombres, apellidos, telefono, "
                + "email, direccion, licencia, CONVERT(VARCHAR, fecha_reg, 120) AS fecha_reg "
                + "FROM cliente WHERE nombres LIKE ? OR apellidos LIKE ?";
        List<Cliente> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearCliente(rs));
        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        return lista;
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id_cliente"),
                rs.getString("cedula"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("direccion"),
                rs.getString("licencia"),
                rs.getString("fecha_reg")
        );
    }
}