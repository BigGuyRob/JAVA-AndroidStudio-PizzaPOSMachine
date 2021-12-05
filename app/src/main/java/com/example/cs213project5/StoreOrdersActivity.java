package com.example.cs213project5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cs213project5.Pizzas.Order;

import java.util.ArrayList;

public class StoreOrdersActivity extends AppCompatActivity {
    private ListView lvAllStoreOrders;
    private Intent returnIntent = new Intent();
    private int selectedOrder = -1;
    private ArrayList<String> allOrders = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_orders);
        Intent intent = getIntent();
        allOrders = (ArrayList<String>) intent.getSerializableExtra(Intent.EXTRA_TEXT);
        load();
        this.setTitle("All Store orders");
        lvAllStoreOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedOrder = position;
            }
        });
    }

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
        if(selectedOrder != -1) {
            returnIntent.putExtra(Intent.EXTRA_TEXT, selectedOrder);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}