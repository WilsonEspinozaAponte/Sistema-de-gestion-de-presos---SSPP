package SSPP.model;

import java.util.Date;

public class Taller {
    private int codTaller;
    private String nombre;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private int capacidad;

    public Taller() {}

    public Taller(int codTaller, String nombre, String descripcion, Date fechaInicio, Date fechaFin, int capacidad) {
        this.codTaller = codTaller;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.capacidad = capacidad;
    }

    // Getters and Setters
    public int getCodTaller() { return codTaller; }
    public void setCodTaller(int codTaller) { this.codTaller = codTaller; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
}
