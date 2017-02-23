package cardview.com.lokeasy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by Sidharth on 08-Feb-17.
 */
public class PowerReceiver extends BroadcastReceiver
{
    private static int countPowerOff = 0;
    LocationManager lm;
    String yourAddress ;
    String yourCity ;
    String yourCountry ;
    MainActivity main=new MainActivity();
    public PowerReceiver ()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            Log.e("In on receive", "In Method:  ACTION_SCREEN_OFF");
            countPowerOff++;
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            Log.e("In on receive", "In Method:  ACTION_SCREEN_ON");
            countPowerOff++;
        }


            if (countPowerOff > 2)
            {
                countPowerOff=0;

                LocationListener locationListener = new MyLocationListener();
                lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
/*
                    requestPermissions(main, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.BROADCAST_SMS,
                            android.Manifest.permission.SEND_SMS,
                            android.Manifest.permission.RECEIVE_SMS,
                            android.Manifest.permission.READ_SMS,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.READ_CONTACTS,
                    }, 10); */
                }


                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        60000,
                        1000,
                        locationListener);



                String locationProvider = LocationManager.NETWORK_PROVIDER;
                Location loc = lm.getLastKnownLocation(locationProvider);
                if(loc!=null) {
                    List<Address> yourAddresses = null;
                    try {
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        yourAddresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 10);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (yourAddresses != null && yourAddresses.size() != 0) {
                        yourAddress = yourAddresses.get(0).getAddressLine(0);
                        yourCity = yourAddresses.get(0).getAddressLine(1);
                        yourCountry = yourAddresses.get(0).getAddressLine(2);
                    }
                }
                else {


                    boolean gps_enabled = false;
                    boolean network_enabled = false;

                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch(Exception ex) {}

                    try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch(Exception ex) {}

                    if(!gps_enabled && !network_enabled) {
                        // notify user
                        class MyDialog extends Activity {
                            @Override
                            public void onCreate(Bundle savedInstanceState) {
                                super.onCreate(savedInstanceState);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
                            }}}
                }

                String URL = "content://cardview.com.lokeasy/contacts";

                Uri contacts = Uri.parse(URL);
                Cursor c = context.getContentResolver().query(contacts, null, null, null, "name");

                if (c.moveToFirst()) {
                    do {

                        SmsManager sms = SmsManager.getDefault();
                        String num=c.getString(c.getColumnIndex(EmergencyContact.NUMBER));

                        sms.sendTextMessage(num, null, "I am in an Emergency,Please contact me immediatly !!   I am at "+yourAddress+" "+yourCity+" "+yourCountry, null, null);





                    } while (c.moveToNext());


                }


                timer.cancel();
                timer.start();













            }

    }
    private CountDownTimer timer = new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            countPowerOff = 0;
        }
    };
    public class LocalBinder extends Binder {
        PowerReceiver getService() {
            return PowerReceiver.this;
        }
    }



    private class MyLocationListener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location loc) {
            if (loc != null) {


                //  sms.sendTextMessage(senderTel, null, "Sorry !!", null, null);

            }
            //lm.removeUpdates(locationListener);

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
        }
    }

}


