//package org.example.services;
//
//import com.lowagie.text.Document;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//public class Slacknotif {
//
//    private static final String SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/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";
//
//    public static void main(String[] args) throws IOException {
//        Slacknotif slackPDFGenerator = new Slacknotif();
//        slackPDFGenerator.generatePDFAndNotify();
//    }
//
//    public void generatePDFAndNotify() {
//        // Generate the PDF
//        generatePDF();
//
//        // Send Slack notification with the PDF attachment
//        sendSlackNotification("PDF generated successfully!");
//    }
//
//    public void generatePDF() {
//        try {
//            Document document = new Document(PageSize.A4);
//            PdfWriter.getInstance(document, new FileOutputStream("Entrepot.pdf"));
//            document.open();
//
//            PdfPTable pdfTable = new PdfPTable(4);
//            addTableHeader(pdfTable);
//            // Add some test rows
//            addRows(pdfTable);
//
//            document.add(pdfTable);
//            document.close();
//
//            System.out.println("PDF generated successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void addTableHeader(PdfPTable table) {
//        table.addCell("Name");
//        table.addCell("Adresse");
//        table.addCell("Statut");
//        table.addCell("Capacite");
//    }
//
//    private void addRows(PdfPTable table) {
//        // Add some sample data to the table
//        table.addCell("Entrepot 1");
//        table.addCell("Adresse 1");
//        table.addCell("Actif");
//        table.addCell("1000");
//        table.addCell("Entrepot 2");
//        table.addCell("Adresse 2");
//        table.addCell("Inactif");
//        table.addCell("500");
//    }
//
//    public static void sendSlackNotification(String message) {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost httpPost = new HttpPost(SLACK_WEBHOOK_URL);
//
//            // Create the payload JSON object including the message
//            String payload = "{\"text\": \"" + message + "\"}";
//
//            // Create a file entity for the PDF attachment
//            File pdfFile = new File("Entrepot.pdf");
//            FileBody fileBody = new FileBody(pdfFile, ContentType.APPLICATION_OCTET_STREAM, "Entrepot.pdf");
//
//            // Create a multipart entity for the payload and attachment
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.addTextBody("payload", payload, ContentType.APPLICATION_JSON);
//            builder.addPart("file", fileBody);
//
//            httpPost.setEntity(builder.build());
//
//            // Execute the HTTP request
//            HttpResponse response = httpClient.execute(httpPost);
//            int statusCode = response.getStatusLine().getStatusCode();
//            System.out.println("Slack notification sent. Response code: " + statusCode);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
