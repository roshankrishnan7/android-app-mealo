package rkthi3.mealo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import rkthi3.mealo.models.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ListView cListView;
    private ArrayList<MenuItem> menuItemList;
    private EditText ip ;
    private UserAdapter adapter;
    private TextView count;
    //private ArrayList<MenuItem> monsterList;

    //private DatabaseHelper m_cDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Search ItemS");

        cListView = (ListView) findViewById(R.id.listMonster);
        ip = (EditText)findViewById(R.id.ipSearch);
        count = (TextView) findViewById(R.id.tvCount);

      /*  m_cDBHelper = new DatabaseHelper(getApplicationContext());
        // If there are no monsters in the db then add some defaults
        if(m_cDBHelper.GetAllMonster().size() == 0)
            m_cDBHelper.CreateDefaultMonster();
*/

        //load the list with items received from server
        initList();


        ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //used to implement search as user types into the EditText box
                if(charSequence.toString().equals("")){
                    initList();

                }

                else {
                    searchItems(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // on Clicking any item from the list view, the monster details are passed to ViewPage
                // Monster details are displayed there and function to Update/Delete implement there
                MenuItem selectedItem = (MenuItem) cListView.getItemAtPosition(position);
                Intent viewIntent = new Intent(getApplicationContext(), ViewItemActivity.class);
                viewIntent.putExtra("ViewItem",selectedItem);

                startActivity(viewIntent);
            }
        });



    }

    public void initList(){
//populate with data from database
        menuItemList = new ArrayList<>(/*get data from server*/);
        adapter = new UserAdapter(this, menuItemList);
        cListView.setAdapter(adapter);
        //count.setText("Search Results: "+Integer.toString(monsterList.size()));
    }

    public void searchItems(String key){
//search results are filtered
        ArrayList<MenuItem> searchList = new ArrayList<MenuItem>();
        for(MenuItem item:menuItemList){
            if(item.getName().toLowerCase().contains(key.toLowerCase())){

                searchList.add(item);
            }
        }

        adapter = new UserAdapter(this,searchList);
        cListView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        count.setText("Search Results: "+Integer.toString(searchList.size()));
    }

}
