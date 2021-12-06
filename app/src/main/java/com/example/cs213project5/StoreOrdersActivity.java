package com.example.cs213project5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cs213project5.Application.Constants;
import com.example.cs213project5.Pizzas.Order;

import java.util.ArrayList;
/**
 * Store Orders Activity class for displaying/functionality of Store Orders
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class StoreOrdersActivity extends AppCompatActivity {
    /**
     * List View to display Store Orders
     */
    private ListView lvAllStoreOrders;
    /**
     * Return information based off of changes made in this activity
     */
    private Intent returnIntent = new Intent();
    /**
     * Selected order, -1(No order) by default
     */
    private int selectedOrder = -1;
    /**
     * Array List of string representation of all orders
     */
    private ArrayList<String> allOrders = new ArrayList<String>();

    /**
     * Method to initialize Store Orders view
     * @param savedInstanceState current instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_orders);
        Intent intent = getIntent();
        allOrders = (ArrayList<String>) intent.getSerializableExtra(Intent.EXTRA_TEXT);
        load();
        lvAllStoreOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedOrder = position;
            }
        });
    }

    /**
     * Method for loading current orders
     */
    private void load(){
        lvAllStoreOrders = (ListView) findViewById(R.id.lvAllOrders);
        updateLVOrders();
    }

    /**
     * Method for updating the list of current orders in the store orders
     */
    private void updateLVOrders() {
        ArrayList<String> newArr = new ArrayList<String>();
        for(String o : allOrders){
            newArr.add(o);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                newArr);
        lvAllStoreOrders.setAdapter(arrayAdapter);
    }

    /**
     * Method to cancel an order and remove it from StoreOrders
     * @param  - on cancel order button click
     */
    public void cancelOrder(View view) {
        int nullSelect = -1;

        if(selectedOrder != nullSelect) {
            returnIntent.putExtra(Intent.EXTRA_TEXT, selectedOrder);
            setResult(Constants.cancelNone, returnIntent);
            finish();
        }
    }
}