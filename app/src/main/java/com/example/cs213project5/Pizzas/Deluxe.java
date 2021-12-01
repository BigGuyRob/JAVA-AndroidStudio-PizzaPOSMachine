package com.example.cs213project5.Pizzas;

import java.util.ArrayList;
import com.example.cs213project5.Application.Constants;
/**
 * Deluxe Pizza class contains logic from Abstract class Pizza relating
 * to interacting with Deluxe Pizza object
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class Deluxe extends Pizza{
    /**
     * Type of pizza this class produces
     */
    private String pizzaType = "Deluxe";

    /**
     * Constructor for Deluxe Pizza
     * @param SIZE - String size of pizza, small, medium, large
     * @param TOPPINGS - ArrayList toppings of pizza max length 7, handled in pizza maker class
     */
    public Deluxe(Size SIZE, ArrayList<Topping> TOPPINGS) {
        this.size = SIZE;
        this.toppings = TOPPINGS;
        this.price = price();
    }

    /**
     * Method for calculating the price of deluxe
     * @return price of deluxe as double
     */
    @Override
    public double price() {
        double price = Constants.baseDeluxePrice;
        if(size.name().equals(Constants.SMALL.toUpperCase())) {
            price += 0;
        }else if(size.name().equals(Constants.MEDIUM.toUpperCase())) {
            price += Constants.mediumPriceIncrease;
        }else if(size.name().equals(Constants.Large.toUpperCase())) {
            price += Constants.largePriceIncrease;
        }
        int numToppings = toppings.size();
        if(numToppings > Constants.deluxeIncludedToppings) {
            price += Constants.toppingPrice * (numToppings - Constants.deluxeIncludedToppings);
        }
        String o = String.format("%,.2f", price);
        price = Double.parseDouble(o);
        return price;
    }

    /**
     * Method for returning Deluxe object as string
     * @return String representation of Deluxe object
     */
    @Override
    public String toString() {
        String str = "";
        str += pizzaType + ":" + size.name() +":Toppings" + toppings.toString() + ":Price[" + String.valueOf(price) + "]";
        return str;
    }

    /**
     * Method for updating the toppings on the deluxe
     * @param TOPPINGS ArrayList of toppings on the deluxe
     */
    @Override
    public void updateToppings(ArrayList<Topping> TOPPINGS) {
        this.toppings = TOPPINGS;
        this.price = price();

    }

    /**
     * Method for updating the size of the deluxe
     * @param SIZE - Enum size for the deluxe to be updated to
     */
    @Override
    public void updateSize(Size SIZE) {
        this.size = SIZE;
        this.price = price();
    }

    /**
     * Method for getting the current toppings on the deluxe
     * @return ArrayList of toppings on the deluxe
     */
    @Override
    public ArrayList<Topping> getToppings() {
        return this.toppings;
    }

    /**
     * Method for getting the current size of the deluxe
     * @return Enum size of the deluxe
     */
    @Override
    public Size getSize() {
        return this.size;
    }

    /**
     * Method for getting the type of the deluxe
     * @return type of deluxe as string
     */
    @Override
    public String getType() {
        return "deluxe";
    }

}

