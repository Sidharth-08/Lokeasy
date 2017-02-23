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
    PowerReceiver powerReceiver;
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


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        powerReceiver = new PowerReceiver();
        registerReceiver(powerReceiver, filter);



        super.onCreate();
    }


    @Override
    public boolean stopService(Intent intent) {

        if (powerReceiver != null)
        {
            unregisterReceiver(powerReceiver);
            powerReceiver = null;
        }
        return true;
    }




}


