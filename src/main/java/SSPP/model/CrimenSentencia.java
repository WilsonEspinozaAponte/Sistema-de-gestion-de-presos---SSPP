package SSPP.model;

public class CrimenSentencia {
    private int codCrimenSent;
    private int codSentencia;
    private String nombreCrimen;
    private String descripcion;
    private double condenaCrimen;

    public CrimenSentencia() {}

    public CrimenSentencia(int codCrimenSent, int codSentencia, String nombreCrimen, String descripcion, double condenaCrimen) {
        this.codCrimenSent = codCrimenSent;
        this.codSentencia = codSentencia;
        this.nombreCrimen = nombreCrimen;
        this.descripcion = descripcion;
        this.condenaCrimen = condenaCrimen;
    }

    // Getters and Setters
    public int getCodCrimenSent() { return codCrimenSent; }
    public void setCodCrimenSent(int codCrimenSent) { this.codCrimenSent = codCrimenSent; }

    public int getCodSentencia() { return codSentencia; }
    public void setCodSentencia(int codSentencia) { this.codSentencia = codSentencia; }

    public String getNombreCrimen() { return nombreCrimen; }
    public void setNombreCrimen(String nombreCrimen) { this.nombreCrimen = nombreCrimen; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCondenaCrimen() { return condenaCrimen; }
    public void setCondenaCrimen(double condenaCrimen) { this.condenaCrimen = condenaCrimen; }
}
