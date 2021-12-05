package com.example.cs213project5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cs213project5.Application.Constants;
import com.example.cs213project5.Pizzas.Order;
import com.example.cs213project5.Pizzas.Pizza;

import java.util.ArrayList;

public class CurrentOrderActivity extends AppCompatActivity {
    /**
     * TextView for Current Order ID
     */
    private TextView txtCurrentOrderID;
    /**
     * TextView for Current Subtotal
     */
    private TextView txtCurrentSubtotal;
    /**
     * TextView for Order Total
     */
    private TextView txtCurrentSubtotal2;
    /**
     * TextView for Tax Rate
     */
    private TextView txtTaxRate;
    /**
     * TextView for Total After Tax
     */
    private TextView txtCurrentTotal;
    /**
     * List View of all Orders
     */
    private ListView lvOrders;
    /**
     * Array List representation of orders
     */
    private ArrayList<String> data = new ArrayList<String>();
    /**
     * Order selected
     */
    private String selectedPizza = "";
    /**
     * Data for List View
     */
    private ArrayAdapter<String> arrayAdapter;

    /**
     * Method to initialize Current Order View
     * @param savedInstanceState Saved Instance State
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        this.setTitle("Cart");
        Intent intent = getIntent();
        String action = intent.getAction();
        if(Intent.ACTION_SEND.equals(action)) {
             data = (ArrayList<String>) intent.getSerializableExtra("data");
        }
        load();
        populate();
    }

    /**
     * Method to load statistics for current order
     */
    private void load(){
        txtCurrentOrderID = (TextView)findViewById(R.id.txtCurrentOrderID);
        txtCurrentSubtotal = (TextView) findViewById(R.id.txtCurrentSubtotal);
        txtCurrentSubtotal2 = (TextView) findViewById(R.id.txtCurrentSubtotal2);
        txtTaxRate = (TextView)findViewById(R.id.txtTaxRate);
        txtCurrentTotal = (TextView)findViewById(R.id.txtCurrentTotal);
        lvOrders = (ListView) findViewById(R.id.lvOrders);
    }
    /**
     * Method for calculating and displaying final total
     */
    private void populate(){
        txtCurrentOrderID.setText(data.get(0));
        txtCurrentSubtotal.setText(data.get(1));
        txtCurrentSubtotal2.setText(data.get(1));
        String sales_tax = "6.625%";
        txtTaxRate.setText(sales_tax);
        txtCurrentTotal.setText(data.get(2));
        updateLvOrders();
    }
    /**
     * Method for updating which orders are displayed, hiding removed orders
     */
    private void updateLvOrders(){
        int dataStart = 3;
        ArrayList<String> newArr = new ArrayList<String>();
        for(int x = dataStart; x < data.size(); x++){
            newArr.add(data.get(x));
        }
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                newArr);
        lvOrders.setAdapter(arrayAdapter);
    }

    /**
     * Method for removing pizza from order
     */
    private void removePizza()
    {
        //updateLvOrders();
    }


}