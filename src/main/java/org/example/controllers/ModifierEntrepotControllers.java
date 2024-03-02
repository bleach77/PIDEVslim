package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.entites.Entrepot;
import org.example.entites.StatuE;

import org.example.entites.Stock;
import org.example.services.EntrepotService;
import org.example.services.Sendmail;


import java.io.IOException;
import java.sql.SQLException;



public class ModifierEntrepotControllers {

    @FXML
    private Button myButton;


    private final EntrepotService se = new EntrepotService();
    @FXML
    private TextField ENTREPOT_MO_CA1;

    @FXML
    private TextField ENTREPOT_MO_NO1;

    @FXML
    private TextField ENTREPOT_MO_AD1;

    @FXML
    private ComboBox<StatuE> ENTREPOT_MO_S1; // Modifier le type du ComboBox

    @FXML
    private Label ENTREPOT_MO_REP1;

    @FXML
    private TextField ENTREPOT_MO_ID1;




    @FXML
    void ENTREPOT_MO_BOUTON_AFF(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherEntrepot2.fxml"));
            ENTREPOT_MO_NO1.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void ENTREPOT_MO_BOUTON1(javafx.event.ActionEvent actionEvent) {
        try {
            // Vérifier si un champ est vide
            if (ENTREPOT_MO_NO1.getText().isEmpty() || ENTREPOT_MO_AD1.getText().isEmpty() || ENTREPOT_MO_CA1.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Les champs ne doivent pas être vides.");
                alert.showAndWait();
                return; // Arrêter l'exécution si un champ est vide
            }

            // Vérifier si les champs requis sont remplis correctement
            if (!isString(ENTREPOT_MO_NO1.getText()) || !isString(ENTREPOT_MO_AD1.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Le champs NOM ou ADRESSE doivent être en String");
                alert.showAndWait();
                return; // Arrêter l'exécution si un champ est invalide
            }

            if (!isInteger(ENTREPOT_MO_CA1.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Attention ! Le champ CAPACITE doit être rempli correctement pour pouvoir ajouter. Veuillez ajouter une valeur INT.");
                alert.showAndWait();
                return; // Arrêter l'exécution si un champ est invalide
            }


           // StatuE ENTREPOT_MO_S1 = getStatuEFromComboBox();
            System.out.println("Start");
            // Utiliser la valeur d'énumération statuE comme nécessairesout
            se.modifier(new Entrepot(Integer.parseInt(ENTREPOT_MO_ID1.getText()),ENTREPOT_MO_NO1.getText(), ENTREPOT_MO_AD1.getText(), Integer.parseInt(ENTREPOT_MO_CA1.getText()),  ENTREPOT_MO_S1.getValue()));
            System.out.println("Stop");
            ENTREPOT_MO_REP1.setText("Hamdoula !");


            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! API MAILING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Sendmail sender = new Sendmail();
            String message = "Bonjour Madame/Monsieur,\n\n";
            message += "Nous vous informons que vous avez modifié un entrepôt au système avec succès.\n";
            message += "\n";
            message += "Les détails de l'entrepôt modifié sont :\n";
            message += "- Nom de l'entrepôt : " + ENTREPOT_MO_NO1.getText();// Remplacez [Nom de l'entrepôt] par le nom réel de l'entrepôt ajouté
            message += "\n";
            message += "- Adresse de l'entrepôt : " + ENTREPOT_MO_AD1.getText(); // Remplacez [Adresse de l'entrepôt] par l'adresse réelle de l'entrepôt ajouté
            message += "\n";
            message += "- Capacité de l'entrepôt : " + Integer.parseInt(ENTREPOT_MO_CA1.getText()); // Remplacez [Capacité de l'entrepôt] par la capacité réelle de l'entrepôt ajouté
            message += "\n";
            message += "- Statut de l'entrepôt : " + ENTREPOT_MO_S1.getValue(); // Remplacez [Statut de l'entrepôt] par le statut réel de l'entrepôt ajouté
            message += "\n\n\n";
            message += "Bien à vous,";
            message += "\n";
            message += "Cordialement.";

            sender.envoyer("slim-fady.hanafi@esprit.tn", "Confirmation de la modification d'un entrepôt", message);

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Méthode pour récupérer la valeur de l'énumération à partir du ComboBox
    private StatuE getStatuEFromComboBox() {
        String selectedValue = ENTREPOT_MO_S1.getValue().toString(); // Récupérer la valeur sélectionnée
        return StatuE.valueOf(selectedValue); // Convertir la chaîne en énumération StatuE
    }


    public void initData(Entrepot entrepot) {
        ENTREPOT_MO_ID1.setText(String.valueOf(entrepot.getEntrepotID()));
        ENTREPOT_MO_NO1.setText(String.valueOf(entrepot.getNomE()));
        ENTREPOT_MO_AD1.setText(String.valueOf(entrepot.getAdresseE()));
        ENTREPOT_MO_CA1.setText(String.valueOf(entrepot.getCapaciteE()));

        // Ajouter les valeurs de l'énumération StatuE au ComboBox
        ENTREPOT_MO_S1.getItems().addAll(StatuE.values());

        // Sélectionner la valeur correspondant à celle de l'objet Entrepot
        ENTREPOT_MO_S1.setValue(entrepot.getStatutE());
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
