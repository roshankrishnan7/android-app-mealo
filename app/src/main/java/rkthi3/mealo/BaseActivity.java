package rkthi3.mealo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import static rkthi3.mealo.R.id.drawerLayout;

public class BaseActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    public DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;

/*
    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        //super.onCreateDrawer();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView headerView = (TextView) findViewById(R.id.navHeader);


        mToolBar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolBar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /*commented for base*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle =new  ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawerOpen, R.string.drawerClose);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        View header_View =navigationView.getHeaderView(0);
        header_View.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Toast.makeText(getBaseContext(), "HOME",Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(mainIntent);
            }
        });

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
                //Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(getApplicationContext(), SignInActivity.class);

                startActivity(loginIntent);

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }


}