package org.example.services;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.entites.Entrepot;
import org.example.services.EntrepotService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class Slacknotif{

    private static final String SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/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";
    public static final String PDF_FILE_PATH = "C:\\Users\\habib\\IdeaProjects\\PIDEVslim\\Entrepot.pdf";
    public static void main(String[] args) throws SQLException {
        Slacknotif slackPDFGenerator = new Slacknotif();
        slackPDFGenerator.generatePDFAndNotify();
    }

    public void generatePDFAndNotify() throws SQLException {
        // Generate the PDF
        generatePDF();

        // Send Slack notification with the PDF attachment
        sendSlackNotification("PDF generated successfully!");
    }

    public void generatePDF() throws SQLException {
        EntrepotService es = new EntrepotService();
        List<Entrepot> entrepots = es.recuperer();

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("Entrepot.pdf"));
            document.open();

            PdfPTable pdfTable = new PdfPTable(4);
            addTableHeader(pdfTable);
            addRows(pdfTable, entrepots);

            document.add(pdfTable);
            document.close();

            System.out.println("PDF generated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table) {
        table.addCell("Name");
        table.addCell("Adresse");
        table.addCell("statut");
        table.addCell("capacite");
    }

    private void addRows(PdfPTable table, List<Entrepot> entrepots) {
        for (Entrepot entrepot : entrepots) {
            table.addCell(entrepot.getNomE());
            table.addCell(entrepot.getAdresseE());
            table.addCell(entrepot.getStatutE().name());
            table.addCell(String.valueOf(entrepot.getCapaciteE()));
        }
    }

    public static void sendSlackNotification(String message) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SLACK_WEBHOOK_URL);

            // Create the payload JSON object including the message
            String payload = "{\"text\": \"" + message + "\"}";

            // Create a file entity for the PDF attachment
            File pdfFile = new File(PDF_FILE_PATH);

            FileBody fileBody = new FileBody(pdfFile);

            // Create a multipart entity for the payload and attachment
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("payload", payload, ContentType.APPLICATION_JSON); // Set content type for payload
            builder.addPart("file", fileBody);

            httpPost.setEntity(builder.build());

            // Execute the HTTP request
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Slack notification sent. Response code: " + statusCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
