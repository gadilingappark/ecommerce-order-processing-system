package com.ecommerce;

import com.ecommerce.events.Event;
import com.ecommerce.ingestion.EventIngestionService;
import com.ecommerce.model.Order;
import com.ecommerce.observers.AlertObserver;
import com.ecommerce.observers.LoggerObserver;
import com.ecommerce.processing.EventProcessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class EcommerceApp
{
    public static void main( String[] args )
    {
        EventProcessor processor = new EventProcessor();
        EventIngestionService ingestionService = new EventIngestionService();


        processor.addObserver(new LoggerObserver());
        processor.addObserver(new AlertObserver());

        System.out.println("=== Order Processing System Started ===\n");

        createSampleEventsFile();

        try {
            // Read and process events from file
            List<Event> events = ingestionService.readEventsFromFile("sample_events.json");

            System.out.println("Processing " + events.size() + " events...\n");

            for (Event event : events) {
                processor.processEvent(event);
                System.out.println(); // Add spacing between events
            }

            // Display final state
            System.out.println("=== Final Order States ===");
            Map<String, Order> allOrders = processor.getAllOrders();
            for (Order order : allOrders.values()) {
                System.out.println(order);
                System.out.println("  Event History: " + order.getEventHistory().size() + " events");
            }

        } catch (IOException e) {
            System.err.println("ERROR reading events file: " + e.getMessage());
        }

        System.out.println("\n=== Order Processing System Completed ===");
    }
    private static void createSampleEventsFile() {
        String fileName = "sample_events.json";
        File file = new File(fileName);

        if (file.exists()) {
            return; // File already exists
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Sample events
            writer.println("{\"eventId\": \"e1\", \"timestamp\": \"2025-07-29T10:00:00\", \"eventType\": \"OrderCreated\", \"orderId\": \"ORD001\", \"customerId\": \"CUST001\", \"items\": [{\"itemId\": \"P001\", \"qty\": 2}, {\"itemId\": \"P002\", \"qty\": 1}], \"totalAmount\": 150.00}");
            writer.println("{\"eventId\": \"e2\", \"timestamp\": \"2025-07-29T10:05:00\", \"eventType\": \"PaymentReceived\", \"orderId\": \"ORD001\", \"amountPaid\": 75.00}");
            writer.println("{\"eventId\": \"e3\", \"timestamp\": \"2025-07-29T10:10:00\", \"eventType\": \"PaymentReceived\", \"orderId\": \"ORD001\", \"amountPaid\": 75.00}");
            writer.println("{\"eventId\": \"e4\", \"timestamp\": \"2025-07-29T11:00:00\", \"eventType\": \"ShippingScheduled\", \"orderId\": \"ORD001\", \"shippingDate\": \"2025-07-30T09:00:00\"}");
            writer.println("{\"eventId\": \"e5\", \"timestamp\": \"2025-07-29T10:15:00\", \"eventType\": \"OrderCreated\", \"orderId\": \"ORD002\", \"customerId\": \"CUST002\", \"items\": [{\"itemId\": \"P003\", \"qty\": 1}], \"totalAmount\": 99.99}");
            writer.println("{\"eventId\": \"e6\", \"timestamp\": \"2025-07-29T10:20:00\", \"eventType\": \"OrderCancelled\", \"orderId\": \"ORD002\", \"reason\": \"Customer requested cancellation\"}");

            System.out.println("Created sample events file: " + fileName);
        } catch (IOException e) {
            System.err.println("ERROR creating sample events file: " + e.getMessage());
        }
    }

}
