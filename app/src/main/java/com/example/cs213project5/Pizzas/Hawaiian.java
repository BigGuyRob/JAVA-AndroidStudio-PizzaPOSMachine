package com.example.cs213project5.Pizzas;


import com.example.cs213project5.Application.Constants;

import java.util.ArrayList;
/**
 * Hawaiian Pizza class contains logic from Abstract class Pizza relating
 * to interacting with Hawaiian Pizza object
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class Hawaiian extends Pizza{
    /**
     * Type of pizza this class produces
     */
    private String pizzaType = "Hawaiian";
    /**
     * Constructor for Hawaiian pizza
     * @param SIZE - String size of pizza, small, medium, large
     * @param TOPPING - ArrayList toppings of pizza max length 7, handled in pizza maker class
     */
    public Hawaiian(Size SIZE, ArrayList<Topping> TOPPING) {
        this.size = SIZE;
        this.toppings = TOPPING;
        this.price = price();
    }

    /**
     * Method for calculating the price of hawaiian
     * @return price of hawaiian as double
     */
    @Override
    public double price() {
        double price = Constants.baseHawaiianPrice;
        if(size.name().equals(Constants.SMALL.toUpperCase())) {
            price+= 0;
        }else if(size.name().equals(Constants.MEDIUM.toUpperCase())) {
            price += Constants.mediumPriceIncrease;
        }else if(size.name().equals(Constants.Large.toUpperCase())) {
            price+= Constants.largePriceIncrease;
        }
        int numToppings = toppings.size();
        if(numToppings > Constants.hawaiianIncludedToppings) {
            price += Constants.toppingPrice * (numToppings - Constants.hawaiianIncludedToppings);
        }
        String o = String.format("%,.2f", price);
        price = Double.parseDouble(o);
        return price;
    }

    /**
     * Method for returning Hawaiian object as string
     * @return String representation of hawaiian object
     */
    @Override
    public String toString() {
        String str = "";
        str += pizzaType + ":" + size.name() +":Toppings" + toppings.toString() + ":Price[" + String.valueOf(price) + "]";
        return str;
    }

    /**
     * Method for updating the toppings on the hawaiian
     * @param TOPPINGS ArrayList of toppings on the hawaiian
     */
    @Override
    public void updateToppings(ArrayList<Topping> TOPPINGS) {
        this.toppings = TOPPINGS;
        this.price = price();

    }

    /**
     * Method for updating the size of the hawaiian
     * @param SIZE - Enum size for the hawaiian to be updated to
     */
    @Override
    public void updateSize(Size SIZE) {
        this.size = SIZE;
        this.price = price();

    }

    /**
     * Method for getting the current toppings on the hawaiian
     * @return ArrayList of toppings on the hawaiian
     */
    @Override
    public ArrayList<Topping> getToppings() {
        return this.toppings;
    }

    /**
     * Method for getting the current size of the hawaiian
     * @return Enum size of the hawaiian
     */
    @Override
    public Size getSize() {
        return this.size;
    }

    /**
     * Method for getting the type of the hawaiian
     * @return type of hawaiian as string
     */
    @Override
    public String getType() {
        return "hawiian";
    }

}

