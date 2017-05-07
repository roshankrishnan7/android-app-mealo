package rkthi3.mealo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rkthi3.mealo.models.MenuItem;

public class ViewItemActivity extends AppCompatActivity {

    private MenuItem menuItem;
    private EditText cNameEditText;
    private EditText cPriceEditText;

    private Button cAddButton;


    //private DatabaseHelper m_cDBHelper;
    // private ArrayList<Monster> monsterList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        setTitle("Item");
/*
        //database helper
        m_cDBHelper = new DatabaseHelper(this);
        // If there are no people in the db then add some defaults
        if(m_cDBHelper.GetAllMonster().size() == 0)
            m_cDBHelper.CreateDefaultMonster();
*/


        cNameEditText = (EditText) findViewById(R.id.nameEditText);
        cPriceEditText = (EditText) findViewById(R.id.priceEditText);


        Intent intent = getIntent();
        menuItem = intent.getParcelableExtra("ViewItem");


        //Set data from Monster object received from Calling page
        cNameEditText.setText(menuItem.getName());
        cPriceEditText.setText(Integer.toString( menuItem.getPrice()));
        cAddButton = (Button) findViewById(R.id.btnAdd);




            cAddButton.setVisibility(View.GONE);

            cNameEditText.setFocusable(false);
            cPriceEditText.setFocusable(false);




        //Listener for update button
        //Updates the specific monster details in the database
        cAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                                menuItem.setName(cNameEditText.getText().toString());
                                menuItem.setPrice(new Integer(cPriceEditText.getText().toString()));

                                //m_cDBHelper.UpdateMonster(monster);

                                // Update ListView
                                Toast.makeText(getBaseContext(),
                                        "Monster has been updated",
                                        Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                                //searchIntent.putExtra("MonsterList",monsterList);
                                startActivity(homeIntent);




            }
        });

        //Listener for Delete button
        //The monster is Deleted s


    }
}
