package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.example.entites.Entrepot;
import org.example.entites.StatuE;
import org.example.services.EntrepotService;
import org.example.services.Sendmail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class AjouterEntrepotControllers {

    @FXML
    private Button myButton;



    private final EntrepotService ss = new EntrepotService();

    @FXML
    private ComboBox<StatuE> ENTREPOT_AJ_S1;

    @FXML
    private TextField ENTREPOT_AJ_N1;

    @FXML
    private TextField ENTREPOT_AJ_CA1;

    @FXML
    private TextField ENTREPOT_AJ_AD1;

    @FXML
    private Label ENTREPOT_AJ_REP1;

    @FXML
    public void initialize() {
        // Récupérer les valeurs de l'énumération StatuE
        StatuE[] statuValues = StatuE.values();

        // Ajouter les valeurs au ComboBox
        ENTREPOT_AJ_S1.getItems().addAll(Arrays.asList(statuValues));

        // Sélectionner une valeur par défaut si nécessaire
        ENTREPOT_AJ_S1.getSelectionModel().selectFirst();
    }

    @FXML
    public void ENTREPOT_AJ_BOUTON1(javafx.event.ActionEvent actionEvent) {
        try {

            // Vérifier si un champ est vide
            if (ENTREPOT_AJ_N1.getText().isEmpty() || ENTREPOT_AJ_AD1.getText().isEmpty() || ENTREPOT_AJ_CA1.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Les champs ne doivent pas être vides.");
                alert.showAndWait();
                return; // Arrêter l'exécution si un champ est vide
            }

            // Vérifier si les champs requis sont remplis correctement
            if (!isString(ENTREPOT_AJ_N1.getText()) || !isString(ENTREPOT_AJ_AD1.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Le champs NOM ou ADRESSE doivent être en String");
                alert.showAndWait();
                return; // Arrêter l'exécution si un champ est invalide
            }

            if (!isInteger(ENTREPOT_AJ_CA1.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Le champ CAPACITE doit être remplis correctement pour pouvoir ajouter. Veuillez Ajouter une valeur INT.");
                alert.showAndWait();
                return; // Arrêter l'exécution si un champ est invalide
            }


            String selectedValue = String.valueOf(ENTREPOT_AJ_S1.getValue());
            StatuE statuE = StatuE.valueOf(selectedValue);

            // Utiliser la valeur d'énumération statuE comme nécessaire
            ss.ajouter(new Entrepot(ENTREPOT_AJ_N1.getText(), ENTREPOT_AJ_AD1.getText(), Integer.parseInt(ENTREPOT_AJ_CA1.getText()), statuE));
            ENTREPOT_AJ_REP1.setText("Hamdoula !");
            Sendmail sender = new Sendmail();
            sender.envoyer("slim-fady.hanafi@esprit.tn","Confirmation","Confirmation !!!");

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Attention ! Veuillez entrer une valeur valide pour la capacité (entier) et sélectionner un statut.");
            alert.showAndWait();
        }
    }

    @FXML
    void ENTREPOT_AJ_BOUTON_AFF(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherEntrepot2.fxml"));
            ENTREPOT_AJ_CA1.getScene().setRoot(root);
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

    // Méthode pour vérifier si une chaîne ne contient que des lettres
    private boolean isString(String str) {
        return str.matches("[a-zA-Z]+");
    }
}
