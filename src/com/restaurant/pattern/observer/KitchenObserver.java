package com.restaurant.pattern.observer;

import com.restaurant.model.Order;
import com.restaurant.view.chef.KitchenDisplayView;

/**
 * OBSERVER PATTERN - Concrete Observer (Member 3 - Chef)
 * When a Waiter submits an order, this observer automatically
 * updates the Chef's Kitchen Display System (KDS) in real-time.
 */
public class KitchenObserver implements OrderObserver {

    private KitchenDisplayView kitchenDisplayView;

    public KitchenObserver(KitchenDisplayView view) {
        this.kitchenDisplayView = view;
    }

    @Override
    public void onOrderReceived(Order order) {
        System.out.println("[KitchenObserver] New order received: Order #" + order.getOrderId()
                + " for Table " + order.getTableId());
        // Refresh the chef's KDS screen with the new order
        if (kitchenDisplayView != null) {
            kitchenDisplayView.refreshOrders();
        }
    }

    @Override
    public void onOrderStatusChanged(Order order) {
        System.out.println("[KitchenObserver] Order #" + order.getOrderId()
                + " status changed to: " + order.getStatus());
        if (kitchenDisplayView != null) {
            kitchenDisplayView.refreshOrders();
        }
    }
}
