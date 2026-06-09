package dto;

public abstract class Persona {
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;

    public Persona(String nombres, String apellidos, String telefono, String email) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
    }

    //getters
    public String getNombres() {
        return nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public String getTelefono()
    {
        return telefono;
    }
    public String getEmail()
    {
        return email;
    }

    //setters
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    //metodo abstracto
    public abstract void mostrarInfo();
}
