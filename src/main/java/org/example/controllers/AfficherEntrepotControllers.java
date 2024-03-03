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
import java.util.Collections;
import java.util.Comparator;
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
    private TextField ENTREPOT_DYN_NB;

    @FXML
    public void ENTREPOT_ENT_BOUTON_STOCK(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherStock.fxml"));
            ENTREPOT_RECH1.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    @FXML
    void initialize() {
        try {
            List<Entrepot> entrepots = entrepotService.recuperer(); // Récupérer les données depuis le service
            ObservableList<Entrepot> observableEntrepots = FXCollections.observableArrayList(entrepots);

            ENTREPOT_AFF.setItems(observableEntrepots);

            // Mettre à jour le nombre d'entrepôts affichés dans ENTREPOT_DYN_NB
            ENTREPOT_DYN_NB.setText(String.valueOf(observableEntrepots.size()));

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
                                // Mettre à jour le nombre d'entrepôts affichés dans ENTREPOT_DYN_NB après la suppression
                                ENTREPOT_DYN_NB.setText(String.valueOf(ENTREPOT_AFF.getItems().size()));
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

    @FXML
    void ENTREPOT_ANALYSE_BOUTON(javafx.event.ActionEvent event) {
        try {
            List<Entrepot> entrepots = ENTREPOT_AFF.getItems();

            // Obtenir les noms en doublon
            List<String> duplicateNames = getDuplicateNames(entrepots);

            if (!duplicateNames.isEmpty()) {
                // Afficher un message d'erreur avec les noms en doublon
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Des doublons de noms d'entrepôt ont été trouvés !");
                alert.setContentText("Nombre d'erreurs : " + duplicateNames.size() +
                        "\nNoms d'entrepôt en doublon : " + duplicateNames);
                alert.showAndWait();
            } else {
                // Afficher un message de succès si aucun doublon n'est trouvé
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Aucun doublon de nom d'entrepôt trouvé !");
                alert.setContentText("Les éléments affichés dans le ListView sont valides.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les erreurs éventuelles
        }
    }

    private List<String> getDuplicateNames(List<Entrepot> entrepots) {
        // Créer une liste de noms d'entrepôt pour vérifier les doublons
        List<String> names = entrepots.stream()
                .map(Entrepot::getNomE)
                .collect(Collectors.toList());

        // Créer une liste des noms d'entrepôt en doublon
        return names.stream()
                .filter(name -> Collections.frequency(names, name) > 1)
                .distinct()
                .collect(Collectors.toList());
    }

    @FXML
    void ENTREPOT_TRIER_ASC_BOUTON(javafx.event.ActionEvent event) {
        try {
            String searchText = ENTREPOT_RECH1.getText().toLowerCase().trim();
            List<Entrepot> entrepots;

            if (searchText.isEmpty()) {
                entrepots = entrepotService.recuperer();
            } else {
                entrepots = entrepotService.recuperer().stream()
                        .filter(entrepot ->
                                String.valueOf(entrepot.getNomE()).toLowerCase().contains(searchText) ||
                                        String.valueOf(entrepot.getAdresseE()).toLowerCase().contains(searchText) ||
                                        String.valueOf(entrepot.getCapaciteE()).toLowerCase().contains(searchText) ||
                                        String.valueOf(entrepot.getStatutE()).toLowerCase().contains(searchText))
                        .toList();
            }

            ObservableList<Entrepot> observableEntrepots = FXCollections.observableArrayList(entrepots);
            observableEntrepots.sort(Comparator.comparing(Entrepot::getNomE));
            ENTREPOT_AFF.setItems(observableEntrepots);
            // Mettre à jour le nombre d'entrepôts affichés dans ENTREPOT_DYN_NB après le tri
            ENTREPOT_DYN_NB.setText(String.valueOf(observableEntrepots.size()));
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de récupération des données de la base de données
        }
    }

    @FXML
    void ENTREPOT_TRIER_DESC_BOUTON(javafx.event.ActionEvent event) {
        try {
            String searchText = ENTREPOT_RECH1.getText().toLowerCase().trim();
            List<Entrepot> entrepots;

            if (searchText.isEmpty()) {
                entrepots = entrepotService.recuperer();
            } else {
                entrepots = entrepotService.recuperer().stream()
                        .filter(entrepot ->
                                String.valueOf(entrepot.getNomE()).toLowerCase().contains(searchText) ||
                                        String.valueOf(entrepot.getAdresseE()).toLowerCase().contains(searchText) ||
                                        String.valueOf(entrepot.getCapaciteE()).toLowerCase().contains(searchText) ||
                                        String.valueOf(entrepot.getStatutE()).toLowerCase().contains(searchText))
                        .toList();
            }

            ObservableList<Entrepot> observableEntrepots = FXCollections.observableArrayList(entrepots);
            observableEntrepots.sort((e1, e2) -> e2.getNomE().compareToIgnoreCase(e1.getNomE()));
            ENTREPOT_AFF.setItems(observableEntrepots);
            // Mettre à jour le nombre d'entrepôts affichés dans ENTREPOT_DYN_NB après le tri
            ENTREPOT_DYN_NB.setText(String.valueOf(observableEntrepots.size()));
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de récupération des données de la base de données
        }
    }

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  API PDF !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @FXML
    void ENTREPOT_PDF_BOUTON(javafx.event.ActionEvent actionEvent) throws SQLException, DocumentException, FileNotFoundException {
        EntrepotService es = new EntrepotService();
        es.generatePDF();
    }
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @FXML
    void ENTREPOT_AFF_BOUTON_AJ(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterEntrepot.fxml"));
            ENTREPOT_AFF.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
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
            // Mettre à jour le nombre d'entrepôts affichés dans ENTREPOT_DYN_NB après la recherche
            ENTREPOT_DYN_NB.setText(String.valueOf(observableEntrepots.size()));
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de récupération des données de la base de données
        }
    }


}
