package dto;

public class DetalleAlquiler {
    private int idDetalle;
    private Vehiculo vehiculo;
    private int cantidadDias;
    private double precioDia;
    private double subtotal;

    public DetalleAlquiler(Vehiculo vehiculo, int cantidadDias) {
        this.vehiculo = vehiculo;
        this.cantidadDias = cantidadDias;
        this.precioDia = vehiculo.getPrecio();
        calcularSubtotal();
    }

    public void calcularSubtotal() {
        this.subtotal = precioDia * cantidadDias;
    }

    @Override
    public String toString() {
        return vehiculo.getMarca() + " " + vehiculo.getModelo()
                + " | " + cantidadDias + " días"
                + " | $" + precioDia + "/día"
                + " | Subtotal: $" + String.format("%.2f", subtotal);
    }

    public int      getIdDetalle()    { return idDetalle; }
    public Vehiculo getVehiculo()     { return vehiculo; }
    public int      getCantidadDias() { return cantidadDias; }
    public double   getPrecioDia()    { return precioDia; }
    public double   getSubtotal()     { return subtotal; }

    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
}
