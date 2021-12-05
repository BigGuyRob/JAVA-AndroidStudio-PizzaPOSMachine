package com.example.cs213project5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cs213project5.Application.Constants;
import com.example.cs213project5.Application.parsers;
import com.example.cs213project5.Pizzas.Order;
import com.example.cs213project5.Pizzas.Pizza;
import com.example.cs213project5.Pizzas.PizzaMaker;
import com.example.cs213project5.Pizzas.StoreOrders;
import com.example.cs213project5.Pizzas.Topping;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText txtOrderID;
    private RadioButton rdbDeluxe;
    private RadioButton rdbHawaiian;
    private RadioButton rdbPepperoni;
    private Button btnStoreOrders;
    private Button btnComplete;
    private Button btnNewPizza;
    private Button btnCart;
    private ListView lvStoreOrders;
    private int cartNum = 0;
    private int PCRequestCode = 1;
    private int CORequestCode = 2;
    private int SORequestCode = 3;
    private Order focus = null;
    private StoreOrders storeOrders = new StoreOrders();
    private int selectedOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Main Menu Pizza Operator");
        load();
        endisableNewOrder(false);
        lvStoreOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedOrder = position;
            }
        });
    }


    private void load(){
        txtOrderID = (EditText) findViewById(R.id.txtOrderID);
        rdbDeluxe = (RadioButton) findViewById(R.id.rdbDeluxe);
        rdbHawaiian = (RadioButton) findViewById(R.id.rdbHawaiian);
        rdbPepperoni = (RadioButton) findViewById(R.id.rdbPepperoni);
        btnStoreOrders = (Button)findViewById(R.id.btnAllOrders);
        btnComplete = (Button)findViewById(R.id.btnCompleteOrder);
        btnNewPizza = (Button)findViewById(R.id.btnNewPizza);
        btnCart = (Button)findViewById(R.id.btnCart);
        lvStoreOrders = (ListView)findViewById(R.id.lvstoreOrders);
    }

    public void GotoCustomizer(View view){
        String errorMessage = validateOrder();
        if(errorMessage.equals("")){
            Intent intent = new Intent(this, PizzaCustomizationActivity.class);
            TextView CustomizerOrderID = (TextView) findViewById(R.id.txtOrderID);
            String OrderID = CustomizerOrderID.getText().toString();
            String data = getUserData();
            intent.putExtra(Intent.EXTRA_TEXT, data);
            intent.setAction(Intent.ACTION_SEND);
            if(focus == null){
                focus = new Order(txtOrderID.getText().toString());
                cartNum = 0;
                txtOrderID.setEnabled(false);
            }
            startActivityForResult(intent, PCRequestCode);

        }else{
            CharSequence message = errorMessage;
            Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
            //https://issuetracker.google.com/issues/36951147?pli=1
            toast.show();
        }

    }

    public void GotoCart(View view){
       gotoCartHelper();
    }

    public void GotoStoreOrders(View view){
        gotoAllOrdersHelper();
    }

    public void enableNewPizza(View view){
        btnNewPizza.setEnabled(true);
    }

    private String validateOrder() {
        int orderID = 0;
        String retMes = "";
        Context context = getApplicationContext();

        if(txtOrderID.getText().toString().trim().length() == 0){
            retMes = context.getString(R.string.PleaseEnterPhoneNumber);
        }else
        if(txtOrderID.getText().toString().trim().matches("[0-9]+")) {
            if(txtOrderID.getText().toString().trim().length() != 10) {
                retMes = context.getString(R.string.InvalidPhoneNumberLength);
            }
        }else {
            retMes = context.getString(R.string.InvalidCharactersinID);
        }
        return retMes;
    }

    /**
     * Method for taking the user entries and creating pizza object for customizer
     * @return - String[] representation of pizza object to pass to customizer
     */
    public String getUserData() {
        String orderID = txtOrderID.getText().toString();
        String pass;
        if(rdbDeluxe.isChecked()) {
            pass = orderID + ",deluxe" + ",small" + ",CHICKEN" + ",SAUSAGE" + ",PEPPERS" + ",ONION" + ",BACON";
        }else if(rdbHawaiian.isChecked()) {
            pass = orderID +",hawaiian" + ",small" + ",CHICKEN" + ",PINEAPPLE";
        }else {
            pass = orderID + ",pepperoni" + ",small" + ",PEPPERONI";
        }
        return pass;
    }

    /**
     * Method for changing the status of New Order Button and Cart Button
     * @param status - status to setEnabled
     */
    public void endisableNewOrder(boolean status) {
        btnNewPizza.setEnabled(status);
        btnCart.setEnabled(status);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == PCRequestCode){
                if (resultCode == Activity.RESULT_OK) {
                    String pizza = data.getStringExtra("result");
                    focus.addPizza(makePizza(pizza));
                    cartNum +=1;
                    reFocus();
                }else{
                    reFocus();
                }
            }else if (requestCode == CORequestCode){
                if(resultCode == Activity.RESULT_OK){
                    int pizzaIndex = data.getIntExtra(Intent.EXTRA_TEXT, -1);
                    Pizza p = focus.getOrder().get(pizzaIndex);
                    focus.removePizza(p);
                    cartNum -=1;
                    reFocus();
                    gotoCartHelper();
                }else if(resultCode == Activity.RESULT_FIRST_USER){
                    storeOrders.addOrder(focus);
                    focus = null;
                    cartNum = 0;
                    txtOrderID.setText("");
                    reFocus();
                }else if(resultCode == Constants.canceledOrderResultCode){
                    focus = null;
                    cartNum = 0;
                    txtOrderID.setText("");
                    reFocus();
                }
            }else if(requestCode == SORequestCode){
                if(resultCode == Activity.RESULT_OK){
                    int orderIndex = data.getIntExtra(Intent.EXTRA_TEXT, -1);
                    Order o = storeOrders.getOrder(orderIndex);
                    storeOrders.completeOrder(o);
                    reFocus();
                    gotoAllOrdersHelper();
                }
            }

    }

    /**
     * Method for updating the list of current orders in the store orders
     */
    private void updateLVOrders() {
        ArrayList<String> newArr = new ArrayList<String>();
        for(Order o : storeOrders.getOrders()){
            newArr.add(o.getOrderID());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                newArr);
        lvStoreOrders.setAdapter(arrayAdapter);
    }

    private void gotoCartHelper(){
        Context context = getApplicationContext();
        if(focus.getOrder().size() > 0){
            Intent intent = new Intent(this, CurrentOrderActivity.class);
            //Data OrderID, subtotal, Total, Toppings ->
            ArrayList<String> data = new ArrayList<String>();
            data.add(focus.getOrderID());
            double subtotal = 0;
            for(Pizza p : focus.getOrder()){
                subtotal += p.price();
            }
            data.add(String.format("%,.2f", subtotal));
            data.add(String.format("%,.2f", focus.getPrice()));
            for(Pizza p : focus.getOrder()){
                data.add(p.toString());
            }
            intent.putExtra("data", data);
            intent.setAction(Intent.ACTION_SEND);
            startActivityForResult(intent, CORequestCode);
        }else{
            CharSequence message = context.getString(R.string.EmptyCart);
            Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
            //https://issuetracker.google.com/issues/36951147?pli=1
            toast.show();
            reFocus();
        }
    }

    private void gotoAllOrdersHelper(){
        if(storeOrders.getOrders().size() > 0){
            Intent intent = new Intent(this, StoreOrdersActivity.class);
            ArrayList<String> ordersList = new ArrayList<String>();
            for(Order o : storeOrders.getOrders()){
                ordersList.add(o.toString());
            }
            intent.putExtra(Intent.EXTRA_TEXT, ordersList);
            intent.setAction(Intent.ACTION_SEND);
            startActivityForResult(intent, SORequestCode);

        }else{
            Toast toast = Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.NoCurrentOrders), Toast.LENGTH_SHORT);
            //https://issuetracker.google.com/issues/36951147?pli=1
            toast.show();
            reFocus();
        }
    }

    private Pizza makePizza(String p){
        ArrayList<Topping> Toppings = new ArrayList<Topping>();
        String[] data = p.split(",");
        for(int x = 2; x < data.length; x++){
            Toppings.add(parsers.parseTopping(data[x]));
        }
        String size = data[1];
        String pizzaType = data[0];
        return PizzaMaker.createPizza(pizzaType, parsers.parseSize(size), Toppings);
    }

    /**
     * Method for refocusing on this stage whenever another view is closed which decides which options
     * to enable.
     */
    private void reFocus() {
        updateLVOrders();
        rdbDeluxe.setSelected(true);
        rdbDeluxe.setSelected(false);
        if(cartNum == 0) {
            txtOrderID.setEnabled(true);
            btnCart.setEnabled(false);
            btnCart.setText("Cart");
        }else {
            txtOrderID.setEnabled(false);
            btnCart.setText("Cart (" + cartNum + ")");
            btnCart.setEnabled(true);
        }
    }

    /**
     * Method for completing the selected order and removing it from the store orders
     * @param - on complete Button click
     */
    public void completeOrder(View view) {
        Context context = getApplicationContext();
        if(selectedOrder != -1) {
            storeOrders.completeOrder(storeOrders.getOrder(selectedOrder));
            updateLVOrders();
        }else {
            CharSequence message = context.getString(R.string.NoOrderSelected);
            Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
            //https://issuetracker.google.com/issues/36951147?pli=1
            toast.show();
        }



    }
}