package com.ecommerce.events;

import java.time.LocalDateTime;

public class PaymentReceivedEvent extends Event{
    private String orderId;
    private double amountPaid;

    public PaymentReceivedEvent() {

    }

    public PaymentReceivedEvent(String eventId, LocalDateTime timestamp,String orderId,double amountPaid){
        super(eventId, timestamp, "PaymentReceived");
        this.orderId = orderId;
        this.amountPaid = amountPaid;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
}
