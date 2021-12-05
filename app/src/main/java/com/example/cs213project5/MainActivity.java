package com.example.cs213project5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cs213project5.Application.parsers;
import com.example.cs213project5.Pizzas.Order;
import com.example.cs213project5.Pizzas.Pizza;
import com.example.cs213project5.Pizzas.PizzaMaker;
import com.example.cs213project5.Pizzas.Topping;
import com.google.android.material.textfield.TextInputEditText;

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
    private int cartNum = 0;
    private int PCRequestCode = 1;
    private int CORequestCode = 2;

    private Order focus = null;

    /**
     * Method for initializing initial conditions
     * @param savedInstanceState the saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Main Menu Pizza Operator");
        load();
        endisableNewOrder(false);
    }

    /**
     * Method for loading assets onto screen
     */
    private void load(){
        txtOrderID = (EditText) findViewById(R.id.txtOrderID);
        rdbDeluxe = (RadioButton) findViewById(R.id.rdbDeluxe);
        rdbHawaiian = (RadioButton) findViewById(R.id.rdbHawaiian);
        rdbPepperoni = (RadioButton) findViewById(R.id.rdbPepperoni);
        btnStoreOrders = (Button)findViewById(R.id.btnAllOrders);
        btnComplete = (Button)findViewById(R.id.btnCompleteOrder);
        btnNewPizza = (Button)findViewById(R.id.btnNewPizza);
        btnCart = (Button)findViewById(R.id.btnCart);
    }

    /**
     * Method that allows the user to add an order
     * @param view current view
     */
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

    /**
     * Method that switches the app to the cart screen and initializes said screen
     * @param view current view
     */
    public void GotoCart(View view){
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
        }
    }

    /**
     * Flag for determining if a new pizza can be added
     * @param view current view
     */
    public void enableNewPizza(View view){
        btnNewPizza.setEnabled(true);
    }

    /**
     * Method for checking if order is valid
     * @return String representation of status of validation
     */
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

    /**
     * Method for refocusing after a pizza is added or screen is exited
     * @param requestCode
     * @param resultCode
     * @param data The Pizza
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String pizza = data.getStringExtra("result");
                    focus.addPizza(makePizza(pizza));
                    cartNum +=1;
                    reFocus();
                }else{
                    reFocus();
                }
                break;
            }
        }
    }

    /**
     * Method for creating an individual pizza
     * @param p toppings
     * @return the pizza created
     */
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
        //updateLVOrders();
        if(cartNum == 0) {
            txtOrderID.setEnabled(true);
            btnCart.setEnabled(false);
        }else {
            txtOrderID.setEnabled(false);
            btnCart.setText("Cart (" + cartNum + ")");
            btnCart.setEnabled(true);
        }
    }
}