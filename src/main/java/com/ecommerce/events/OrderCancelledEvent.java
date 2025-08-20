package com.ecommerce.events;

import java.time.LocalDateTime;

public class OrderCancelledEvent extends Event {
    private String orderId;
    private String reason;

    public OrderCancelledEvent() {

    }

    public OrderCancelledEvent(String eventId, LocalDateTime timestamp,String orderId, String reason){
        super(eventId, timestamp, "OrderCancelled");
        this.orderId = orderId;
        this.reason = reason;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
