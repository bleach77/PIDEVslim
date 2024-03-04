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
import org.example.services.Sendmail;
//import org.example.services.Slacknotif;
import org.example.services.Slacknotif;
import org.example.services.StockService;
import org.w3c.dom.Text;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

public class AjouterStockControllers {

    @FXML
    private Button myButton;



    private final StockService ss = new StockService();
    @FXML
    private TextField STOCK_AJ_PU1;

    @FXML
    private TextField STOCK_AJ_CP1;

    @FXML
    private TextField STOCK_AJ_Q1;

    @FXML
    private Label STOCK_AJ_REP1;
    @FXML
    public void STOCK_AJ_BOUTON1(javafx.event.ActionEvent actionEvent) {

            try {

                // Vérifier si un champ est vide
                if (STOCK_AJ_CP1.getText().isEmpty() || STOCK_AJ_Q1.getText().isEmpty() || STOCK_AJ_PU1.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Attention ! Les champs ne doivent pas être vides.");
                    alert.showAndWait();
                    return; // Arrêter l'exécution si un champ est vide
                }

                if (!isInteger(STOCK_AJ_CP1.getText()) || !isInteger(STOCK_AJ_Q1.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Attention ! Le champ CODE PRODUIT ou QUANTITE doivent être un INT.");
                    alert.showAndWait();
                    return; // Arrêter l'exécution si un champ est invalide
                }

                if (!isFloat(STOCK_AJ_PU1.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Attention ! Le champ PRIX UNITAIRE doit être un FLOAT.");
                    alert.showAndWait();
                    return; // Arrêter l'exécution si un champ est invalide
                }

                ss.ajouter(new Stock( Integer.parseInt(STOCK_AJ_CP1.getText()), Integer.parseInt(STOCK_AJ_Q1.getText()) , Float.parseFloat(STOCK_AJ_PU1.getText())));
                STOCK_AJ_REP1.setText("Hamdoula !");

                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! API MAILING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                Sendmail sender = new Sendmail();
                String message = "Bonjour Madame/Monsieur,\n\n";
                message += "Nous vous informons que vous avez ajouté un nouveau stock au système avec succès.\n";
                message += "\n";
                message += "Les détails du stock ajouté sont :\n";
                message += "- Code produit du stock : " + Integer.parseInt(STOCK_AJ_CP1.getText());// Remplacez [Nom de l'entrepôt] par le nom réel de l'entrepôt ajouté
                message += "\n";
                message += "- Quantité du stock : " + Integer.parseInt(STOCK_AJ_Q1.getText()); // Remplacez [Adresse de l'entrepôt] par l'adresse réelle de l'entrepôt ajouté
                message += "\n";
                message += "- Prix unitaire du stock : " + Float.parseFloat(STOCK_AJ_PU1.getText()); // Remplacez [Capacité de l'entrepôt] par la capacité réelle de l'entrepôt ajouté
                message += "\n\n\n";
                message += "Bien à vous,";
                message += "\n";
                message += "Cordialement.";

                sender.envoyer("slim-fady.hanafi@esprit.tn", "Confirmation de l'ajout d'un stock", message);

                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                Slacknotif.sendSlackNotification("Stock Ajouté !!!!!! ");


//                Slacknotif.sendSlackNotification("Stock Ajouté !!!!!! ");
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Tout les TextField doivent être remplie pour pouvoir Ajouter");
                alert.showAndWait();
            }
    }

    @FXML
    void STOCK_AJ_BOUTON_AFF(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherStock.fxml"));
            STOCK_AJ_Q1.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // Méthode pour vérifier si une chaîne peut être convertie en entier
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

