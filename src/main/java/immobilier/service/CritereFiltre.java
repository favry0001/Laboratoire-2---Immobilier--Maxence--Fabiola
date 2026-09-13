package immobilier.service;

import immobilier.model.TypeTransaction;

public class CritereFiltre {

    private TypeTransaction transaction;
    private String typeBien;
    private Double prixMax;
    private Integer chambresMin;
    private String ville;

    public TypeTransaction getTransaction() { return transaction; }
    public void setTransaction(TypeTransaction t) { this.transaction = t; }

    public String getTypeBien() { return typeBien; }
    public void setTypeBien(String typeBien) { this.typeBien = typeBien; }

    public Double getPrixMax() { return prixMax; }
    public void setPrixMax(Double prixMax) { this.prixMax = prixMax; }

    public Integer getChambresMin() { return chambresMin; }
    public void setChambresMin(Integer chambresMin) { this.chambresMin = chambresMin; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
}