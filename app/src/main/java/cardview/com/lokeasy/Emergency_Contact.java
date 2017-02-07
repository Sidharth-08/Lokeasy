package cardview.com.lokeasy;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sidharth on 03-Feb-17.
 */

public class Emergency_Contact extends AppCompatActivity {
    private static final int CONTACT_PICKER_RESULT = 1001;
    SimpleCursorAdapter mAdapter;
    ListView mListView;
    View empty;
    ContentResolver cr;
    TextView textView;
    int count=0;
    private static final Uri CONTENT_URI = Uri.parse("content://cardview.com.lokeasy/contacts");
    String[] columns = new String[] {EmergencyContact._ID, EmergencyContact.NAME, EmergencyContact.NUMBER};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contact);




        mListView = (ListView) findViewById(R.id.mobile_list);

        empty = findViewById(R.id.empty);
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(CONTENT_URI, columns, null, null, null);

        mAdapter = new SimpleCursorAdapter(this,
                R.layout.emergency_contact_listview,
                cursor,
                new String[]{EmergencyContact.NAME, EmergencyContact.NUMBER},
                new int[]{R.id.ContactName, R.id.ContactNumber}, 0);
        if(mAdapter.getCount()==0)
        {
            mListView.setEmptyView(empty);
        }
        else
        {
            empty.setVisibility(View.INVISIBLE);
            mListView.setAdapter(mAdapter);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {



                textView = ((TextView) v.findViewById(R.id.ContactName));
                cr = getContentResolver();
                count--;
                if(count!=0)
                {

                    AlertDialog alert = new AlertDialog.Builder(Emergency_Contact.this).create();
                    alert.setTitle("Delete");
                    alert.setMessage("Do you want to delete this emergency contact?");
                    alert.setButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            cr.delete(EmergencyContact.CONTENT_URI, "NAME = ?", new String[]{"" + textView.getText().toString()});

                            finish();
                            startActivity(getIntent());
                        }
                    });

                    alert.show();
                    onClickRetrieveStudents(v);

                }
                else if(count==0)
                {



                    AlertDialog alert = new AlertDialog.Builder(Emergency_Contact.this).create();
                    alert.setTitle("Delete");
                    alert.setMessage("Do you want to delete this emergency contact?");
                    alert.setButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            mListView.setAdapter(null);
                            mListView.setEmptyView(empty);
                            cr.delete(EmergencyContact.CONTENT_URI, "NAME = ?", new String[]{"" + textView.getText().toString()});

                            finish();
                            startActivity(getIntent());
                        }
                    });

                    alert.show();
                    onClickRetrieveStudents(v);

                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.emergency_contact_menu, menu);
        return true;
    }



    public void onClickRetrieveStudents(View view) {

        // Retrieve student records
        String URL = "content://cardview.com.lokeasy/contacts";

        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "name");

        if (c.moveToFirst()) {
            do {
                //        Toast.makeText(this,
                //              c.getString(c.getColumnIndex(EmergencyContact._ID)) +
                //                    ", " + c.getString(c.getColumnIndex(EmergencyContact.NAME)) +
                //                  ", " + c.getString(c.getColumnIndex(EmergencyContact.NUMBER)),
                //        Toast.LENGTH_SHORT).show();


                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(CONTENT_URI, columns, null, null, null);
                mAdapter = new SimpleCursorAdapter(this,
                        R.layout.emergency_contact_listview,
                        cursor,
                        new String[] { EmergencyContact.NAME, EmergencyContact.NUMBER},
                        new int[] { R.id.ContactName , R.id.ContactNumber }, 0);

            } while (c.moveToNext());

            if(count==0)
            {

                mListView.setEmptyView(empty);
            }
            else {

                mListView.setAdapter(mAdapter);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home1) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == RESULT_OK && null != data) {
            Uri contactUri = data.getData();

            Cursor c = managedQuery(contactUri, null, null, null, null);
            if (c.moveToFirst()) {
                String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);
if(phones.moveToFirst())

                    //Toast.makeText(getApplicationContext(),name+"  "+ContactsContract.CommonDataKinds.P,Toast.LENGTH_SHORT).show();

{
    String phoneNumber = phones.getString(phones.getColumnIndex("data1")).toString();


    // Add a new  record
    ContentValues values = new ContentValues();

    values.put(EmergencyContact.NAME, name);

    values.put(EmergencyContact.NUMBER, phoneNumber);

    Uri uri = getContentResolver().insert(
            EmergencyContact.CONTENT_URI, values);

    Toast.makeText(getBaseContext(),
            name + " with phone number " + phoneNumber + " is added as an Emergency Contact", Toast.LENGTH_LONG).show();

    count++;
    // }

    onClickRetrieveStudents(mListView);
}

                }
                else
                {
                    Toast.makeText(getBaseContext(),
                            name+" has no phone number in record.", Toast.LENGTH_LONG).show();

                }
            }
        }
    }


    }
