package rkthi3.mealo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import rkthi3.mealo.models.Cart;
import rkthi3.mealo.models.MenuItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class ViewItemActivity extends AppCompatActivity {

    private MenuItem menuItem;
    private TextView mName;
    private TextView mPrice;
    private TextView mDescription;
    private TextView mAllergent;
    private Button cAddButton;
    private Cart cart;
    private int qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        setTitle("Item");
        cart = new Cart();

        mName = (TextView) findViewById(R.id.lblName);
        mPrice = (TextView) findViewById(R.id.lblPrice);

        Intent intent = getIntent();
        menuItem = intent.getParcelableExtra("ViewItem");

       /* try {
            getItem();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        mName.setText(menuItem.getName());
        mPrice.setText(Integer.toString( menuItem.getPrice()));
        cAddButton = (Button) findViewById(R.id.btnAdd);

    //cAddButton.setVisibility(View.GONE);


        //Listener for add to cart button

        cAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                qty=1;
                               // cart.addItem(menuItem,qty);

                                Toast.makeText(getBaseContext(),
                                        "Item added to cart",
                                        Toast.LENGTH_SHORT).show();
                                Intent paymentIntent = new Intent(getApplicationContext(), PaymentActivity.class);
                paymentIntent.putExtra("Cart", menuItem);

                startActivity(paymentIntent);

            }
        });

    }


    public void getItem() throws IOException {
//populate with data from database


     //   menuItem = new ArrayList<>(/*get data from server*/);
        //txtJson = (TextView) findViewById(R.id.tvJSONItem);
        new JsonTask().execute("http://ec2-54-245-158-23.us-west-2.compute.amazonaws.com:3000/items/"+menuItem.getName());

/*        adapter = new ItemAdapter(this, menuItemList);
        cListView.setAdapter(adapter);
  */      //count.setText("Search Results: "+Integer.toString(monsterList.size()));
    }


    private class JsonTask extends AsyncTask<String, String, String> {

        private ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ViewItemActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }


            try {
                //JSONArray jArray = new JSONArray(result);
                //for(int i=0; i < jArray.length(); i++) {

                JSONObject jObject = new JSONObject(result);




                JSONArray jArray = jObject.getJSONArray("Item");

                for (int i=0; i < jArray.length(); i++)
                {
                    try {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        // Pulling items from the array

                        Iterator iterator = oneObject.keys();
                        String key = null;
                        while (iterator.hasNext()) {
                            key = (String) iterator.next();
                            System.out.println(key);
                            int price = oneObject.getJSONObject(key).getInt("price");
                            System.out.println(price);
                           // menuItemList.add(new MenuItem(key,price));
                        }



                    } catch (JSONException e) {
                        // Oops
                    }


                }

                /*commented on 23-may-2017
              //  System.out.println(menuItemList.size());
               // adapter = new ItemAdapter(MainActivity.this, menuItemList);
                //cListView.setAdapter(adapter);
*/
                String name = jObject.getString("Item");
                System.out.print("hi " +name);
                //String tab1_text = jObject.getString("tab1_text");
                //int price = jObject.getInt("active");
                //  txtJson.setText(name);
                //} // End Loop
                this.pd.dismiss();
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
        }
    }
}
