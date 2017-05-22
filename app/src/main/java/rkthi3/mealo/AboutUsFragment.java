package rkthi3.mealo;


import android.location.Location;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.SupportMapFragment;
import android.support.v4.app.Fragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import rkthi3.mealo.MainActivity;
import rkthi3.mealo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // Min and Max Update Intervals for our Location Service
    private static final long MAX_UPDATE_INTERVAL = 10000; // 10 Seconds
    private static final long MIN_UPDATE_INTERVAL = 2000; // 2 Seconds


    private static final LatLng LOCATION_MEALO
            = new LatLng(-37.865621, 145.006346);

    private GoogleMap m_cGoogleMap;
    private Location m_cCurrentLocation;
    private GoogleApiClient m_cAPIClient;


    private SupportMapFragment mapFrag;
  /*
    public AboutUsFragment() {
        FragmentManager fm = getChildFragmentManager();
        // Required empty public constructor
        mapFrag = (SupportMapFragment) fm.findFragmentById(R.id.map_fragment);
        mapFrag.getMapAsync(this);

        if (m_cAPIClient == null) {
            // Create our API Client and tell it to connect to Location Services
            m_cAPIClient = new GoogleApiClient.Builder(super.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (m_cAPIClient == null) {
            // Create our API Client and tell it to connect to Location Services
            m_cAPIClient = new GoogleApiClient.Builder(super.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, null);

        //SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        //initializeMap();
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        return view ;
        //inflater.inflate(R.layout.fragment_about_us, container, false);
        // Check to see if our APIClient is null.

        }

        // Set up an asyncronous callback to let us know when the map has loaded


    private void initializeMap(){
        if (m_cGoogleMap == null) {
            mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Function is called once the map has fully loaded.
        // Set our map object to reference the loaded map
        m_cGoogleMap = googleMap;
        // Move the focus of the map to be on Caulfield Campus. 15 is for zoom
        m_cGoogleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(LOCATION_MEALO,15));
        // Call our Add Default Markers function
        // NOTE: In a proper application it may be better to load these from a DB
        AddDefaultMarkers();
    }

    private void AddDefaultMarkers() {
        // Create a series of markers for each campus with the title being the

        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_MEALO).title("MEALO"));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Android 6.0 & up added security permissions
        // If the user rejects allowing access to location data then this try block
        // will stop the application from crashing (Will not track location)
        try {
            // Set up a constant updater for location services
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(MAX_UPDATE_INTERVAL)
                    .setFastestInterval(MIN_UPDATE_INTERVAL);
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(m_cAPIClient, locationRequest, this);
        }
        catch (SecurityException secEx) {
            Toast.makeText(super.getContext(), "ERROR: Please enable location services" , Toast.LENGTH_LONG).show();
            Log.d("roshan",secEx.getStackTrace().toString());
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        // Do nothing for now. This function is called should the connection halt
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Do nothing for now. This function is called if the connection fails
        //initially
    }
    @Override
    public void onLocationChanged(Location location) {
        // This is our function that is called whenever we change locations
        // Update our current location variable
        m_cCurrentLocation = location;
        ChangeMapLocation();
    }
    private void ChangeMapLocation() {
        // Check to ensure map and location are not null
        if(m_cCurrentLocation != null && m_cGoogleMap != null) {
            // Create a new LatLng based on our new location
            LatLng newPos = new LatLng(m_cCurrentLocation.getLatitude(),
                    m_cCurrentLocation.getLongitude());
            // Change the map focus to be our new location
            m_cGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPos, 15));
        }
    }
    @Override
    public void onStart() {
        m_cAPIClient.connect();
        super.onStart();
    }
    @Override
    public void onStop() {
        m_cAPIClient.disconnect();
        super.onStop();
    }

}
