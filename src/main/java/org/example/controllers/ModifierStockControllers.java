package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entites.Stock;
import org.example.services.StockService;
import org.w3c.dom.Text;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
public class ModifierStockControllers {

    @FXML
    private Button myButton;


    private final StockService ss = new StockService();
    @FXML
    private TextField STOCK_MO_PU1;

    @FXML
    private Label STOCK_MO_REP1;

    @FXML
    private TextField STOCK_MO_CP1;
    @FXML
    private TextField STOCK_MO_ID1;

    @FXML
    private TextField STOCK_MO_Q1;

    @FXML
    void STOCK_MO_BOUTON_AFF(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherStock.fxml"));
            STOCK_MO_Q1.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void STOCK_MO_BOUTON1(javafx.event.ActionEvent actionEvent) {

        try {


            // Vérifier si un champ est vide
            if (STOCK_MO_CP1.getText().isEmpty() || STOCK_MO_Q1.getText().isEmpty() || STOCK_MO_PU1.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Les champs ne doivent pas être vides.");
                alert.showAndWait();
                return; // Arrêter l'exécution si un champ est vide
            }

            if (!isInteger(STOCK_MO_CP1.getText()) || !isInteger(STOCK_MO_Q1.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Le champ CODE PRODUIT ou QUANTITE doivent être un INT.");
                alert.showAndWait();
                return; // Arrêter l'exécution si un champ est invalide
            }

            if (!isFloat(STOCK_MO_PU1.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Le champ PRIX UNITAIRE doit être un FLOAT.");
                alert.showAndWait();
                return; // Arrêter l'exécution si un champ est invalide
            }
            ss.modifier(new Stock(Integer.parseInt(STOCK_MO_ID1.getText()),Integer.parseInt(STOCK_MO_CP1.getText()), Integer.parseInt(STOCK_MO_Q1.getText()) , Float.parseFloat(STOCK_MO_PU1.getText())));
            STOCK_MO_REP1.setText("Hamdoula !");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void initData(Stock stock) {
        STOCK_MO_ID1.setText(String.valueOf(stock.getStockID()));
        STOCK_MO_CP1.setText(String.valueOf(stock.getCodeProduitS()));
        STOCK_MO_Q1.setText(String.valueOf(stock.getQuantiteS()));
        STOCK_MO_PU1.setText(String.valueOf(stock.getPrixUnitaireS()));
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Méthode pour vérifier si une chaîne peut être convertie en float
    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
