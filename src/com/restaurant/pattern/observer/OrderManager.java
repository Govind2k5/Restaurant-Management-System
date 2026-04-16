package com.restaurant.pattern.observer;

import com.restaurant.model.Order;
import java.util.ArrayList;
import java.util.List;

/**
 * OBSERVER PATTERN - Concrete Subject
 * Manages observers and fires events when orders change.
 * Used by WaiterController to notify Chef's KDS automatically.
 */
public class OrderManager implements OrderSubject {

    private static OrderManager instance; // Also uses Singleton for global access
    private List<OrderObserver> observers;

    private OrderManager() {
        this.observers = new ArrayList<>();
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    @Override
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
        System.out.println("[OrderManager] Observer registered: " + observer.getClass().getSimpleName());
    }

    @Override
    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Order order) {
        System.out.println("[OrderManager] Notifying " + observers.size() + " observer(s) of new order.");
        for (OrderObserver observer : observers) {
            observer.onOrderReceived(order);
        }
    }

    @Override
    public void notifyStatusChange(Order order) {
        System.out.println("[OrderManager] Notifying " + observers.size() + " observer(s) of status change.");
        for (OrderObserver observer : observers) {
            observer.onOrderStatusChanged(order);
        }
    }
}
