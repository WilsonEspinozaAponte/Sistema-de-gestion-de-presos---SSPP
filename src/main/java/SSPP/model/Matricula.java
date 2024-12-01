package SSPP.model;

import java.util.Date;

public class Matricula {
    private int codMatricula;
    private int codRecluso;
    private int codTaller;
    private Date fechaMatricula;

    public Matricula() {}

    public Matricula(int codMatricula, int codRecluso, int codTaller, Date fechaMatricula) {
        this.codMatricula = codMatricula;
        this.codRecluso = codRecluso;
        this.codTaller = codTaller;
        this.fechaMatricula = fechaMatricula;
    }

    // Getters and Setters
    public int getCodMatricula() { return codMatricula; }
    public void setCodMatricula(int codMatricula) { this.codMatricula = codMatricula; }

    public int getCodRecluso() { return codRecluso; }
    public void setCodRecluso(int codRecluso) { this.codRecluso = codRecluso; }

    public int getCodTaller() { return codTaller; }
    public void setCodTaller(int codTaller) { this.codTaller = codTaller; }

    public Date getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(Date fechaMatricula) { this.fechaMatricula = fechaMatricula; }
}
