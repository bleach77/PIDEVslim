package org.example.entites;
// import org.example.entites.StatuE;
public class Entrepot {


    private StatuE StatutE;
    private int EntrepotID, CapaciteE;
    private String NomE, AdresseE;



    public Entrepot(int EntrepotID, String NomE, String AdresseE, int CapaciteE, StatuE StatutE) {
        this.EntrepotID = EntrepotID;
        this.NomE = NomE;
        this.AdresseE = AdresseE;
        this.CapaciteE = CapaciteE;
        this.StatutE = StatutE; // Valeur par défaut du statut
    }

    public Entrepot(String NomE, String AdresseE, int CapaciteE, StatuE StatutE) {
        this.NomE = NomE;
        this.AdresseE = AdresseE;
        this.CapaciteE = CapaciteE;
        this.StatutE = StatutE; // Valeur par défaut du statut
    }

    public Entrepot() {

    }

    public StatuE getStatutE() { return StatutE; }

    public void setStatutE(StatuE StatutE) {
        this.StatutE = StatutE;
    }





    public int getEntrepotID() {
        return EntrepotID;
    }

    public void setEntrepotID(int EntrepotID) {
        this.EntrepotID = EntrepotID;
    }

    public int getCapaciteE() {
        return CapaciteE;
    }

    public void setCapaciteE(int CapaciteE) {
        this.CapaciteE = CapaciteE;
    }

    public String getNomE() {
        return NomE;
    }

    public void setNomE(String NomE) {
        this.NomE = NomE;
    }

    public String getAdresseE() {
        return AdresseE;
    }

    public void setAdresseE(String AdresseE) {
        this.AdresseE = AdresseE;
    }

    @Override
    public String toString() {
        return "Entrepot{" +
                "EntrepotID =" + EntrepotID + '\'' +
                ", NomE =" + NomE + '\'' +
                ", AdresseE =" + AdresseE + '\'' +
                ", CapaciteE ='" + CapaciteE + '\'' +
                ", StatutE ='" + StatutE  + '\'' +
                '}';
    }
}