package com.example.cs213project5.Pizzas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * StoreOrders class contains logic for interacting with StoreOrders object and its attributes which are orders
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class StoreOrders {
    private ArrayList<Order> orders = new ArrayList<Order>();

    /**
     * Method for getting the orders the store currently has
     * @return arraylist of store orders
     */
    public ArrayList<Order> getOrders(){
        return this.orders;
    }

    /**
     * Method for adding an order to the store orders
     * @param e - Order to be added to store orders
     */
    public void addOrder(Order e) {
        orders.add(e);
    }

    /**
     * Method for removing an order from the store orders
     * @param e - Order to be removed from store orders
     */
    public void completeOrder(Order e) {
        orders.remove(e);
    }

    /**
     *
     * @param index - Index of order to be returned
     * @return - Order at Index in store orders
     */
    public Order getOrder(int index) {
        return orders.get(index);
    }

}
