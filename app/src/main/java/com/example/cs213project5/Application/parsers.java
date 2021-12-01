package com.example.cs213project5.Application;


import com.example.cs213project5.Pizzas.Size;
import com.example.cs213project5.Pizzas.Topping;

/**
 * parsers class contains logic for converting string into enum
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class parsers {

    /**
     * Method for converting string topping to Enum topping
     * @param topping - String topping
     * @return - return Enum Topping
     */
    public static Topping parseTopping(String topping) {
        Topping t;
        t = Topping.valueOf(topping.toUpperCase());
        return t;
    }

    /**
     * Method for converting string size to enum size
     * @param size - string size
     * @return - renum size
     */
    public static Size parseSize(String size) {
        Size s;
        s = Size.valueOf(size.toUpperCase());
        return s;
    }

}
