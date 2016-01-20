package com.fadetoproductions.rvkn.locationtracker;

import com.google.android.gms.location.LocationListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public String username;
    public EditText nameInput;
    public Button startButton;
    public Button sendLocationButton;
    public TextView finalNameTextView;

    private String SHARED_PREFERENCES_FILENAME = "LocationTrackerPreferences";


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 299 * 1000;  /* 299 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private long TIMER_INTERVAL = 5 * 60 * 1000; /* 5 mins */

    static Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeGoogleApiClient();
        setupParse();
        setupUiElements();
        startSendingLocationAtInterval();
    }

    public void setupUiElements() {
        nameInput = (EditText) findViewById(R.id.nameInput);
        startButton = (Button) findViewById(R.id.submitName);
        sendLocationButton = (Button) findViewById(R.id.sendLocationButton);
        finalNameTextView = (TextView) findViewById(R.id.finalName);
        setupSendLocationButtonListener();

        username = getSavedUsernameFromSharedPreferences();
        if (username.equals("")) {
            setupStartButtonListener();
        } else {
            setUsernameTextViewAndHideInput();
        }
    }

    public void setUsernameTextViewAndHideInput() {
        finalNameTextView.setText(username);
        hideUsernameSelection();
    }

    public void setupSendLocationButtonListener() {
        sendLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getAndSendLocation();
                Toast.makeText(getApplicationContext(), "Saving your location!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupStartButtonListener() {
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                username = nameInput.getText().toString();
                saveUsernameToSharedPreferences(username);
                Log.v("The user is ", username);
                String toastString = "Alright " + username + "! Let's this show on the road. Hopefully this is working :). You should be able to background the app now.";
                Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
                setUsernameTextViewAndHideInput();
                getAndSendLocation();
            }
        });
    }

    public void saveUsernameToSharedPreferences(String username) {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.commit();
    }

    public String getSavedUsernameFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
        return prefs.getString("username", "");
    }


    public void hideUsernameSelection() {
        nameInput.setVisibility(EditText.GONE);
        startButton.setVisibility(Button.GONE);
        sendLocationButton.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setupParse() {
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(LocationCoordinates.class);
        Parse.initialize(this, "x7Q6obaRCEfUdzPsPkuSn91woz3dUV9K81dEnaWj", "iX1mykGp2PgwO7uudVDpgBPeXGqZKjATtrmIUqtz");
    }

    public void startSendingLocationAtInterval() {
        timer = new Timer();
        timer.schedule(new SendLocationTimerTask(), 0, TIMER_INTERVAL);
    }

    protected void initializeGoogleApiClient() {
        // Create the location client to start receiving updates
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }

    protected void onStart() {
        super.onStart();
        Log.d("DEBUG", "STARTED");
        // Connect the client.
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        // Disconnecting the client invalidates it.
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        Log.d("DEBUG", "STOPPED");
        super.onStop();
    }

    public void onConnected(Bundle dataBundle) {
        Log.d("DEBUG", "CONNECTED");
        // Get last known recent location.
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        else {
            Log.d("DEBUG", "Current Location is NULL");
        }
//         Begin polling for new location updates.
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("DEBUG", "SUSPENDED");
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("DEBUG", "CONNECTION FAILED");
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void getAndSendLocation() {
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            // Print current location if not null
            String locationString = mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude();
            createAndSaveLocation(locationString);
            Log.d("DEBUG", locationString);
        }
    }

    private void createAndSaveLocation(String locationString) {
        LocationCoordinates locationCoordinates = new LocationCoordinates();
        locationCoordinates.setLocation(locationString);
        locationCoordinates.setUsername(username);
        Date date = new Date();
        locationCoordinates.setTimeVisited(date);
        locationCoordinates.uploadToParse();
    }

    public class SendLocationTimerTask extends TimerTask {
        @Override
        public void run() {
            Log.d("DEBUG", "TIMER TASK RUN");
            getAndSendLocation();
        }
    }
}
