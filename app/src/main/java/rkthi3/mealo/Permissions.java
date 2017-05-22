package rkthi3.mealo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;


public class Permissions extends Activity implements View.OnClickListener{
    /**
     * This will handle permissions asking
     */
    Button button;
    //handles ask permissions/continue
    boolean completed = false;
    public void onCreate(Bundle sis){
        super.onCreate(sis);
        /**
         * I'm not going to create the layout for you, but you essentially need two things:
         * a TextView
         * a Button
         *
         * The TextView will explain **why** you need the permissions, while the button
         * will be pressed by the user to ask the permissions. No need to call the textview
         * inside this class
         */
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if(!completed)
                    askPermissions();
                else{
                    //start the main activity
                }
                break;
        }
    }


    public void askPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ){
            completed = true;
            button.setText("Permissions supplied. Press to continue");
        }

    }
}