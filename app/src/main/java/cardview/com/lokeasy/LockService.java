package cardview.com.lokeasy;

/**
 * Created by Sidharth on 19-Dec-16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
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
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class LockService extends Service {
int count=0,c=0;
    LocationManager lm;
    String yourAddress ;
    String yourCity ;
    String yourCountry ;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      /* final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        count++;
        filter.addAction(Intent.ACTION_SCREEN_ON);
        count++;
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        count++;

if(count==3) { */
    //final BroadcastReceiver mReceiver = new ScreenReceiver();
    // registerReceiver(mReceiver, filter);





    LocationListener locationListener = new MyLocationListener();
    lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
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
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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
    Cursor c = getContentResolver().query(contacts, null, null, null, "name");

    if (c.moveToFirst()) {
        do {
            SmsManager sms = SmsManager.getDefault();
            String num=c.getString(c.getColumnIndex(EmergencyContact.NUMBER));

            sms.sendTextMessage(num, null, "I am in an Emergency,Please contact me immediatly !!   The person is at "+yourAddress+" "+yourCity+" "+yourCountry, null, null);





        } while (c.moveToNext());


    }


    timer.cancel();
    timer.start();



    count = 0;








        return super.onStartCommand(intent, flags, startId);

    }


    private CountDownTimer timer = new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            count = 0;
        }
    };
    public class LocalBinder extends Binder {
        LockService getService() {
            return LockService.this;
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


