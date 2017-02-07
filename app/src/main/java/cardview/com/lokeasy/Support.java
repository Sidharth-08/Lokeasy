package cardview.com.lokeasy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.net.UnknownHostException;

import cardview.com.lokeasy.cardview.com.lokeasy.database.MySupport;
import cardview.com.lokeasy.cardview.com.lokeasy.database.SaveAsyncTask1;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class Support extends Fragment {

    View rootView;
    EditText name, emailid, subject, issue;
    Button submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_support, container, false);
        Button Btn = (Button) getActivity().findViewById(R.id.findPlace);
        Btn.setVisibility(View.INVISIBLE);
        name = (EditText)rootView.findViewById(R.id.name);
        emailid = (EditText)rootView.findViewById(R.id.email);
        subject = (EditText)rootView.findViewById(R.id.subject);
        issue= (EditText)rootView.findViewById(R.id.issue);
        submit = (Button)rootView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name= name.getText().toString();
                String Email= emailid.getText().toString();
                String Subject= subject.getText().toString();
                String Issue= issue.getText().toString();


                try {
                    saveSupport(view);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                dimBackground();
                LayoutInflater layoutInflater= (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.support_popup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }});


                popupWindow.setOutsideTouchable(false);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Intent intent=new Intent(getContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);



            }
        });


        return rootView;
    }

    public void saveSupport(View v) throws UnknownHostException {

        MySupport support = new MySupport();
        support.name = name.getText().toString();
        support.email = emailid.getText().toString();
        support.subject = subject.getText().toString();
        support.issue = issue.getText().toString();

        SaveAsyncTask1 tsk = new SaveAsyncTask1();
        tsk.execute(support);


    }
    private PopupWindow dimBackground() {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fadepopup,null);

        PopupWindow fadePopup = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        fadePopup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 0);
        return fadePopup;
    }
}