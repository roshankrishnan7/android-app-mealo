package rkthi3.mealo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;
import java.util.Iterator;

import rkthi3.mealo.models.MenuItem;

public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener {

    private ListView cListView;
    private ArrayList<MenuItem> menuItemList;
    private EditText ip ;
    private ItemAdapter adapter;


    /*commented for base*/
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;



    TextView txtJson;
    //private TextView count;
    //private ArrayList<MenuItem> monsterList;

    //private DatabaseHelper m_cDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MEAL-O");

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M ) {
            checkPermission();
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView headerView = (TextView) findViewById(R.id.navHeader);


       // TextView

        /*commented for base*/
        mToolBar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolBar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        cListView = (ListView) findViewById(R.id.listMenuItem);
        //ip = (EditText)findViewById(R.id.ipSearch);

        /*commented for base*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle =new  ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawerOpen, R.string.drawerClose);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();



        //load the list with items received from server
        try {
            initList();
        } catch (IOException e) {
            e.printStackTrace();
        }


        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // on Clicking any item from the list view, the item details are passed to ViewPage
                // Item details are displayed there and function to Update/Delete implement there
                Log.d("clicked", "> here");
                MenuItem selectedItem = (MenuItem) cListView.getItemAtPosition(position);
                Intent viewIntent = new Intent(getApplicationContext(), ViewItemActivity.class);
                viewIntent.putExtra("ViewItem", selectedItem);

                startActivity(viewIntent);
            }
        });

        View header_View =navigationView.getHeaderView(0);
        header_View.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               // Toast.makeText(getBaseContext(), "HOME",Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(mainIntent);

            }
        });

      /*  headerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getBaseContext(), "HOME",Toast.LENGTH_LONG).show();
            }
        });
*/

    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(android.view.MenuItem item){
        int id = item.getItemId();

        switch(id){
            case R.id.nav_account:
                Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contactUs:
                //Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.main_linear_layout, aboutUsFragment).commit();
                break;
            case R.id.nav_orders:
                Toast.makeText(this, "Orders", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_About:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initList() throws IOException {
//populate with data from database


        menuItemList = new ArrayList<>(/*get data from server*/);
        //txtJson = (TextView) findViewById(R.id.tvJSONItem);
        new JsonTask().execute("http://ec2-54-245-158-23.us-west-2.compute.amazonaws.com:3000/items");

/*        adapter = new ItemAdapter(this, menuItemList);
        cListView.setAdapter(adapter);
  */      //count.setText("Search Results: "+Integer.toString(monsterList.size()));
    }

    public void searchItems(String key){
//search results are filtered
        ArrayList<MenuItem> searchList = new ArrayList<MenuItem>();
        for(MenuItem item:menuItemList){
            if(item.getName().toLowerCase().contains(key.toLowerCase())){

                searchList.add(item);
            }
        }

        adapter = new ItemAdapter(this,searchList);
        cListView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
     //   count.setText("Search Results: "+Integer.toString(searchList.size()));
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        private ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Delicious Food Loading..");
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
                            menuItemList.add(new MenuItem(key,price));
                        }



                    } catch (JSONException e) {
                        // Oops
                    }


                }

                System.out.println(menuItemList.size());
                adapter = new ItemAdapter(MainActivity.this, menuItemList);
                cListView.setAdapter(adapter);

                    String name = jObject.getString("Item");
                    //System.out.print("hi " +name);
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
