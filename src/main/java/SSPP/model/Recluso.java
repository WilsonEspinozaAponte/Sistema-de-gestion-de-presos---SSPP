package SSPP.model;

import java.util.Date;

public class Recluso {
    private int codRecluso;
    private String nombre;
    private String apellido;
    private Date fechaNacimiento;
    private int codPrision;
    private int conducta;
    private String tipoDocumento;
    private String numeroDocumento;  // Modificado
    private String nacionalidad;

    public Recluso() {}

    public Recluso(int codRecluso, String nombre, String apellido, Date fechaNacimiento, int codPrision, int conducta, String tipoDocumento, String numeroDocumento, String nacionalidad) {
        this.codRecluso = codRecluso;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.codPrision = codPrision;
        this.conducta = conducta;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nacionalidad = nacionalidad;
    }

    // Getters and Setters
    public int getCodRecluso() { return codRecluso; }
    public void setCodRecluso(int codRecluso) { this.codRecluso = codRecluso; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public int getCodPrision() { return codPrision; }
    public void setCodPrision(int codPrision) { this.codPrision = codPrision; }

    public int getConducta() { return conducta; }
    public void setConducta(int conducta) { this.conducta = conducta; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
}
