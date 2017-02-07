package cardview.com.lokeasy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Sidharth on 06-Feb-17.
 */

public class About extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_about, container, false);
        Button Btn = (Button) getActivity().findViewById(R.id.findPlace);
        Btn.setVisibility(View.INVISIBLE);

        return rootView;
    }
}