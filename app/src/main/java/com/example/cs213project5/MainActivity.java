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
//import com.google.android.material.textfield.TextInputEditText;

//import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * Main Activity class for displaying/functionality of Main Screen
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Order ID Input Box
     */
    private EditText txtOrderID;
    /**
     * Radio button for deluxe pizza
     */
    private RadioButton rdbDeluxe;
    /**
     * Radio button for Hawaiian pizza
     */
    private RadioButton rdbHawaiian;
    /**
     * Radio button for Pepperoni Pizza
     */
    private RadioButton rdbPepperoni;
    /**
     * Button for accessing stored orders
     */
    private Button btnStoreOrders;
    /**
     * Button for complete orders
     */
    private Button btnComplete;
    /**
     * Button for new pizza
     */
    private Button btnNewPizza;
    /**
     * Button to access cart
     */
    private Button btnCart;
    /**
     * List view of stored orders
     */
    private ListView lvStoreOrders;
    /**
     * Number of items in cart
     */
    private int cartNum = 0;
    /**
     * Code for Pizza Customizer Activity
     */
    private final int PCRequestCode = 1;
    /**
     * Code for Current Order Activity
     */
    private final int CORequestCode = 2;
    /**
     * Code for Store Orders Activity
     */
    private final int SORequestCode = 3;
    /**
     * Selected item
     */
    private Order focus = null;
    /**
     * Stored Orders
     */
    private StoreOrders storeOrders = new StoreOrders();
    /**
     * Selected Order
     */
    private int selectedOrder = -1;
    /**
     * Max Size of Phone Number
     */
    private final int maxOIDLen = 10;
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
        lvStoreOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedOrder = position;
            }
        });
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
        lvStoreOrders = (ListView)findViewById(R.id.lvstoreOrders);
    }
    /**
     * Method that brings up pizza customizer activity
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
       gotoCartHelper();
    }
    /**
     * Method that switches the app to stored orders screen
     * @param view current view
     */
    public void GotoStoreOrders(View view){
        gotoAllOrdersHelper();
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
            if(txtOrderID.getText().toString().trim().length() != maxOIDLen) {
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
     * Method to transfer pizza index data between activities
     * @param requestCode desired code
     * @param resultCode return code
     * @param data information to transfer
     */
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
                if(resultCode == Constants.cancelNone) {
                    int orderIndex = data.getIntExtra(Intent.EXTRA_TEXT, -1);
                    Order o = storeOrders.getOrder(orderIndex);
                    storeOrders.completeOrder(o);
                    reFocus();
                    gotoAllOrdersHelper();
                }
                reFocus();
            }

    }
    public void onPause()
    {
        super.onPause();

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

    /**
     * Method for calculating cart stats
     */
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

    /**
     * Helper Method for Stored Orders screen
     */
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

    /**
     * Method to parse string order into pizza representation
     * @param p String representation of pizza
     * @return Pizza representation of order string
     */
    private Pizza makePizza(String p){
        ArrayList<Topping> Toppings = new ArrayList<Topping>();
        String[] data = p.split(",");
        int dataStart = 2;
        for(int x = dataStart; x < data.length; x++){
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
        int nullSelect = -1;
        if(selectedOrder != nullSelect) {
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