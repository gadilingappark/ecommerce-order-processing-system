package com.ecommerce.observers;

import com.ecommerce.events.Event;
import com.ecommerce.events.OrderCancelledEvent;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AlertObserver implements OrderObserver{
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        if (isCriticalStatusChange(newStatus)) {
            System.out.println(String.format("[%s] ALERT: Sending alert for Order %s: Status changed to %s",
                    LocalDateTime.now().format(formatter),
                    order.getOrderId(), newStatus));
        }
    }

    @Override
    public void onEventProcessed(Event event, Order order) {
        if (event instanceof OrderCancelledEvent) {
            System.out.println(String.format("[%s] ALERT: Critical event - Order %s has been cancelled!",
                    LocalDateTime.now().format(formatter), order.getOrderId()));
        }
    }

    private boolean isCriticalStatusChange(OrderStatus status) {
        return status == OrderStatus.CANCELLED || status == OrderStatus.SHIPPED;
    }
}
