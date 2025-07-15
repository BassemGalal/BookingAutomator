package com.example.booking.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Slack Integration utility for sending test reports
 */
public class SlackIntegration {
    
    private static final Logger logger = LogManager.getLogger(SlackIntegration.class);
    private final ConfigManager configManager;
    private final HttpClient httpClient;
    
    public SlackIntegration() {
        this.configManager = ConfigManager.getInstance();
        this.httpClient = HttpClient.newBuilder()
                .build();
    }
    
    /**
     * Send test results summary to Slack
     */
    public void sendTestResultsSummary(String environment, int totalTests, int passed, int failed, int skipped) {
        try {
            String webhookUrl = configManager.getProperty("slack.webhook.url");
            String channel = configManager.getProperty("slack.channel");
            
            if (webhookUrl == null || webhookUrl.isEmpty()) {
                logger.warn("Slack webhook URL not configured, skipping notification");
                return;
            }
            
            String message = buildTestResultsMessage(environment, totalTests, passed, failed, skipped);
            String payload = buildSlackPayload(channel, message);
            
            sendSlackMessage(webhookUrl, payload);
            
        } catch (Exception e) {
            logger.error("Error sending Slack notification: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Send custom message to Slack
     */
    public void sendCustomMessage(String message) {
        try {
            String webhookUrl = configManager.getProperty("slack.webhook.url");
            String channel = configManager.getProperty("slack.channel");
            
            if (webhookUrl == null || webhookUrl.isEmpty()) {
                logger.warn("Slack webhook URL not configured, skipping notification");
                return;
            }
            
            String payload = buildSlackPayload(channel, message);
            sendSlackMessage(webhookUrl, payload);
            
        } catch (Exception e) {
            logger.error("Error sending custom Slack message: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Build test results message
     */
    private String buildTestResultsMessage(String environment, int totalTests, int passed, int failed, int skipped) {
        StringBuilder message = new StringBuilder();
        
        message.append("🚀 *Booking Automation Test Results*\\n");
        message.append("📍 *Environment:* ").append(environment).append("\\n");
        message.append("📊 *Test Summary:*\\n");
        message.append("   • Total Tests: ").append(totalTests).append("\\n");
        message.append("   • ✅ Passed: ").append(passed).append("\\n");
        message.append("   • ❌ Failed: ").append(failed).append("\\n");
        message.append("   • ⏭️ Skipped: ").append(skipped).append("\\n");
        
        if (failed > 0) {
            message.append("\\n⚠️ *Status:* Some tests failed - please review the results");
        } else {
            message.append("\\n✅ *Status:* All tests passed successfully!");
        }
        
        return message.toString();
    }
    
    /**
     * Build Slack payload JSON
     */
    private String buildSlackPayload(String channel, String message) {
        return String.format(
            "{\"channel\": \"%s\", \"text\": \"%s\", \"username\": \"Booking Automation\", \"icon_emoji\": \":robot_face:\"}",
            channel, message
        );
    }
    
    /**
     * Send message to Slack webhook
     */
    private void sendSlackMessage(String webhookUrl, String payload) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            logger.info("Slack notification sent successfully");
        } else {
            logger.error("Failed to send Slack notification. Status: {}, Response: {}", 
                        response.statusCode(), response.body());
        }
    }
}