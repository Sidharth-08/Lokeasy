package cardview.com.lokeasy;

import android.*;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sidharth on 31-Jan-17.
 */

public class StoredAddresses extends Fragment  {

    View rootView;
    String yourAddress ="";
    String yourCity = "";
    String yourCountry = "";
    private static final String DATABASE_TABLE = "locations";
    ArrayAdapter<String> itemsAdapter;
    ListView listView;
    private SimpleCursorAdapter adapter;
    ArrayList<String> arrayList;
    public static final String FIELD_ROW_ID = "_id";

    /** Field 2 of the table locations, stores the latitude */
    public static final String FIELD_LAT = "lat";

    /** Field 3 of the table locations, stores the longitude*/
    public static final String FIELD_LNG = "lng";

    private static final Uri CONTENT_URI = Uri.parse("content://cardview.com.lokeasy.locations/locations");
    String[] columns = new String[] {LocationsDB.FIELD_LAT.toString(),LocationsDB.FIELD_LNG.toString()};

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.storedaddresses, container, false);
        listView=(ListView)rootView.findViewById(R.id.list);
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(CONTENT_URI, columns, null, null, null);




        arrayList = new ArrayList();
        double lat=0;
        double lng=0;
        cursor.moveToFirst();

        for(int i=0;i<cursor.getCount();i++)
        {


            lat = cursor.getDouble(cursor.getColumnIndex(LocationsDB.FIELD_LAT));

            // Get the longitude
            lng = cursor.getDouble(cursor.getColumnIndex(LocationsDB.FIELD_LNG));



            List<Address> yourAddresses =null;
            try {
                Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
                yourAddresses = geocoder.getFromLocation(lat,lng, 10);
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

            arrayList.add(yourAddress+","+yourCity+","+yourCountry);
            cursor.moveToNext();
        }







        String array[] = new String[arrayList.size()];
        for(int j =0;j<arrayList.size();j++){
            array[j] = arrayList.get(j);
        }

 /*       adapter = new SimpleCursorAdapter(getContext(),
                R.layout.storedaddress_item,
                cursor,
                array,
                new int[] { R.id.Addresslat  },0);
/*
        adapter = new ArrayAdapter<String>(this,
                R.layout.storedaddress_item, R.id.Addresslat,
                array.toString());
*/


        if(!arrayList.isEmpty()) {
            itemsAdapter =

                    new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, array);


        }

        Button Btn = (Button) getActivity().findViewById(R.id.findPlace);
        Btn.setVisibility(View.INVISIBLE);





        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
           /*     LatLng loc=getLocationFromAddress(getContext(),item);


///////As of now this feature is officially not working



                LocationsDB mL=new LocationsDB(getContext());

                mL.getWritableDatabase();

                        mL.deleteTitle(loc.latitude,loc.longitude);
*/


            }});


                return rootView;

    }



    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

}
