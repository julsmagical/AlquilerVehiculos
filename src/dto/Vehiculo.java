package dto;

public class Vehiculo extends Producto {
    private int idVehiculo;
    private int idTipo;
    private String tipoNombre;
    private String marca;
    private String modelo;
    private int anio;
    private String placa;
    private String color;

    // para leer desde la base de datos
    public Vehiculo(int idVehiculo, int idTipo, String tipoNombre, String marca, String modelo, int anio, String placa, String color, double precioDia, int stock) {
        super(marca + " " + modelo, precioDia, stock);
        this.idVehiculo = idVehiculo;
        this.idTipo = idTipo;
        this.tipoNombre = tipoNombre;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.placa = placa;
        this.color = color;
    }

    public Vehiculo(int idTipo, String marca, String modelo, int anio, String placa, String color, double precioDia, int stock) {
        super(marca + " " + modelo, precioDia, stock);
        this.idTipo = idTipo;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.placa = placa;
        this.color = color;
    }

    @Override
    public void mostrarDetalle() {
        System.out.println("************");
        System.out.println("ID       : " + idVehiculo);
        System.out.println("Tipo     : " + tipoNombre);
        System.out.println("Marca    : " + marca);
        System.out.println("Modelo   : " + modelo);
        System.out.println("Año      : " + anio);
        System.out.println("Placa    : " + placa);
        System.out.println("Color    : " + color);
        System.out.printf ("Precio/día: $%.2f%n", getPrecio());
        System.out.println("Stock    : " + getStock());
        System.out.println("************");
    }

    public double calcularCostoAlquiler(int dias) {
        return getPrecio() * dias;
    }

    @Override
    public String toString() {
        return "[" + idVehiculo + "] " + marca + " " + modelo
                + " (" + anio + ") | Placa: " + placa
                + " | $" + getPrecio() + "/día | Stock: " + getStock();
    }

    public int getIdVehiculo() { return idVehiculo; }
    public int getIdTipo() { return idTipo; }
    public String getTipoNombre() { return tipoNombre; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnio() { return anio; }
    public String getPlaca() { return placa; }
    public String getColor() { return color; }

    public void setIdVehiculo(int idVehiculo)    { this.idVehiculo = idVehiculo; }
    public void setIdTipo(int idTipo)            { this.idTipo     = idTipo; }
    public void setTipoNombre(String tipoNombre) { this.tipoNombre = tipoNombre; }
    public void setMarca(String marca)           { this.marca      = marca; }
    public void setModelo(String modelo)         { this.modelo     = modelo; }
    public void setAnio(int anio)                { this.anio       = anio; }
    public void setPlaca(String placa)           { this.placa      = placa; }
    public void setColor(String color)           { this.color      = color; }
}
