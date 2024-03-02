package org.example.services;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.example.entites.Entrepot;
import org.example.entites.StatuE;
import org.example.utils.MyDatabase;
import com.itextpdf.text.pdf.PdfPCell;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

import java.io.FileOutputStream;

public class EntrepotService implements IService<Entrepot> {

    private Connection connection;

    public EntrepotService() {
        connection = MyDatabase.getInstance().getConnection();
    }
/*
    @Override
    public void ajouter(Entrepot entrepot) throws SQLException {
        String sql = "insert into entrepot (NomE, AdresseE, CapaciteE, StatutE) " +
                "values('" + entrepot.getNomE() + "','" + entrepot.getAdresseE() + "'" +
                "," + entrepot.getCapaciteE() +
                "," + entrepot.getStatutE() + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
*/
//    public void ajouter(Entrepot entrepot) throws SQLException {
//        String sql = "INSERT INTO entrepot (NomE, AdresseE, CapaciteE, StatutE) VALUES (?, ?, ?, ?)";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.setString(1, entrepot.getNomE());
//        statement.setString(2, entrepot.getAdresseE());
//        statement.setInt(3, entrepot.getCapaciteE());
//        statement.setString(4, entrepot.getStatutE().name());
//        statement.executeUpdate();
//    }

    public void ajouter(Entrepot entrepot) throws SQLException {
        String sql = "INSERT INTO entrepot (NomE, AdresseE, CapaciteE, StatutE) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, entrepot.getNomE());
        statement.setString(2, entrepot.getAdresseE());
        statement.setInt(3, entrepot.getCapaciteE());
        statement.setString(4, entrepot.getStatutE().name()); // Insérer le nom de l'énumération
        statement.executeUpdate();
    }

    @Override
    public void modifier(Entrepot entrepot) throws SQLException {
        System.out.println("--->"+entrepot);
        String sql = "update entrepot set NomE = ?, AdresseE = ?, CapaciteE = ?, StatutE = ? where EntrepotID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, entrepot.getNomE());
        preparedStatement.setString(2, entrepot.getAdresseE());
        preparedStatement.setInt(3, entrepot.getCapaciteE());
        preparedStatement.setString(4, entrepot.getStatutE().name());
        preparedStatement.setInt(5, entrepot.getEntrepotID());
        preparedStatement.executeUpdate();
        System.out.println("YESSSSs");
    }

    @Override
    public void supprimer(int EntrepotID) throws SQLException {
        String sql = "delete from entrepot where EntrepotID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, EntrepotID);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Entrepot> recuperer() throws SQLException {
        String sql = "select * from entrepot";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Entrepot> entrepots = new ArrayList<>();
        while (rs.next()) {
            Entrepot e = new Entrepot();
            e.setEntrepotID (rs.getInt("EntrepotID"));
            e.setNomE(rs.getString("NomE"));
            e.setAdresseE(rs.getString("AdresseE"));
            e.setCapaciteE(rs.getInt("CapaciteE"));
            e.setStatutE(StatuE.valueOf(rs.getString("StatutE")));
            entrepots.add(e);
        }
        return entrepots;
    }


    public void generatePDF() throws SQLException {
        EntrepotService es = new EntrepotService();
        List<Entrepot> entrepots = es.recuperer(); // Fetch data from the database and populate the list

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("PDF_LISTE_DES_ENTREPOTS.pdf"));
            document.open();

            // Ajout du titre
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.RED);
            Chunk titleChunk = new Chunk("Résumer de la liste des Entrepots : ", titleFont);
            titleChunk.setBackground(BaseColor.YELLOW); // Surlignage en jaune
            Paragraph title = new Paragraph(titleChunk);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Ajout de la ligne horizontale sans espace avant
            LineSeparator line = new LineSeparator();
            line.setLineColor(BaseColor.GRAY); // Couleur de la ligne
            line.setLineWidth(1); // Épaisseur de la ligne
            document.add(new Chunk(line));

            // Calcul du nombre de lignes dans la liste des entrepôts
            int numberOfLines = entrepots.size();

            // Ajout du deuxième titre avec le nombre de lignes avec espace avant et après
            Font secondTitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLDITALIC, BaseColor.BLACK); // Création de la police avec gras, italique et couleur noire
            Paragraph secondTitle = new Paragraph("Le Nombre d'Entrepot est de : " + numberOfLines, secondTitleFont); // Utilisation de la police définie
            secondTitle.setAlignment(Element.ALIGN_CENTER);
            secondTitle.setSpacingBefore(10); // Espacement avant le deuxième titre
            secondTitle.setSpacingAfter(20); // Espacement après le deuxième titre
            document.add(secondTitle);


            PdfPTable pdfTable = new PdfPTable(5); // Assuming 4 attributes in Entrepot
            addTableHeader(pdfTable);

            addRows(pdfTable, entrepots);

            document.add(pdfTable);
            document.close();

            System.out.println("PDF generated successfully !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Fetch data from the database and populate the list of Entrepot objects


    private void addTableHeader(PdfPTable table) throws DocumentException {
        Font redFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC, BaseColor.RED); // Changed to Font.BOLDITALIC and set color to red
        Font blackFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD); // Font for other elements
        PdfPCell cell;


        // Définir la largeur des colonnes
        float[] columnWidths = {2f, 3f, 4f, 3f, 4f}; // Exemple: première colonne 2 fois plus petite que la deuxième

        table.setWidthPercentage(100); // Utiliser 100% de la largeur de la page
        table.setWidths(columnWidths);

        cell = new PdfPCell(new Phrase("Numéro :", blackFont)); // Use redFont for "Numéro :"
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Centering the content
        cell.setBorder(PdfPCell.BOTTOM); // Adding underline
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nom :", redFont)); // Use blackFont for other elements
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Centering the content
        cell.setBorder(PdfPCell.BOTTOM); // Adding underline
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Adresse :", redFont)); // Use blackFont for other elements
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Centering the content
        cell.setBorder(PdfPCell.BOTTOM); // Adding underline
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Capacité :", redFont)); // Use blackFont for other elements
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Centering the content
        cell.setBorder(PdfPCell.BOTTOM); // Adding underline
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Type de Statut :", redFont)); // Use blackFont for other elements
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Centering the content
        cell.setBorder(PdfPCell.BOTTOM); // Adding underline
        table.addCell(cell);
    }


    private void addRows(PdfPTable table, List<Entrepot> entrepots) {
        int lineNumber = 1; // Compteur de numéro de ligne
        for (Entrepot entrepot : entrepots) {

            // Créer la cellule pour le numéro de ligne avec "N°" en gras et en italique
            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC);
            Phrase phrase = new Phrase("N°" + lineNumber + " :", font);
            PdfPCell numberCellF = new PdfPCell(phrase);

            numberCellF.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            numberCellF.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrer verticalement
            table.addCell(numberCellF); // Ajouter la cellule

            // Ajouter le nom centré
            PdfPCell nomCell = new PdfPCell(new Phrase(entrepot.getNomE()));
            nomCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            table.addCell(nomCell);

            // Ajouter l'adresse centrée
            PdfPCell adresseCell = new PdfPCell(new Phrase(entrepot.getAdresseE()));
            adresseCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            table.addCell(adresseCell);

            // Ajouter la capacité centrée
            PdfPCell capaciteCell = new PdfPCell(new Phrase(String.valueOf(entrepot.getCapaciteE())));
            capaciteCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            table.addCell(capaciteCell);

            // Ajouter le statut centré
            PdfPCell statutCell = new PdfPCell(new Phrase(entrepot.getStatutE().name()));
            statutCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            table.addCell(statutCell);

            lineNumber++; // Incrémenter le numéro de ligne
        }
    }













}