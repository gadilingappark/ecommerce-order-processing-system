package com.ecommerce.observers;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.events.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerObserver implements OrderObserver{
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println(String.format("[%s] LOG: Order %s status changed from %s to %s",
                LocalDateTime.now().format(formatter),
                order.getOrderId(), oldStatus, newStatus));
    }

    @Override
    public void onEventProcessed(Event event, Order order) {
        System.out.println(String.format("[%s] LOG: Processed event %s (%s) for order %s",
                LocalDateTime.now().format(formatter),
                event.getEventId(), event.getEventType(), order.getOrderId()));
    }
}
