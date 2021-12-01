package com.example.cs213project5.Pizzas;
import java.util.ArrayList;
import com.example.cs213project5.Application.Constants;


/**
 * Abstract Pizza class contains logic for interacting with any Pizza object
 * @author Robert Reid, Anthony Romanushko
 *
 */
public abstract class Pizza {

    protected ArrayList<Topping> toppings = new ArrayList<Topping>();
    protected Size size;
    protected double price;

    /**
     * Method for calculating the price of pizza
     * @return price of pizza as double
     */
    public abstract double price();

    /**
     * Method for returning Pizza object as string
     * @return String representation of pizza object
     */
    @Override
    public abstract String toString();

    /**
     * Method for updating the toppings on the pizza
     * @param TOPPINGS ArrayList of toppings on the pizza
     */
    public abstract void updateToppings(ArrayList<Topping> TOPPINGS);

    /**
     * Method for updating the size of the pizza
     * @param size - Enum size for the pizza to be updated to
     */
    public abstract void updateSize(Size size);

    /**
     * Method for getting the current toppings on the pizza
     * @return ArrayList of toppings on the pizza
     */
    public abstract ArrayList<Topping> getToppings();

    /**
     * Method for getting the current size of the pizza
     * @return Enum size of the pizza
     */
    public abstract Size getSize();

    /**
     * Method for getting the type of the pizza
     * @return type of pizza as string
     */
    public abstract String getType();
}
