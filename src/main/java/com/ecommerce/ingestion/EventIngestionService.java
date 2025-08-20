package com.ecommerce.ingestion;

import com.ecommerce.events.*;
import com.ecommerce.model.OrderItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventIngestionService {
    private ObjectMapper objectMapper;

    public EventIngestionService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public List<Event> readEventsFromFile(String filePath) throws IOException {
        List<Event> events = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            try {
                Event event = parseEventFromJson(line);
                if (event != null) {
                    events.add(event);
                }
            } catch (Exception e) {
                System.err.println("ERROR parsing line: " + line + " - " + e.getMessage());
            }
        }

        return events;
    }

    private Event parseEventFromJson(String jsonLine) throws Exception {
        JsonNode node = objectMapper.readTree(jsonLine);

        String eventId = node.get("eventId").asText();
        String timestampStr = node.get("timestamp").asText();
        LocalDateTime timestamp = LocalDateTime.parse(timestampStr.replace("Z", ""));
        String eventType = node.get("eventType").asText();

        switch (eventType) {
            case "OrderCreated":
                return parseOrderCreatedEvent(node, eventId, timestamp);
            case "PaymentReceived":
                return parsePaymentReceivedEvent(node, eventId, timestamp);
            case "ShippingScheduled":
                return parseShippingScheduledEvent(node, eventId, timestamp);
            case "OrderCancelled":
                return parseOrderCancelledEvent(node, eventId, timestamp);
            default:
                System.out.println("WARNING: Unknown event type in JSON: " + eventType);
                return null;
        }
    }

    private OrderCreatedEvent parseOrderCreatedEvent(JsonNode node, String eventId, LocalDateTime timestamp) {
        String orderId = node.get("orderId").asText();
        String customerId = node.get("customerId").asText();
        double totalAmount = node.get("totalAmount").asDouble();

        List<OrderItem> items = new ArrayList<>();
        JsonNode itemsNode = node.get("items");
        if (itemsNode.isArray()) {
            for (JsonNode itemNode : itemsNode) {
                String itemId = itemNode.get("itemId").asText();
                int qty = itemNode.get("qty").asInt();
                items.add(new OrderItem(itemId, qty));
            }
        }

        return new OrderCreatedEvent(eventId, timestamp, orderId, customerId, items, totalAmount);
    }

    private PaymentReceivedEvent parsePaymentReceivedEvent(JsonNode node, String eventId, LocalDateTime timestamp) {
        String orderId = node.get("orderId").asText();
        double amountPaid = node.get("amountPaid").asDouble();
        return new PaymentReceivedEvent(eventId, timestamp, orderId, amountPaid);
    }

    private ShippingScheduledEvent parseShippingScheduledEvent(JsonNode node, String eventId, LocalDateTime timestamp) {
        String orderId = node.get("orderId").asText();
        String shippingDateStr = node.get("shippingDate").asText();
        LocalDateTime shippingDate = LocalDateTime.parse(shippingDateStr.replace("Z", ""));
        return new ShippingScheduledEvent(eventId, timestamp, orderId, shippingDate);
    }

    private OrderCancelledEvent parseOrderCancelledEvent(JsonNode node, String eventId, LocalDateTime timestamp) {
        String orderId = node.get("orderId").asText();
        String reason = node.get("reason").asText();
        return new OrderCancelledEvent(eventId, timestamp, orderId, reason);
    }

}
