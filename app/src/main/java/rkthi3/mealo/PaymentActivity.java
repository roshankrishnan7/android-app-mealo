package rkthi3.mealo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rkthi3.mealo.models.Cart;
import rkthi3.mealo.models.MenuItem;

import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.Stripe;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import static java.net.Proxy.Type.HTTP;


public class PaymentActivity extends AppCompatActivity {

    private Cart cart;
    private TextView tvTotal;
    private Button btnPay;
    private MenuItem item;
    private CardInputWidget mCardInputWidget;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        Intent intent = getIntent();
        item = intent.getParcelableExtra("Cart");
      //  cart = (Cart) getIntent().getExtras().getSerializable("Cart");

        //ErrorDialogHandler mErrorDialogHandler = new ErrorDialogHandler;
        cart = new Cart();
        cart.addItem(item,1);





        tvTotal =  (TextView) findViewById(R.id.lblTotal);
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        btnPay  = (Button) findViewById(R.id.btnPay);

        tvTotal.setText("Total : $"+ String.valueOf(cart.getTotal()));
        btnPay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pd = new ProgressDialog(PaymentActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setMessage("Processing Payment..");
                pd.setCancelable(false);
                pd.show();

                Card card = mCardInputWidget.getCard();
                if (card == null) {
                    //mErrorDialogHandler.showError("Invalid Card Data");
                    Toast.makeText(getBaseContext(),
                            "Invalid Card Data",
                            Toast.LENGTH_SHORT).show();
                    if (pd.isShowing()){
                        pd.dismiss();
                    }
                }
                else{
                    //Stripe calls
                    Stripe stripe = new Stripe(getApplicationContext(), "pk_test_SGpwHUfbw96ZhzFAJEBAaVAM");
                    stripe.createToken(
                            card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    // Send token to your server
                                    new StripeCharge(token.getId()).execute(("http://ec2-54-245-158-23.us-west-2.compute.amazonaws.com:3000/stripe"));
                                }
                                public void onError(Exception error) {
                                    // Show localized error message
                                    Log.d("Stripe", error.getLocalizedMessage());
                                    if (pd.isShowing()){
                                        pd.dismiss();
                                    }
                                }
                            }
                    );




                }


            }
        });

    }

    private class StripeCharge extends AsyncTask<String, Void, String> {



        String token;

        public StripeCharge(String token) {
            this.token = token;
        }
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PaymentActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Processing Payment..");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection conn = null;
            //BufferedReader reader = null;
            StringBuffer buffer = null;

            try {
                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                //connection.connect();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type","application/json");
                conn.connect();

             /*   List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new NameValuePair("method", "charge"));
                params.add(new NameValuePair("description", "TEST charge"));
                params.add(new NameValuePair("source", token));
                params.add(new NameValuePair("amount", "100"));
*/
                JSONObject stripeData = new JSONObject();
                try {
                    stripeData.put("method", "charge");
                    stripeData.put("description", "TEST charge");
                    stripeData.put("source", token);
                    stripeData.put("amount", "100");


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }


                DataOutputStream printout;
                DataInputStream input;
                StringBuilder sb = new StringBuilder();


                printout = new DataOutputStream(conn.getOutputStream());
                Log.d("JSON",stripeData.toString());

                printout.writeBytes( stripeData.toString());//URLEncoder.encode(stripeData.toString(),"UTF-8"));
                printout.flush();
                printout.close();


                int HttpResult = conn.getResponseCode();
                if(HttpResult ==HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            conn.getInputStream(),"utf-8"));
                    buffer = new StringBuffer();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    reader.close();
                    System.out.println(""+buffer.toString());

                    return buffer.toString();

                }else{
                    System.out.println(conn.getResponseMessage());

                }

            }


            catch (MalformedURLException e) {

                e.printStackTrace();

            }
            catch (IOException e) {

                e.printStackTrace();
            }   finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
          return buffer.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }

            try {
            JSONObject respJSON = new JSONObject(result);
                String status = respJSON.getString("status");
                if(status.equals("success")){
                    Toast.makeText(getBaseContext(),
                            "Payment Successful",
                            Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(getApplicationContext(), DeliveryLocationActivity.class);

                    startActivity(mainIntent);
                }
                else{
                    Toast.makeText(getBaseContext(),
                            "Payment Failed",
                            Toast.LENGTH_SHORT).show();
                    //Intent mainIntent = new Intent(getApplicationContext(), DeliveryLocationActivity.class);

                    //startActivity(mainIntent);
                }

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }


        }
    }


}
