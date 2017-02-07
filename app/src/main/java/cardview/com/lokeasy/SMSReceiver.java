package cardview.com.lokeasy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SMSReceiver extends BroadcastReceiver
{
	Context context12 = null;
	LocationManager lm;
	MyLocationListener locationListener;


	String senderTel,ss12="",ss11="";
	String yourAddress ="";
	String yourCity = "";
	String yourCountry = "";



	@Override
	public void onReceive(Context context, Intent intent)
	{

		context12=context;



		SharedPreferences sharedpreferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
		ss11 = sharedpreferences.getString("someValue" , "");

		Bundle bundle = intent.getExtras();

		SmsMessage[] msgs = null;
		String str="";


		if (bundle != null)
		{
			senderTel = "";

			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i=0; i<msgs.length; i++)
			{
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				if (i==0)
				{
					senderTel = msgs[i].getOriginatingAddress();
				}
				str += msgs[i].getMessageBody().toString();

			}




			if(str.startsWith(ss11.toString()))
			{


				lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
				if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
				}


				locationListener = new MyLocationListener();
				lm.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER,
						0,
						0,
						locationListener);

				Location location = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);






				SmsManager sms = SmsManager.getDefault();





				List<Address> yourAddresses =null;
				try {
					Geocoder geocoder=new Geocoder(context12, Locale.getDefault());
					yourAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 10);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (yourAddresses != null && yourAddresses.size() != 0)
				{
					yourAddress = yourAddresses.get(0).getAddressLine(0);
					yourCity = yourAddresses.get(0).getAddressLine(1);
					yourCountry = yourAddresses.get(0).getAddressLine(2);
				}

				sms.sendTextMessage(senderTel, null,"The person is at "+yourAddress+"  "+yourCity+"  "+yourCountry+" ,To Follow on map :- "+"http://maps.google.com/maps?q="+location.getLatitude()+","+location.getLongitude(), null, null);



				//sms.sendTextMessage(senderTel, null, "Sorry !!", null, null);

			}
			if (ActivityCompat.checkSelfPermission(context12, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context12, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling

				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.












				lm.removeUpdates(locationListener);




				this.abortBroadcast();
			}


		}
	}



	private class MyLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {


			}



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
