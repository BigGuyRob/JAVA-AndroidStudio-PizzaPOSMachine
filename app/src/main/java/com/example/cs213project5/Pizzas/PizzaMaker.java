package com.example.cs213project5.Pizzas;
import com.example.cs213project5.Application.Constants;

import java.util.ArrayList;

/**
 * PizzaMaker class contains logic for creating different instances
 * of Abstract class Pizza
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class PizzaMaker {

    /**
     * Method for constructing any type of Pizza object
     * @param flavor - Flavor of pizza
     * @param size - SIze enum of pizza
     * @param TOPPINGS - ArrayList of toppings of pizza
     * @return null if pizza flavor is invalid, returns instance of pizza otherwise
     */
    public static Pizza createPizza(String flavor, Size size, ArrayList<Topping> TOPPINGS) {
        Pizza pizza = null;
        if(flavor.equals(Constants.deluxe)) {
            pizza = new Deluxe(size, TOPPINGS);
        }else if(flavor.equals(Constants.hawaiian)) {
            pizza = new Hawaiian(size, TOPPINGS);
        }else if(flavor.equals(Constants.pepperoni)){
            pizza = new Pepperoni(size, TOPPINGS);
        }
        return pizza;
    }

}

