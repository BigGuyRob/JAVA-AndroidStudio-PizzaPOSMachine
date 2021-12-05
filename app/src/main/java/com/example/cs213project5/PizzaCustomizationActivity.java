package com.example.cs213project5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cs213project5.Application.Constants;
import com.example.cs213project5.Application.parsers;
import com.example.cs213project5.Pizzas.Pizza;
import com.example.cs213project5.Pizzas.PizzaMaker;
import com.example.cs213project5.Pizzas.Topping;

import java.util.ArrayList;
import java.util.Locale;

public class PizzaCustomizationActivity extends AppCompatActivity {
    private Spinner cbSize;
    private CheckBox cbPEP;
    private CheckBox cbEXC;
    private CheckBox cbBAC;
    private CheckBox cbJAL;
    private CheckBox cbONI;
    private CheckBox cbCHC;
    private CheckBox cbSAS;
    private CheckBox cbPPR;
    private CheckBox cbBRO;
    private CheckBox cbRIC;
    private CheckBox cbPIN;
    private String pizzaType;
    private String size;
    private TextView btnID;
    private TextView txtSubtotal;
    private ImageView pizzaView;
    private Drawable PEPPERONI;
    private Drawable EXTRACHEESE;
    private Drawable BACON;
    private Drawable JALAPENO;
    private Drawable ONION;
    private Drawable CHICKEN;
    private Drawable SAUSAGE;
    private Drawable PEPPERS;
    private Drawable BROCCOLI;
    private Drawable RICOTTA;
    private Drawable PINEAPPLE;
    private TextView txtPizzaType;

    private ArrayList<Drawable> layers = new ArrayList<Drawable>();
    private int amtToppings = 0;
    private String[]data;
    private String orderID;
    private Toast toast;
    private Pizza valuePizza;
    private float smallScale = (float).6;
    private float medScale = (float).8;
    private float largeScale = (float) 1;
    private String[] arraySizes = {"small", "medium", "large"};
    private Intent returnIntent = new Intent();
    /**
     * toppings selected
     */
    private ArrayList<Topping> toppings = new ArrayList<Topping>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_customization);
        Intent intent = getIntent();
        String action = intent.getAction();
        if(Intent.ACTION_SEND.equals(action)) {
            data = intent.getStringExtra(Intent.EXTRA_TEXT).split(",");
            orderID = data[0];
            size = data[2];
            pizzaType = data[1];
        }
        this.setTitle("Pizza Customizer");
        load();
        cbSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = (String) parent.getItemAtPosition(pos);
                size = item;
                setImageSize();
                displaySubtotal();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        layers.add(getResources().getDrawable(R.drawable.pizza));
        txtPizzaType.setText(" " + pizzaType);
        populate();
    }

    private void load(){
        cbSize = (Spinner) findViewById(R.id.cbSize);
        cbPEP = (CheckBox) findViewById(R.id.cbPEP);
        cbEXC = (CheckBox) findViewById(R.id.cbEXC);
        cbBAC = (CheckBox) findViewById(R.id.cbBAC);
        cbJAL = (CheckBox) findViewById(R.id.cbJAL);
        cbONI = (CheckBox) findViewById(R.id.cbONI);
        cbCHC = (CheckBox) findViewById(R.id.cbCHC);
        cbSAS = (CheckBox) findViewById(R.id.cbSAS);
        cbPPR = (CheckBox) findViewById(R.id.cbPPR);
        cbBRO = (CheckBox) findViewById(R.id.cbBRO);
        cbRIC = (CheckBox) findViewById(R.id.cbRIC);
        cbPIN = (CheckBox) findViewById(R.id.cbPIN);
        btnID = (TextView) findViewById(R.id.txtID);
        txtPizzaType = (TextView) findViewById(R.id.txtPizzaType);
        pizzaView = (ImageView) findViewById(R.id.pizzaView);
        //toast = new Toast(getApplicationContext());
        txtSubtotal = (TextView) findViewById(R.id.txtSubtotal);
        PEPPERONI = getResources().getDrawable(R.drawable.pepperoni);
        EXTRACHEESE = getResources().getDrawable(R.drawable.extracheese);
        BACON = getResources().getDrawable(R.drawable.bacon);
        JALAPENO = getResources().getDrawable(R.drawable.jalapeno);
        ONION = getResources().getDrawable(R.drawable.onion);
        CHICKEN = getResources().getDrawable(R.drawable.chicken);
        SAUSAGE = getResources().getDrawable(R.drawable.sausage);
        PEPPERS = getResources().getDrawable(R.drawable.pepper);
        BROCCOLI = getResources().getDrawable(R.drawable.brocolli);
        RICOTTA = getResources().getDrawable(R.drawable.ricotta);
        PINEAPPLE = getResources().getDrawable(R.drawable.pineapple);
    }

    private void populate(){
        ArrayAdapter<String> sizeArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, arraySizes);
        cbSize.setAdapter(sizeArrayAdapter);
        cbSize.setSelection(sizeArrayAdapter.getPosition(size));
        btnID.setText(orderID);
        if(pizzaType.equals(Constants.deluxe)) {
            cbCHC.setChecked(true);
            cbSAS.setChecked(true);
            cbPPR.setChecked(true);
            cbONI.setChecked(true);
            cbBAC.setChecked(true);
            modAddLayer(Topping.CHICKEN);
            modAddLayer(Topping.SAUSAGE);
            modAddLayer(Topping.PEPPERS);
            modAddLayer(Topping.ONION);
            modAddLayer(Topping.BACON);
            loadToppings();
            amtToppings = Constants.deluxeIncludedToppings;
        }else if(pizzaType.equals(Constants.hawaiian)) {
            cbCHC.setChecked(true);
            cbPIN.setChecked(true);
            modAddLayer(Topping.CHICKEN);
            modAddLayer(Topping.PINEAPPLE);
            loadToppings();
            amtToppings = Constants.hawaiianIncludedToppings;
        }else {
            cbPEP.setChecked(true);
            modAddLayer(Topping.PEPPERONI);
            loadToppings();
            amtToppings = Constants.pepperoniIncludedToppings;
        }
        displaySubtotal();
    }

    /**
     * Method for updating the image of the pizza shown
     * @param topping - topping that was checked
     */
    private void modAddLayer(Topping topping)
    {
        switch(topping)
        {
            case PEPPERONI : layers.add(PEPPERONI); break;
            case EXTRACHEESE : layers.add(EXTRACHEESE); break;
            case BACON : layers.add(BACON); break;
            case JALAPENO : layers.add(JALAPENO); break;
            case ONION : layers.add(ONION); break;
            case CHICKEN : layers.add(CHICKEN); break;
            case SAUSAGE : layers.add(SAUSAGE); break;
            case PEPPERS : layers.add(PEPPERS); break;
            case BROCCOLI : layers.add(BROCCOLI); break;
            case RICOTTA : layers.add(RICOTTA); break;
            case PINEAPPLE : layers.add(PINEAPPLE); break;
        }
        Drawable[] layersArr = new Drawable[layers.size()];
        for(int x = 0; x < layers.size(); x++){
            layersArr[x] = layers.get(x);
        }
        LayerDrawable pizzaDrawable = new LayerDrawable(layersArr);
        pizzaView.setImageDrawable(pizzaDrawable);
    }

    /**
     * Method for updating the image of the pizza shown by removing the stacked images
     * @param topping - topping that was checked
     */
    private void modRemoveLayer(Topping topping)
    {
        switch(topping)
        {
            case PEPPERONI : layers.remove(PEPPERONI); break;
            case EXTRACHEESE : layers.remove(EXTRACHEESE); break;
            case BACON : layers.remove(BACON); break;
            case JALAPENO : layers.remove(JALAPENO); break;
            case ONION : layers.remove(ONION); break;
            case CHICKEN : layers.remove(CHICKEN); break;
            case SAUSAGE : layers.remove(SAUSAGE); break;
            case PEPPERS : layers.remove(PEPPERS); break;
            case BROCCOLI : layers.remove(BROCCOLI); break;
            case RICOTTA : layers.remove(RICOTTA); break;
            case PINEAPPLE : layers.remove(PINEAPPLE); break;
        }
        Drawable[] layersArr = new Drawable[layers.size()];
        for(int x = 0; x < layers.size(); x++){
            layersArr[x] = layers.get(x);
        }
        LayerDrawable pizzaDrawable = new LayerDrawable(layersArr);
        pizzaView.setImageDrawable(pizzaDrawable);
    }

    /**
     * Method for updating the image size
     */
    public void setImageSize()
    {
        float newScale = 0;
        switch(cbSize.getSelectedItem().toString())
        {
            case "small" : newScale = smallScale; break;
            case "medium" : newScale = medScale; break;
            case "large" : newScale = largeScale; break;
        }
        pizzaView.setScaleX(newScale);
        pizzaView.setScaleY(newScale);
    }


    /**
     * Method for loading the preset topping array
     */
    public void loadToppings() {
        int dataStart = 3;
        for(int x = dataStart; x < data.length; x++) {
            toppings.add(parsers.parseTopping(data[x].toUpperCase()));
        }
    }

    /**
     * Method for calculating and displaying subtotal
     */
    public void displaySubtotal() {
        valuePizza = PizzaMaker.createPizza(pizzaType, parsers.parseSize(size.toUpperCase()), toppings);
        String o = String.format("%,.2f", valuePizza.price());
        txtSubtotal.setText("$" + o);
    }


    /**
     * Method for updating the topping array for the current pizza and changing image
     * @param view - On topping checkbox check changed
     */
    public void updateToppings(View view) {
        CheckBox source = (CheckBox) view;
        String boxName = source.getText().toString().toUpperCase();
        if(!source.isChecked()) {
            if(amtToppings > 0) {
                amtToppings -=1;
                toppings.remove(parsers.parseTopping(boxName));
                modRemoveLayer(parsers.parseTopping(source.getText().toString().toUpperCase()));
            }
        }else {
            if(amtToppings >= Constants.MAX_TOPPING) {
                source.setChecked(false);
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, context.getString(R.string.MaxToppings), Toast.LENGTH_SHORT);
                toast.show();
            }else {
                toppings.add(parsers.parseTopping(boxName));
                modAddLayer(parsers.parseTopping(source.getText().toString().toUpperCase()));
                amtToppings +=1;
            }
        }
        displaySubtotal();
    }

    public void addToOrder(View view){
        String pizza = "";
        pizza+= pizzaType;
        pizza += "," + size ;
        for(int x = 0; x < toppings.size(); x++){
            pizza += "," + toppings.get(x).toString();
        }
        returnIntent.putExtra("result",pizza);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}