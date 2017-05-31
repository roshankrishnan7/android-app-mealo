package rkthi3.mealo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rkthi3.mealo.models.Cart;
import rkthi3.mealo.models.MenuItem;

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

        cart = new Cart();
        cart.addItem(item,1);

        tvTotal =  (TextView) findViewById(R.id.lblTotal);
        btnPay  = (Button) findViewById(R.id.btnPay);

        tvTotal.setText("Total : "+ String.valueOf(cart.getTotal()));
        btnPay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               Toast.makeText(getBaseContext(),
                        "Payment Successful",
                        Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(getApplicationContext(), DeliveryLocationActivity.class);

                startActivity(mainIntent);

            }
        });

    }
}
