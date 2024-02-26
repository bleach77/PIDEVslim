package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import org.example.entites.Stock;
import org.example.services.StockService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherStockControllers {

    @FXML
    private Button myButton;



    @FXML
    private ListView<Stock> STOCK_AFF;

    @FXML
    private TextField STOCK_RECH1;

    private final StockService stockService = new StockService();

    @FXML
    void STOCK_RECH_BOUTON(ActionEvent event) {
        try {
            String searchText = STOCK_RECH1.getText().toLowerCase().trim();
            List<Stock> stocks = stockService.recuperer(); // Récupérer toutes les données depuis le service
            ObservableList<Stock> observableStocks;

            if (searchText.isEmpty()) {
                // Si le champ de recherche est vide, afficher toutes les données
                observableStocks = FXCollections.observableArrayList(stocks);
            } else {
                // Sinon, filtrer les données en fonction du texte de recherche
                List<Stock> filteredStocks = stocks.stream()
                        .filter(stock ->
                                String.valueOf(stock.getCodeProduitS()).toLowerCase().contains(searchText) ||
                                        String.valueOf(stock.getQuantiteS()).toLowerCase().contains(searchText) ||
                                        String.valueOf(stock.getPrixUnitaireS()).toLowerCase().contains(searchText) ||
                                        String.valueOf(stock.getdateRestockS()).toLowerCase().contains(searchText))
                        .collect(Collectors.toList());
                observableStocks = FXCollections.observableArrayList(filteredStocks);
            }

            STOCK_AFF.setItems(observableStocks);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de récupération des données de la base de données
        }
    }



    @FXML
    void STOCK_AFF_BOUTON_AJ(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterStock.fxml"));
            STOCK_AFF.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }



    @FXML
    void initialize()
    {
        try {
            List<Stock> stocks = stockService.recuperer(); // Récupérer les données depuis le service
            ObservableList<Stock> observableStocks = FXCollections.observableArrayList(stocks);

            STOCK_AFF.setItems(observableStocks);


            // Configurer l'adaptateur de cellule personnalisé
            STOCK_AFF.setCellFactory(new Callback<ListView<Stock>, ListCell<Stock>>() {
                @Override
                public ListCell<Stock> call(ListView<Stock> listView) {
                    return new ListCell<Stock>() {
                        @Override
                        protected void updateItem(Stock stock, boolean empty) {
                            super.updateItem(stock, empty);

                            if (stock == null || empty) {
                                setText(null);
                            } else {
                                setText("   "+
                                        stock.getCodeProduitS() +
                                        "      " +
                                                stock.getQuantiteS() +
                                                "        " +
                                                stock.getPrixUnitaireS() +
                                        "          " +
                                                stock.getdateRestockS());
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
        STOCK_AFF.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                Stock selectedStock = STOCK_AFF.getSelectionModel().getSelectedItem();
                if (selectedStock != null) {
                    // Afficher une boîte de dialogue de confirmation
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet élément ?");
                    alert.setContentText("Code Produit: " + selectedStock.getCodeProduitS());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // Si l'utilisateur a cliqué sur OK, supprimer l'élément
                            try {
                                stockService.supprimer(selectedStock.getStockID());
                                // Rafraîchir la liste après la suppression
                                STOCK_AFF.getItems().remove(selectedStock);
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

    public void STOCK_AFF_BOUTON_MO(javafx.event.ActionEvent actionEvent) {
        Stock selectedStock = STOCK_AFF.getSelectionModel().getSelectedItem();
        if (selectedStock != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierStock.fxml"));
                Parent root = loader.load();
                ModifierStockControllers controller = loader.getController();
                controller.initData(selectedStock);
                STOCK_AFF.getScene().setRoot(root);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }


}