package rkthi3.mealo;

import android.content.Intent;
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class PaymentActivity extends AppCompatActivity {

    private Cart cart;
    private TextView tvTotal;
    private Button btnPay;
    private MenuItem item;

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
        btnPay  = (Button) findViewById(R.id.btnPay);

        tvTotal.setText("Total : "+ String.valueOf(cart.getTotal()));
        btnPay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
                Card card = mCardInputWidget.getCard();
                if (card == null) {
                    //mErrorDialogHandler.showError("Invalid Card Data");
                    Toast.makeText(getBaseContext(),
                            "Invalid Card Data",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    //Stripe calls
                    Stripe stripe = new Stripe(getApplicationContext(), "pk_test_SGpwHUfbw96ZhzFAJEBAaVAM");
                    stripe.createToken(
                            card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    // Send token to your server
                                    new StripeCharge(token.getId()).execute();
                                }
                                public void onError(Exception error) {
                                    // Show localized error message
                                    Log.d("Stripe", error.getLocalizedMessage());
                                }
                            }
                    )



                    Toast.makeText(getBaseContext(),
                            "Payment Successful",
                            Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(getApplicationContext(), DeliveryLocationActivity.class);

                    startActivity(mainIntent);
                }


            }
        });

    }

    public class StripeCharge extends AsyncTask<String, Void, String> {
        String token;

        public StripeCharge(String token) {
            this.token = token;
        }

        @Override
        protected String doInBackground(String... params) {
            new Thread() {
                @Override
                public void run() {
                    postData(name,token,""+amount);
                }
            }.start();
            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Result",s);
        }

        public void postData(String description, String token,String amount) {
            // Create a new HttpClient and Post Header
            try {
                URL url = new URL("[YOUR_SERVER_CHARGE_SCRIPT_URL]");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new NameValuePair("method", "charge"));
                params.add(new NameValuePair("description", description));
                params.add(new NameValuePair("source", token));
                params.add(new NameValuePair("amount", amount));

                OutputStream os = null;

                os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
