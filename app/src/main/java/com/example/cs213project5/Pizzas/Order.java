package com.example.cs213project5.Pizzas;
import com.example.cs213project5.Application.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Order class contains logic for interacting with order object and its attributes
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class Order implements Serializable{
    /**
     * Order number used to ID order
     */
    private String phoneNumber;
    /**
     * List of all orders made
     */
    private ArrayList<Pizza> order = new ArrayList<Pizza>();
    /**
     * Total price of the order
     */
    private double totalPrice;

    /**
     * Constructor for order class requires phone number to start the order
     * @param PHONENUMBER - String phone number for to identify order
     */
    public Order(String PHONENUMBER) {
        this.phoneNumber = PHONENUMBER;
        this.totalPrice = 0;
    }

    /**
     * Method for calculating the price based on the pizzas in the order
     * @return double total price of the order including sales tax
     */
    private double calculatePrice() {
        double price = 0;
        for(Pizza p : order) {
            price += p.price();
        }
        price += price * Constants.SALES_TAX;
        String o = String.format("%,.2f", price);
        price = Double.parseDouble(o);
        return price;
    }

    /**
     * Method for adding a Pizza object to the order
     * @param pizza - Pizza to be added to the order
     */
    public void addPizza(Pizza pizza) {
        order.add(pizza);
    }

    /**
     * Method for removing Pizza from the order, Pizza is assumed to be in the order
     * @param pizza - Pizza to be removed from the Order
     */
    public void removePizza(Pizza pizza) {
        order.remove(pizza);
    }

    /**
     * Helper method for returning total price of the order
     * @return double value returned from calculatePrice() method
     */
    public double getPrice() {
        return calculatePrice();
    }

    /**
     * Method for returning Order object to string, including all pizza to string
     * @return String representation of order
     */
    @Override
    public String toString() {
        String str = phoneNumber + ":\n";
        for(Pizza p : order) {
            str += p.toString() + ":\n";
        }
        str += "Price: " +this.getPrice();
        return str;
    }

    /**
     * Method for getting the amount of pizzas in the order
     * @return The amount of pizzas in the order
     */
    public int getSize() {
        return order.size();
    }

    /**
     * Method for getting the OrderID of the order
     * @return OrderID as string
     */
    public String getOrderID() {
        return this.phoneNumber;
    }

    /**
     * Method for returning the arrayList of pizza objects for the order
     * @return ArrayList of the pizzas in the order
     */
    public ArrayList<Pizza> getOrder() {
        return this.order;
    }
}

