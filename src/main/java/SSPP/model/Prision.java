package SSPP.model;

public class Prision {
    private int codPrision;
    private String nombre;
    private String direccion;
    private int capacidad;

    public Prision(){}
    
    public Prision(int codPrision, String nombre, String direccion, int capacidad) {
        this.codPrision = codPrision;
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidad = capacidad;
    }

    public int getCodPrision() {
        return codPrision;
    }

    public void setCodPrision(int codPrision) {
        this.codPrision = codPrision;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
}