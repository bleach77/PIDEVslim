package org.example.services;

import com.google.protobuf.Message;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.mysql.cj.Session;
import org.example.entites.Entrepot;
import org.example.entites.Stock;
import org.example.utils.MyDatabase;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class StockService implements IService<Stock> {

    private Connection connection;

    public StockService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    /*
    @Override
    public void ajouter(Stock stock) throws SQLException {
        String sql = "insert into stock ((EntrepotID, PrixUnitaireS, CodeProduitS, QuantiteS, dateRestock) " +
                "values('" + stock.getEntrepotID() + "','" + stock.getPrixUnitaireS() + "'" +
                "," + stock.getCodeProduitS() +
                "," + stock.getQuantiteS() +
                "," + "NOW()"+ ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
     */

    public void ajouter(Stock stock) throws SQLException {
        String sql = "INSERT INTO stock (CodeProduitS, QuantiteS, PrixUnitaireS, dateRestockS) VALUES (?, ?, ?, NOW())";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, stock.getCodeProduitS());
        statement.setInt(2, stock.getQuantiteS());
        statement.setFloat(3, stock.getPrixUnitaireS());
        statement.executeUpdate();
    }


    @Override
    public void modifier(Stock stock) throws SQLException {
        String sql = "update stock set CodeProduitS = ?, QuantiteS = ?, PrixUnitaireS = ? where StockID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, stock.getCodeProduitS());
        preparedStatement.setInt(2, stock.getQuantiteS());
        preparedStatement.setFloat(3, stock.getPrixUnitaireS());
        preparedStatement.setInt(4, stock.getStockID());
        preparedStatement.executeUpdate();
    }

    @Override
    public void supprimer(int StockID) throws SQLException {
        String sql = "delete from stock where StockID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, StockID);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Stock> recuperer() throws SQLException {
        String sql = "select * from stock";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Stock> stocks = new ArrayList<>();
        while (rs.next()) {
            Stock s = new Stock();
            s.setStockID (rs.getInt("StockID"));
            s.setCodeProduitS(rs.getInt("CodeProduitS"));
            s.setQuantiteS(rs.getInt("QuantiteS"));
            s.setPrixUnitaireS(rs.getFloat("PrixUnitaireS"));
            s.setdateRestockS(rs.getDate("dateRestockS"));

            stocks.add(s);
        }
        return stocks;
    }

    public void generatePDF() throws SQLException {
        StockService es = new StockService();
        List<Stock> stocks = es.recuperer(); // Fetch data from the database and populate the list

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("PDF_LISTE_DES_STOCKS.pdf"));
            document.open();

            // Ajout du titre
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.RED);
            Chunk titleChunk = new Chunk("Résumer de la liste des Stocks : ", titleFont);
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
            int numberOfLines = stocks.size();

            // Ajout du deuxième titre avec le nombre de lignes avec espace avant et après
            Font secondTitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLDITALIC, BaseColor.BLACK); // Création de la police avec gras, italique et couleur noire
            Paragraph secondTitle = new Paragraph("Le Nombre de Stock est de : " + numberOfLines, secondTitleFont); // Utilisation de la police définie
            secondTitle.setAlignment(Element.ALIGN_CENTER);
            secondTitle.setSpacingBefore(10); // Espacement avant le deuxième titre
            secondTitle.setSpacingAfter(20); // Espacement après le deuxième titre
            document.add(secondTitle);


            PdfPTable pdfTable = new PdfPTable(5); // Assuming 4 attributes in Entrepot
            addTableHeader(pdfTable);

            addRows(pdfTable, stocks);

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

        cell = new PdfPCell(new Phrase("Code Produit :", redFont)); // Use blackFont for other elements
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Centering the content
        cell.setBorder(PdfPCell.BOTTOM); // Adding underline
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Quantité :", redFont)); // Use blackFont for other elements
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Centering the content
        cell.setBorder(PdfPCell.BOTTOM); // Adding underline
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Prix Unitaire :", redFont)); // Use blackFont for other elements
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Centering the content
        cell.setBorder(PdfPCell.BOTTOM); // Adding underline
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Date du Restock :", redFont)); // Use blackFont for other elements
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Centering the content
        cell.setBorder(PdfPCell.BOTTOM); // Adding underline
        table.addCell(cell);
    }


    private void addRows(PdfPTable table, List<Stock> stocks) {
        int lineNumber = 1; // Compteur de numéro de ligne
        for (Stock stock : stocks) {

            // Créer la cellule pour le numéro de ligne avec "N°" en gras et en italique
            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC);
            Phrase phrase = new Phrase("N°" + lineNumber + " :", font);
            PdfPCell numberCellF = new PdfPCell(phrase);

            numberCellF.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            numberCellF.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrer verticalement
            table.addCell(numberCellF); // Ajouter la cellule

            // Ajouter le nom centré
            PdfPCell nomCell = new PdfPCell(new Phrase(String.valueOf(stock.getCodeProduitS())));
            nomCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            table.addCell(nomCell);

            // Ajouter l'adresse centrée
            PdfPCell adresseCell = new PdfPCell(new Phrase(String.valueOf(stock.getQuantiteS())));
            adresseCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            table.addCell(adresseCell);

            // Ajouter la capacité centrée
            PdfPCell capaciteCell = new PdfPCell(new Phrase(String.valueOf(stock.getPrixUnitaireS())));
            capaciteCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            table.addCell(capaciteCell);

            // Ajouter le statut centré
            PdfPCell statutCell = new PdfPCell(new Phrase(String.valueOf(stock.getdateRestockS())));
            statutCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer le contenu
            table.addCell(statutCell);

            lineNumber++; // Incrémenter le numéro de ligne
        }
    }



}