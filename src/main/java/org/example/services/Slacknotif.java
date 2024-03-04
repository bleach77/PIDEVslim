package org.example.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.util.logging.*;


public class Slacknotif {

    private static final String SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/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";
    private static final Logger LOGGER = Logger.getLogger(Slacknotif.class.getName());


    public static void sendSlackNotification(String message) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SLACK_WEBHOOK_URL);

            // Create the payload JSON object including the message
            String payload = "{\"text\": \"" + message + "\"}";

            // Log the payload before sending the request
            LOGGER.info("Payload: " + payload);

            // Create a multipart entity for the payload and attachment
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("payload", payload, ContentType.APPLICATION_JSON);

            httpPost.setEntity(builder.build());

            // Log the HTTP request before executing it
            LOGGER.info("Sending HTTP request to Slack webhook URL: " + SLACK_WEBHOOK_URL);

            // Execute the HTTP request
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            LOGGER.info("HTTP response received. Response code: " + statusCode);
            System.out.println("Slack notification sent. Response code: " + statusCode);
        } catch (IOException e) {
            // Log any exceptions that occur during the execution
            LOGGER.log(Level.SEVERE, "Error sending Slack notification", e);
            e.printStackTrace();
        }
    }


}
