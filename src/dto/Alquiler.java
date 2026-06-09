package dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Alquiler {
    private int idAlquiler;
    private Cliente cliente;
    private String fechaAlquiler;
    private LocalDate fechaDevolucion;
    private double total;
    private String estado;

    private List<DetalleAlquiler> detalles;

    public Alquiler(Cliente cliente, LocalDate fechaDevolucion) {
        this.cliente = cliente;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = "ACTIVO";
        this.detalles = new ArrayList<>();
        this.total = 0;
    }

    //agg vehiculo
    public void agregarDetalle(Vehiculo v, int dias) {
        DetalleAlquiler detalle = new DetalleAlquiler(v, dias);
        detalles.add(detalle);
        calcularTotal();
    }

    //sumar de subtotales
    public void calcularTotal() {
        total = 0;
        for (DetalleAlquiler d : detalles) {
            total += d.getSubtotal();
        }
    }

    //dias entre fecha actual y la de devolucion
    public int calcularDiasAlquiler() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), fechaDevolucion);
    }

    //resumen del alquiler
    public void mostrarResumen() {
        System.out.println("************");
        System.out.println("  RESUMEN DE ALQUILER");
        System.out.println("----------------------------");
        System.out.println("Cliente : " + cliente.getNombres() + " " + cliente.getApellidos());
        System.out.println("Cédula  : " + cliente.getCedula());
        System.out.println("Fecha Dev. : " + fechaDevolucion);
        System.out.println("----------------------------");
        for (DetalleAlquiler d : detalles) {
            System.out.println("  » " + d.toString());
        }
        System.out.println("----------------------------");
        System.out.printf ("  TOTAL: $%.2f%n", total);
        System.out.println("************");
    }

    @Override
    public String toString() {
        return "[" + idAlquiler + "] Cliente: " + cliente.getNombres()
                + " | Dev: " + fechaDevolucion
                + " | Total: $" + String.format("%.2f", total)
                + " | Estado: " + estado;
    }

    public int       getIdAlquiler()       { return idAlquiler; }
    public Cliente   getCliente()          { return cliente; }
    public String    getFechaAlquiler()    { return fechaAlquiler; }
    public LocalDate getFechaDevolucion()  { return fechaDevolucion; }
    public double    getTotal()            { return total; }
    public String    getEstado()           { return estado; }
    public List<DetalleAlquiler> getDetalles() { return detalles; }

    public void setIdAlquiler(int idAlquiler)         { this.idAlquiler     = idAlquiler; }
    public void setFechaAlquiler(String fechaAlquiler){ this.fechaAlquiler  = fechaAlquiler; }
    public void setEstado(String estado)              { this.estado         = estado; }
    public void setTotal(double total)                { this.total          = total; }
}
