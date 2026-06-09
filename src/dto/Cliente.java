package dto;

public class Cliente extends Persona {
    private int idCliente;
    private String cedula;
    private String direccion;
    private String licencia;
    private String fechaReg;

    //constructor implementado con herencia
    public Cliente(String cedula, String nombres, String apellidos, String telefono, String email, String direccion, String licencia) {
        super(nombres, apellidos, telefono, email);
        this.cedula = cedula;
        this.direccion = direccion;
        this.licencia = licencia;
    }

    //constructor con lo que tiene la base de datos
    public Cliente(int idCliente, String cedula, String nombres, String apellidos, String telefono, String email, String direccion, String licencia, String fechaReg) {
        super(nombres, apellidos, telefono, email);
        this.idCliente = idCliente;
        this.cedula = cedula;
        this.direccion = direccion;
        this.licencia = licencia;
        this.fechaReg = fechaReg;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("************");
        System.out.println("ID       : " + idCliente);
        System.out.println("Cédula   : " + cedula);
        System.out.println("Nombre   : " + getNombres() + " " + getApellidos());
        System.out.println("Teléfono : " + getTelefono());
        System.out.println("Email    : " + getEmail());
        System.out.println("Dirección: " + direccion);
        System.out.println("Licencia : " + licencia);
        System.out.println("Registro : " + fechaReg);
        System.out.println("************");
    }

    @Override
    public String toString() {
        return "[" + idCliente + "] " + getNombres() + " " + getApellidos() + " | Cédula: " + cedula + " | Licencia: " + licencia;
    }

    public int getIdCliente() { return idCliente; }
    public String getCedula() { return cedula; }
    public String getDireccion() { return direccion; }
    public String getLicencia() { return licencia; }
    public String getFechaReg() { return fechaReg; }

    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setLicencia(String licencia) { this.licencia = licencia; }
    public void setFechaReg(String fechaReg) { this.fechaReg = fechaReg; }
}
