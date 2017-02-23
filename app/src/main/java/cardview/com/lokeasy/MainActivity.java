package cardview.com.lokeasy;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static cardview.com.lokeasy.R.id.map;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private GoogleMap mMap;
    LocationManager lm;
    MarkerOptions markerOptions;
    LatLng latLng;
    double lat = 0;
    double lng = 0;
    PowerReceiver powerReceiver;
    Location location;
    double latnew = 0;
    double lonnew =0;

    boolean[] marker ;
    private boolean showLevelPicker = true;
    private AudioManager maudio, mmaudio;
    private boolean msilent, amsilent = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
       // MainActivity.this.startService(new Intent(MainActivity.this, LockService.class));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.BROADCAST_SMS,
                    android.Manifest.permission.SEND_SMS,
                    android.Manifest.permission.RECEIVE_SMS,
                    android.Manifest.permission.READ_SMS,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.READ_CONTACTS}, 10);

        }

dis();
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        // Showing status
        if(status!= ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        getLoaderManager().initLoader(0, null, this);
        lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.BROADCAST_SMS,
                    android.Manifest.permission.SEND_SMS,
                    android.Manifest.permission.RECEIVE_SMS,
                    android.Manifest.permission.READ_SMS,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.READ_CONTACTS}, 10);

        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, new MyLocationListener());
        mmaudio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        maudio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.BROADCAST_SMS,
                    android.Manifest.permission.SEND_SMS,
                    android.Manifest.permission.RECEIVE_SMS,
                    android.Manifest.permission.READ_SMS,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.READ_CONTACTS}, 10);

        }
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        powerReceiver = new PowerReceiver();
        registerReceiver(powerReceiver, filter);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.emergencycontacts) {

            Intent intent=new Intent(MainActivity.this,Emergency_Contact.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_smstracker) {

            android.support.v4.app.Fragment fm = new Sms_Tracker();
            android.support.v4.app.FragmentManager fm1 =  getSupportFragmentManager();
            fm1.beginTransaction().replace(R.id.content_main, fm).commit();

        } else if (id == R.id.nav_signout) {

            android.support.v4.app.Fragment fm = new StoredAddresses();
            android.support.v4.app.FragmentManager fm1 =  getSupportFragmentManager();
            fm1.beginTransaction().replace(R.id.content_main, fm).commit();

        } else if (id == R.id.nav_support) {

            android.support.v4.app.Fragment fm = new Support();
            android.support.v4.app.FragmentManager fm1 =  getSupportFragmentManager();
            fm1.beginTransaction().replace(R.id.content_main, fm).commit();

        } else if (id == R.id.nav_contact) {

            String email[]={"lokeasyhelp@gmail.com"};
            openmail(this,email,"","");

        } else if (id == R.id.nav_about) {
            android.support.v4.app.Fragment fm = new About();
            android.support.v4.app.FragmentManager fm1 =  getSupportFragmentManager();
            fm1.beginTransaction().replace(R.id.content_main, fm).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openmail(Activity activity, String[] email, String subject, String content)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = activity.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        activity.startActivity(emailIntent);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

         /*   ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.BROADCAST_SMS,
                    android.Manifest.permission.SEND_SMS,
                    android.Manifest.permission.RECEIVE_SMS,
                    android.Manifest.permission.READ_SMS,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.READ_CONTACTS}, 10); */
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings();


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Drawing marker on the map
                drawMarker(point);

                // Creating an instance of ContentValues
                ContentValues contentValues = new ContentValues();

                // Setting latitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LAT, point.latitude );

                // Setting longitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LNG, point.longitude);

                // Setting zoom in ContentValues
                contentValues.put(LocationsDB.FIELD_ZOOM, mMap.getCameraPosition().zoom);

                // Creating an instance of LocationInsertTask
                LocationInsertTask insertTask = new LocationInsertTask();

                // Storing the latitude, longitude and zoom level to SQLite database
                insertTask.execute(contentValues);

                Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();



                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.BROADCAST_SMS,
                            android.Manifest.permission.SEND_SMS,
                            android.Manifest.permission.RECEIVE_SMS,
                            android.Manifest.permission.READ_SMS,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.READ_CONTACTS}, 10);
                }
                String locationProvider = LocationManager.GPS_PROVIDER;
                Location lastKnownLocation = lm.getLastKnownLocation(locationProvider);

                if(lastKnownLocation!=null)
                {
                    double latnew = lastKnownLocation.getLatitude();
                    double lonnew = lastKnownLocation.getLongitude();

                    if (distance(lat, lng, latnew, lonnew) < 0.1) {

                        maudio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        marker[0] = true;


                    }

                }
                else{

                    MyLocationListener locationListener;

                    locationListener = new MyLocationListener();
                    lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    lm.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener);

                }


                MyLocationListener ml = new MyLocationListener();
                ml.onLocationChanged(lastKnownLocation);




            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {

                // Removing all markers from the Google Map
                mMap.clear();
                mmaudio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                // Creating an instance of LocationDeleteTask
                LocationDeleteTask deleteTask = new LocationDeleteTask();

                // Deleting all the rows from SQLite database table
                deleteTask.execute();

                Toast.makeText(getBaseContext(), "All markers are removed", Toast.LENGTH_LONG).show();
            }
        });





    }
    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>{
        @Override
        protected Void doInBackground(ContentValues... contentValues) {

            /** Setting up values to insert the clicked location into SQLite database */
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {

            /** Deleting all the locations stored in SQLite database */
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int arg0,
                                         Bundle arg1) {

        // Uri to the content provider LocationsContentProvider
        Uri uri = LocationsContentProvider.CONTENT_URI;

        // Fetches all the rows from locations table
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0,
                               Cursor arg1) {
        int locationCount = 0;
        double lat=0;
        double lng=0;
        float zoom=0;

        // Number of locations available in the SQLite database table
        locationCount = arg1.getCount();


        // Number of locations available in the SQLite database table

        boolean[] marker = new boolean[locationCount];

        // Move the current record pointer to the first row of the table
        arg1.moveToFirst();

        for(int i=0;i<locationCount;i++){

            // Get the latitude
            lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));

            // Get the longitude
            lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));

            // Get the zoom level
            zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM));

            // Creating an instance of LatLng to plot the location in Google Maps
            LatLng location = new LatLng(lat, lng);

            // Drawing the marker in the Google Maps
            drawMarker(location);

            String locationProvider = LocationManager.GPS_PROVIDER;

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

              /*  ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.BROADCAST_SMS,
                        android.Manifest.permission.SEND_SMS,
                        android.Manifest.permission.RECEIVE_SMS,
                        android.Manifest.permission.READ_SMS,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.READ_CONTACTS}, 10);*/
            }           Location lastKnownLocation = lm.getLastKnownLocation(locationProvider);


            if(lastKnownLocation!=null)
            {
                double latnew = lastKnownLocation.getLatitude();
                double lonnew = lastKnownLocation.getLongitude();

                if (distance(lat, lng, latnew, lonnew) < 0.1) {

                    maudio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    marker[i] = true;


                }

            }
            else{
                LocationManager lm;
                MyLocationListener locationListener;

                locationListener = new MyLocationListener();
                lm = (LocationManager)getSystemService(LOCATION_SERVICE);
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0,
                        locationListener);

            }


                    /*	else
						{
							maudio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						    amsilent=false;
						}

*/


            // Traverse the pointer to the next row
            arg1.moveToNext();
        }

        int flag = 0;
        for (int m = 0; m < locationCount; m++) {
            if (marker[m] == true) {

                flag = 1;
                break;

            }
        }

        if (flag == 0) {
            maudio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        }

        for (int n = 0; n < locationCount; n++) {
            marker[n] = false;
        }



        if(locationCount>0){
            // Moving CameraPosition to last clicked position
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));

            // Setting the zoom level in the map on last position  is clicked
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub

    }

    private void drawMarker(LatLng point){

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(point);

        mMap.addMarker(markerOptions);

    }
    private double distance(double lat, double lng, double latnew, double lonnew) {
        double earth_radius = 3958.75;
        double dLat = Math.toRadians(latnew - lat);
        double dLng = Math.toRadians(lonnew - lng);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(latnew));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earth_radius * c;
        return dist;

    }

    private class MyLocationListener implements android.location.LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub


            if(location!=null) {
                double latnew=location.getLatitude();
                double lonnew=location.getLongitude();

                if (distance(lat, lng, latnew, lonnew) < 0.1) {

                    maudio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    marker[0] = true;


                }
            }
                else{
                Toast.makeText(getApplicationContext(),"Unable to get your location. Please try moving around.",Toast.LENGTH_SHORT).show();
                }


            getLoaderManager().restartLoader(0, null,MainActivity.this);


        }


        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }
    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);


                LatLng point=place.getLatLng();


                drawMarker(point);

                // Creating an instance of ContentValues
                ContentValues contentValues = new ContentValues();

                // Setting latitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LAT, point.latitude );

                // Setting longitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LNG, point.longitude);

                // Setting zoom in ContentValues
                contentValues.put(LocationsDB.FIELD_ZOOM, mMap.getCameraPosition().zoom);

                // Creating an instance of LocationInsertTask
                LocationInsertTask insertTask = new LocationInsertTask();

                // Storing the latitude, longitude and zoom level to SQLite database
                insertTask.execute(contentValues);

                Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();



                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.BROADCAST_SMS,
                            android.Manifest.permission.SEND_SMS,
                            android.Manifest.permission.RECEIVE_SMS,
                            android.Manifest.permission.READ_SMS,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.READ_CONTACTS}, 10);
                }
                String locationProvider = LocationManager.GPS_PROVIDER;
                Location lastKnownLocation = lm.getLastKnownLocation(locationProvider);

                MyLocationListener ml = new MyLocationListener();
                ml.onLocationChanged(lastKnownLocation);




            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(),"Unable to add the selected place.",Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    // An AsyncTask class for accessing the GeoCoding Web Service
    class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }


        @Override
        protected void onPostExecute(List<Address> addresses) {

            // Adding Markers on Google Map for each matching address
            for (int i = 0; i < addresses.size(); i++) {

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                mMap.addMarker(markerOptions);

                // Locate the first location
                if (i == 0)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }

        }
    }



    public void dis(){


        boolean gps_enabled = false;
        boolean network_enabled = false;
        lm = (LocationManager)getSystemService(LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled || !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }}


    @Override
    protected void onDestroy()
    {
        if (powerReceiver != null)
        {
            unregisterReceiver(powerReceiver);
            powerReceiver = null;
        }
        super.onDestroy();
    }

    }
