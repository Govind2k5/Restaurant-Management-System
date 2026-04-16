package com.restaurant.pattern.observer;

import com.restaurant.model.Order;

/**
 * OBSERVER PATTERN - Subject Interface
 * The OrderManager (used by Waiter) implements this to notify Chef's KDS.
 */
public interface OrderSubject {
    void addObserver(OrderObserver observer);
    void removeObserver(OrderObserver observer);
    void notifyObservers(Order order);
    void notifyStatusChange(Order order);
}
