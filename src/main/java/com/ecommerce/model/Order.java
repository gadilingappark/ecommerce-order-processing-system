package com.ecommerce.model;

import com.ecommerce.events.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private double totalAmount;
    private OrderStatus status;
    private List<Event> eventHistory;
    private double paidAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Order() {
        this.items = new ArrayList<>();
        this.eventHistory = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.paidAmount = 0.0;
    }

    public Order(String orderId, String customerId, List<OrderItem> items, double totalAmount) {
        this();
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
    }

    public void addEvent(Event event) {
        this.eventHistory.add(event);
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    public void addPayment(double amount) {
        this.paidAmount += amount;
        if (this.paidAmount >= this.totalAmount) {
            this.status = OrderStatus.PAID;
        } else {
            this.status = OrderStatus.PARTIALLY_PAID;
        }
    }

// Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<Event> getEventHistory() {
        return eventHistory;
    }



    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    @Override
    public String toString() {
        return String.format("Order{orderId='%s', customerId='%s', totalAmount=%.2f, status=%s, paidAmount=%.2f}",
                orderId, customerId, totalAmount, status, paidAmount);
    }
}


