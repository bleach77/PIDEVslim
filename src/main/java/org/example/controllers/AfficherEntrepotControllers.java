package org.example.controllers;

import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import org.example.entites.Entrepot;

import org.example.services.EntrepotService;


import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherEntrepotControllers {

    @FXML
    private Button myButton;


    @FXML
    private TextField ENTREPOT_RECH1;


    @FXML
    private ListView<Entrepot> ENTREPOT_AFF;


    private final EntrepotService entrepotService = new EntrepotService();

    @FXML
    void ENTREPOT_PDF_BOUTON(javafx.event.ActionEvent actionEvent) throws SQLException, DocumentException, FileNotFoundException {
      EntrepotService es = new EntrepotService();
      es.generatePDF();
    }


    @FXML

    void ENTREPOT_AFF_BOUTON_AJ(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterEntrepot.fxml"));
            ENTREPOT_AFF.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }



    @FXML
    void initialize()
    {
        try {
            List<Entrepot> entrepots = entrepotService.recuperer(); // Récupérer les données depuis le service
            ObservableList<Entrepot> observableEntrepots = FXCollections.observableArrayList(entrepots);

            ENTREPOT_AFF.setItems(observableEntrepots);


            // Configurer l'adaptateur de cellule personnalisé
            ENTREPOT_AFF.setCellFactory(new Callback<ListView<Entrepot>, ListCell<Entrepot>>() {
                @Override
                public ListCell<Entrepot> call(ListView<Entrepot> listView) {
                    return new ListCell<Entrepot>() {
                        @Override
                        protected void updateItem(Entrepot entrepot, boolean empty) {
                            super.updateItem(entrepot, empty);

                            if (entrepot == null || empty) {
                                setText(null);
                            } else {
                                setText("        "+
                                        entrepot.getNomE() +
                                        "                              " +
                                        entrepot.getAdresseE()+
                                        "                                  " +
                                        entrepot.getCapaciteE() +
                                        "                           " +
                                        entrepot.getStatutE().toString());
                            }
                        }
                    };
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de récupération des données de la base de données
        }


        // Ajouter un gestionnaire pour le double-clic sur un élément dans le ListView
        ENTREPOT_AFF.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                Entrepot selectedEntrepot = ENTREPOT_AFF.getSelectionModel().getSelectedItem();
                if (selectedEntrepot != null) {
                    // Afficher une boîte de dialogue de confirmation
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet élément ?");
                    alert.setContentText("Nom : " + selectedEntrepot.getNomE());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // Si l'utilisateur a cliqué sur OK, supprimer l'élément
                            try {
                                entrepotService.supprimer(selectedEntrepot.getEntrepotID());
                                // Rafraîchir la liste après la suppression
                                ENTREPOT_AFF.getItems().remove(selectedEntrepot);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Gérer l'erreur de suppression
                            }
                        }
                    });
                }
            }
        });
    }

    public void ENTREPOT_AFF_BOUTON_MO(javafx.event.ActionEvent actionEvent) {
        Entrepot selectedEntrepot = ENTREPOT_AFF.getSelectionModel().getSelectedItem();
        if (selectedEntrepot != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEntrepot.fxml"));
                Parent root = loader.load();
                ModifierEntrepotControllers controller = loader.getController();
                controller.initData(selectedEntrepot);
                ENTREPOT_AFF.getScene().setRoot(root);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void ENTREPOT_RECH_BOUTON(javafx.event.ActionEvent actionEvent) {
        try {
            String searchText = ENTREPOT_RECH1.getText().toLowerCase().trim();
            List<Entrepot> entrepots = entrepotService.recuperer(); // Récupérer toutes les données depuis le service
            ObservableList<Entrepot> observableEntrepots;

            if (searchText.isEmpty()) {
                // Si le champ de recherche est vide, afficher toutes les données
                observableEntrepots = FXCollections.observableArrayList(entrepots);
            } else {
                // Sinon, filtrer les données en fonction du texte de recherche
                List<Entrepot> filteredEntrepots = entrepots.stream()
                        .filter(entrepot ->
                                String.valueOf(entrepot.getNomE()).toLowerCase().contains(searchText) ||
                                        String.valueOf(entrepot.getAdresseE()).toLowerCase().contains(searchText) ||
                                        String.valueOf(entrepot.getCapaciteE()).toLowerCase().contains(searchText) ||
                                        String.valueOf(entrepot.getStatutE()).toLowerCase().contains(searchText))
                        .collect(Collectors.toList());
                observableEntrepots = FXCollections.observableArrayList(filteredEntrepots);
            }

            ENTREPOT_AFF.setItems(observableEntrepots);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de récupération des données de la base de données
        }
    }
}