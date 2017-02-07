package cardview.com.lokeasy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sidharth on 03-Apr-16.
 */
public class Sms_Tracker extends android.support.v4.app.Fragment {

    View rootView;
    Button b1;
    MainActivity mainActivity=new MainActivity();
    TextView msgkey;
    Sms_Tracker sms_tracker;
    String s1="";
    String yourAddress = "";
    String yourCity = "";
    String yourCountry ="";
    InternalStorage storage;
    Button newDesigns;
    String key="";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sms_tracker, container, false);

        storage=new InternalStorage();
        Button Btn = (Button) getActivity().findViewById(R.id.findPlace);
        Btn.setVisibility(View.INVISIBLE);
        msgkey=(TextView)rootView.findViewById(R.id.messagekey);
        b1=(Button)rootView.findViewById(R.id.regen);

        try {
            key = (String) storage.readObject(getContext(), "key");
        }catch(Exception e)
        {}


        if(!key.equals(""))
        {
            msgkey.setText(key);
        }



        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                s1=GetRandomString(4);
                msgkey.setText(s1);


                try{
                storage.writeObject(getContext(),"key",s1);
                }catch (Exception e){}


              // TODO Auto-generated method stub
                SharedPreferences myPrefs = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor e = myPrefs.edit();
                e.putString("someValue", s1); // add or overwrite someValue
                e.commit(); // this saves to disk and notifies observers


                Toast.makeText(getContext(), "Key Generated", Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;

    }


    public static String GetRandomString(int maxlength)
    {
        String result = "";
        int i = 0, n = 0, min = 33, max = 122;
        while(i < maxlength)
        {
            n = (int)(Math.random() * (max - min) + min);
            if(n >= 33 && n < 123)
            {
                result += (char)n;
                ++i;
            }
        }
        return(result);
    }



}
