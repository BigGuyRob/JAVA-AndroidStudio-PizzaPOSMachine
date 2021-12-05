package com.example.cs213project5;

import androidx.appcompat.app.AppCompatActivity;

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

public class CurrentOrderActivity extends AppCompatActivity {
    private TextView txtCurrentOrderID;
    private TextView txtCurrentSubtotal;
    private TextView txtCurrentSubtotal2;
    private TextView txtTaxRate;
    private TextView txtCurrentTotal;
    private ListView lvOrders;
    private ArrayList<String> data = new ArrayList<String>();
    private Intent returnIntent = new Intent();
    private ArrayAdapter<String> arrayAdapter;

    private int SelectedIndex = -1;


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

    private void load(){
        txtCurrentOrderID = (TextView)findViewById(R.id.txtCurrentOrderID);
        txtCurrentSubtotal = (TextView) findViewById(R.id.txtCurrentSubtotal);
        txtCurrentSubtotal2 = (TextView) findViewById(R.id.txtCurrentSubtotal2);
        txtTaxRate = (TextView)findViewById(R.id.txtTaxRate);
        txtCurrentTotal = (TextView)findViewById(R.id.txtCurrentTotal);
        lvOrders = (ListView) findViewById(R.id.lvstoreOrders);
    }

    private void populate(){
        txtCurrentOrderID.setText(data.get(0));
        txtCurrentSubtotal.setText(data.get(1));
        txtCurrentSubtotal2.setText(data.get(1));
        String sales_tax = "6.625%";
        txtTaxRate.setText(sales_tax);
        txtCurrentTotal.setText(data.get(2));
        updateLvOrders();
    }

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

    public void removePizza(View view){
        if(SelectedIndex != -1) {
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

    public void placeOrder(View view){
        setResult(Activity.RESULT_FIRST_USER);
        finish();
    }

    public void cancelOrder(View view){
        int resultCode = Constants.canceledOrderResultCode;
        setResult(resultCode);
        finish();
    }
}