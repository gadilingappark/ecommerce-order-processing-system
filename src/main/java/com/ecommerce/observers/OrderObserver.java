package com.ecommerce.observers;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.events.Event;

public interface OrderObserver {

   public void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
   public void onEventProcessed(Event event, Order order);


}
