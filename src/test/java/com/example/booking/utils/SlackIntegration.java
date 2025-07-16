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

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public SlackIntegration() {
        this.configManager = ConfigManager.getInstance();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    public void sendTestResultsSummary(String environment, int totalTests, int passed, int failed, int skipped) {
        String webhookUrl = configManager.getProperty("slack.webhook.url");
        if (isWebhookMissing(webhookUrl)) return;

        String message = buildTestResultsMessage(environment, totalTests, passed, failed, skipped);
        String channel = configManager.getProperty("slack.channel", "");
        sendSlackMessage(webhookUrl, buildSlackPayload(channel, message));
    }

    public void sendCustomMessage(String message) {
        String webhookUrl = configManager.getProperty("slack.webhook.url");
        if (isWebhookMissing(webhookUrl)) return;

        String channel = configManager.getProperty("slack.channel", "");
        sendSlackMessage(webhookUrl, buildSlackPayload(channel, message));
    }

    private boolean isWebhookMissing(String webhookUrl) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            logger.warn("Slack webhook URL not configured, skipping notification");
            return true;
        }
        return false;
    }

    private String buildTestResultsMessage(String environment, int totalTests, int passed, int failed, int skipped) {
        StringBuilder message = new StringBuilder();
        message.append("🚀 *Booking Automation Test Results*\n")
                .append("📍 *Environment:* ").append(environment).append("\n")
                .append("📊 *Test Summary:*\n")
                .append("   • Total Tests: ").append(totalTests).append("\n")
                .append("   • ✅ Passed: ").append(passed).append("\n")
                .append("   • ❌ Failed: ").append(failed).append("\n")
                .append("   • ⏭️ Skipped: ").append(skipped).append("\n");

        if (failed > 0) {
            message.append("\n⚠️ *Status:* Some tests failed - please review the results");
        } else {
            message.append("\n✅ *Status:* All tests passed successfully!");
        }

        return message.toString();
    }

    private String buildSlackPayload(String channel, String message) {
        return String.format(
                "{\"channel\": \"%s\", \"text\": \"%s\", \"username\": \"Booking Automation\", \"icon_emoji\": \":robot_face:\"}",
                channel == null ? "" : channel, message.replace("\"", "\\\"")
        );
    }

    private void sendSlackMessage(String webhookUrl, String payload) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .timeout(TIMEOUT)
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
        } catch (IOException | InterruptedException e) {
            logger.error("Error sending Slack message: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}
