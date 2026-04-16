package com.restaurant.pattern.observer;

import com.restaurant.model.Order;

/**
 * OBSERVER PATTERN - Observer Interface (Member 3 - Chef)
 * Any class that wants to be notified of new orders implements this.
 */
public interface OrderObserver {
    void onOrderReceived(Order order);
    void onOrderStatusChanged(Order order);
}
