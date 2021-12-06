package com.example.cs213project5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cs213project5.Application.Constants;

import java.util.ArrayList;
/**
 * Current Order Activity Class for displaying/functionality of current order
 * @author Robert Reid, Anthony Romanushko
 *
 */
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
     * Return values of list changes
     */
    private Intent returnIntent = new Intent();
    /**
     * Data for List View
     */
    private ArrayAdapter<String> arrayAdapter;
    /**
     * Index of selected pizza
     */
    private int SelectedIndex = -1;

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
        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SelectedIndex = position;
            }
        });
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
        lvOrders = (ListView) findViewById(R.id.lvstoreOrders);
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
     * @param view View to remove pizza from
     */
    public void removePizza(View view){
        int nullIndex = -1;
        if(SelectedIndex != nullIndex) {
            Log.i("butts", String.valueOf(lvOrders.getSelectedItemPosition()));
            returnIntent.putExtra(Intent.EXTRA_TEXT, SelectedIndex);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }else{
            CharSequence message = getApplicationContext().getString(R.string.EmptyCart);
            Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
            //https://issuetracker.google.com/issues/36951147?pli=1
            toast.show();
        }
    }

    /**
     * Method for placing order
     * @param view View to add order to
     */
    public void placeOrder(View view){
        setResult(Activity.RESULT_FIRST_USER);
        finish();
    }

    /**
     * Method for cancelling order
     * @param view View to cancel order from
     */
    public void cancelOrder(View view){
        int resultCode = Constants.canceledOrderResultCode;
        setResult(resultCode);
        finish();
    }
}