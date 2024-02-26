package org.example.entites;

import java.sql.Date;

public class Stock {
    private int StockID, CodeProduitS, QuantiteS;
    private float PrixUnitaireS;
    private Date dateRestockS;

    public Stock(int StockID, int CodeProduitS, int QuantiteS, float PrixUnitaireS, Date dateRestockS) {
        this.StockID = StockID;
        this.CodeProduitS = CodeProduitS;
        this.QuantiteS = QuantiteS;
        this.PrixUnitaireS = PrixUnitaireS;
        this.dateRestockS = dateRestockS;
    }

    public Stock(int stockID, int codeProduitS, int quantiteS, float prixUnitaireS) {
        StockID = stockID;
        CodeProduitS = codeProduitS;
        QuantiteS = quantiteS;
        PrixUnitaireS = prixUnitaireS;
    }

    //    public Stock(int stockID, int quantiteS, float prixUnitaireS, Date dateRestockS) {
//        StockID = stockID;
//        QuantiteS = quantiteS;
//        PrixUnitaireS = prixUnitaireS;
//        this.dateRestockS = dateRestockS;
//    }

    //    public Stock(int CodeProduitS, int QuantiteS, float PrixUnitaireS, Date dateRestockS) {
//        this.CodeProduitS = CodeProduitS;
//        this.QuantiteS = QuantiteS;
//        this.PrixUnitaireS = PrixUnitaireS;
//        this.dateRestockS = dateRestockS;
//    }
//
    public Stock(int CodeProduitS, int QuantiteS, float PrixUnitaireS) {
        this.CodeProduitS = CodeProduitS;
        this.QuantiteS = QuantiteS;
        this.PrixUnitaireS = PrixUnitaireS;

    }


    public Stock() {

    }




    public int getStockID() {
        return StockID;
    }

    public void setStockID(int StockID) {
        this.StockID = StockID;
    }

    public int getCodeProduitS() {
        return CodeProduitS;
    }

    public void setCodeProduitS(int CodeProduitS) {
        this.CodeProduitS = CodeProduitS;
    }

    public int getQuantiteS() {
        return QuantiteS;
    }

    public void setQuantiteS(int QuantiteS) {
        this.QuantiteS = QuantiteS;
    }

    public float getPrixUnitaireS() {
        return PrixUnitaireS;
    }

    public void setPrixUnitaireS(float PrixUnitaireS) {
        this.PrixUnitaireS = PrixUnitaireS;
    }
    public Date getdateRestockS() {
        return dateRestockS;
    }

    public void setdateRestockS(Date dateRestock) {
        this.dateRestockS = dateRestock;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "StockID =" + StockID  + '\'' +
                ", CodeProduitS ='" + CodeProduitS + '\'' +
                ", QuantiteS =" + QuantiteS +
                ", PrixUnitaireS ='" + PrixUnitaireS + '\'' +
                ", dateRestockS ='" + dateRestockS + '\'' +
                '}';
    }


}