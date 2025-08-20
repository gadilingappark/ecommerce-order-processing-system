package com.ecommerce.processing;

import com.ecommerce.events.*;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.observers.OrderObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Event-driven order processing system implementation
 * Demonstrates observer pattern and domain modeling - hatchling architecture
 */

public class EventProcessor {
    private Map<String, Order> orders;
    private List<OrderObserver> observers;

    public EventProcessor() {
        this.orders = new ConcurrentHashMap<>();
        this.observers = new ArrayList<>();
    }

    public void addObserver(OrderObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        this.observers.remove(observer);
    }

    private void notifyStatusChange(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        for (OrderObserver observer : observers) {
            observer.onOrderStatusChanged(order, oldStatus, newStatus);
        }
    }

    private void notifyEventProcessed(Event event, Order order) {
        for (OrderObserver observer : observers) {
            observer.onEventProcessed(event, order);
        }
    }

    public void processEvent(Event event) {
        try {
            switch (event.getEventType()) {
                case "OrderCreated":
                    processOrderCreated((OrderCreatedEvent) event);
                    break;
                case "PaymentReceived":
                    processPaymentReceived((PaymentReceivedEvent) event);
                    break;
                case "ShippingScheduled":
                    processShippingScheduled((ShippingScheduledEvent) event);
                    break;
                case "OrderCancelled":
                    processOrderCancelled((OrderCancelledEvent) event);
                    break;
                default:
                    System.out.println("WARNING: Unknown event type: " + event.getEventType());
                    return;
            }
        } catch (Exception e) {
            System.err.println("ERROR processing event " + event.getEventId() + ": " + e.getMessage());
        }
    }


    private void processOrderCreated(OrderCreatedEvent event) {
        Order order = new Order(event.getOrderId(), event.getCustomerId(),
                event.getItems(), event.getTotalAmount());
        order.addEvent(event);
        orders.put(event.getOrderId(), order);

        notifyEventProcessed(event, order);
        System.out.println("Created new order: " + order);
    }

    private void processPaymentReceived(PaymentReceivedEvent event) {
        Order order = orders.get(event.getOrderId());
        if (order == null) {
            System.err.println("ERROR: Order not found for payment: " + event.getOrderId());
            return;
        }

        OrderStatus oldStatus = order.getStatus();
        order.addEvent(event);
        order.addPayment(event.getAmountPaid());

        if (oldStatus != order.getStatus()) {
            notifyStatusChange(order, oldStatus, order.getStatus());
        }
        notifyEventProcessed(event, order);

        System.out.println("Processed payment for order: " + order);
    }

    private void processShippingScheduled(ShippingScheduledEvent event) {
        Order order = orders.get(event.getOrderId());
        if (order == null) {
            System.err.println("ERROR: Order not found for shipping: " + event.getOrderId());
            return;
        }

        OrderStatus oldStatus = order.getStatus();
        order.addEvent(event);
        order.updateStatus(OrderStatus.SHIPPED);

        notifyStatusChange(order, oldStatus, order.getStatus());
        notifyEventProcessed(event, order);

        System.out.println("Scheduled shipping for order: " + order);
    }

    private void processOrderCancelled(OrderCancelledEvent event) {
        Order order = orders.get(event.getOrderId());
        if (order == null) {
            System.err.println("ERROR: Order not found for cancellation: " + event.getOrderId());
            return;
        }

        OrderStatus oldStatus = order.getStatus();
        order.addEvent(event);
        order.updateStatus(OrderStatus.CANCELLED);

        notifyStatusChange(order, oldStatus, order.getStatus());
        notifyEventProcessed(event, order);

        System.out.println("Cancelled order: " + order + " Reason: " + event.getReason());
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public Map<String, Order> getAllOrders() {
        return new HashMap<>(orders);
    }
}
