package rkthi3.mealo;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import rkthi3.mealo.models.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ListView cListView;
    private ArrayList<MenuItem> menuItemList;
    private EditText ip ;
    private ItemAdapter adapter;
    private ProgressDialog pd;
    TextView txtJson;
    //private TextView count;
    //private ArrayList<MenuItem> monsterList;

    //private DatabaseHelper m_cDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MENU");

        cListView = (ListView) findViewById(R.id.listMenuItem);
        //ip = (EditText)findViewById(R.id.ipSearch);


      /*  m_cDBHelper = new DatabaseHelper(getApplicationContext());
        // If there are no monsters in the db then add some defaults
        if(m_cDBHelper.GetAllMonster().size() == 0)
            m_cDBHelper.CreateDefaultMonster();
*/

        //load the list with items received from server
        try {
            initList();
        } catch (IOException e) {
            e.printStackTrace();
        }

//search
        /*
        ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //used to implement search as user types into the EditText box
                if(charSequence.toString().equals("")){
                    try {
                        initList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                else {
                    searchItems(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/
        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // on Clicking any item from the list view, the monster details are passed to ViewPage
                // Monster details are displayed there and function to Update/Delete implement there
                MenuItem selectedItem = (MenuItem) cListView.getItemAtPosition(position);
                Intent viewIntent = new Intent(getApplicationContext(), ViewItemActivity.class);
                viewIntent.putExtra("ViewItem", selectedItem);

                startActivity(viewIntent);
            }
        });



    }

    public void initList() throws IOException {
//populate with data from database
/*
        JSONObject request = new JSONObject();
        try
        {
            request.put("Param1", "Value1");
            request.put("Param2", "Value2");
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        String url = "http://ec2-54-213-142-117.us-west-2.compute.amazonaws.com:3000/items";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTxtDisplay.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });*/

/*/------------------------------------------
        HttpURLConnection urlConnection = null;

        URL url = new URL("http://ec2-54-213-20-213.us-west-2.compute.amazonaws.com:80/items");

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000   );
        urlConnection.setConnectTimeout(15000 );

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);

        //return new JSONObject(jsonString);
//**/

//--------------------


        txtJson = (TextView) findViewById(R.id.tvJSONItem);
        new JsonTask().execute("http://ec2-54-213-20-213.us-west-2.compute.amazonaws.com:80/items");
        menuItemList = new ArrayList<>(/*get data from server*/);
        adapter = new ItemAdapter(this, menuItemList);
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

        adapter = new ItemAdapter(this,searchList);
        cListView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
     //   count.setText("Search Results: "+Integer.toString(searchList.size()));
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
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
            txtJson.setText(result);
        }
    }



}
