package SSPP.model;

import java.util.Date;

public class Sentencia {
    private int codSentencia;
    private int codRecluso;
    private Date fechaSentencia;
    private double condenaTotal;
    private String estado;
    private String detalles;
    private String comentarios;

    public Sentencia() {}

    public Sentencia(int codSentencia, int codRecluso, Date fechaSentencia, double condenaTotal, String estado, String detalles, String comentarios) {
        this.codSentencia = codSentencia;
        this.codRecluso = codRecluso;
        this.fechaSentencia = fechaSentencia;
        this.condenaTotal = condenaTotal;
        this.estado = estado;
        this.detalles = detalles;
        this.comentarios = comentarios;
    }

    // Getters and Setters
    public int getCodSentencia() { return codSentencia; }
    public void setCodSentencia(int codSentencia) { this.codSentencia = codSentencia; }

    public int getCodRecluso() { return codRecluso; }
    public void setCodRecluso(int codRecluso) { this.codRecluso = codRecluso; }

    public Date getFechaSentencia() { return fechaSentencia; }
    public void setFechaSentencia(Date fechaSentencia) { this.fechaSentencia = fechaSentencia; }

    public double getCondenaTotal() { return condenaTotal; }
    public void setCondenaTotal(double condenaTotal) { this.condenaTotal = condenaTotal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
}