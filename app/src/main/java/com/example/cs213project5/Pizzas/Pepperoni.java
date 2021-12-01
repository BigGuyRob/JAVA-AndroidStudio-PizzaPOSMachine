package com.example.cs213project5.Pizzas;


import com.example.cs213project5.Application.Constants;

import java.util.ArrayList;

/**
 * Pepperoni Pizza class contains logic from Abstract class Pizza relating
 * to interacting with Pepperoni Pizza object
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class Pepperoni extends Pizza{
    /**
     * Type of pizza this class produces
     */
    private String pizzaType = "Pepperoni";
    /**
     * Constructor method for Pepperoni pizza object
     * @param SIZE - String size of pizza, small, medium, large
     * @param TOPPING - - ArrayList toppings of pizza max length 7, handled in pizza maker class
     */
    public Pepperoni(Size SIZE, ArrayList<Topping> TOPPING) {
        this.size = SIZE;
        this.toppings = TOPPING;
        this.price = price();
    }

    /**
     * Method for calculating the price of pepperoni
     * @return price of pepperoni as double
     */
    @Override
    public double price() {
        double price = Constants.basePepperoniPrice;
        if(size.name().equals(Constants.SMALL.toUpperCase())) {
            price+= 0;
        }else if(size.name().equals(Constants.MEDIUM.toUpperCase())) {
            price += Constants.mediumPriceIncrease;
        }else if(size.name().equals(Constants.Large.toUpperCase())) {
            price+= Constants.largePriceIncrease;
        }
        int numToppings = toppings.size();
        if(numToppings > Constants.pepperoniIncludedToppings) {
            price += Constants.toppingPrice * (numToppings - Constants.pepperoniIncludedToppings);
        }
        String o = String.format("%,.2f", price);
        price = Double.parseDouble(o);
        return price;
    }

    /**
     * Method for returning Pepperoni object as string
     * @return String representation of Pepperoni object
     */
    @Override
    public String toString() {
        String str = "";
        str += pizzaType + ":" + size.name() +":Toppings" + toppings.toString() + ":Price[" + String.valueOf(price) + "]";
        return str;
    }

    /**
     * Method for updating the toppings on the pepperoni
     * @param TOPPINGS ArrayList of toppings on the pepperoni
     */
    @Override
    public void updateToppings(ArrayList<Topping> TOPPINGS) {
        this.toppings = TOPPINGS;
        this.price = price();

    }

    /**
     * Method for updating the size of the pepperoni
     * @param SIZE - Enum size for the pepperoni to be updated to
     */
    @Override
    public void updateSize(Size SIZE) {
        this.size = SIZE;
        this.price = price();

    }

    /**
     * Method for getting the current toppings on the pepperoni
     * @return ArrayList of toppings on the pepperoni
     */
    @Override
    public ArrayList<Topping> getToppings() {
        return this.toppings;
    }

    /**
     * Method for getting the current size of the pepperoni
     * @return Enum size of the pepperoni
     */
    @Override
    public Size getSize() {
        return this.size;
    }

    /**
     * Method for getting the type of the pepperoni
     * @return type of pepperoni as string
     */
    @Override
    public String getType() {
        return "pepperoni";
    }

}

