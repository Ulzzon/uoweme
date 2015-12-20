package com.example.tobbe.uoweme.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.tobbe.uoweme.Activities.MainActivity;
import com.example.tobbe.uoweme.ExpenseGroup;
import com.example.tobbe.uoweme.Person;
import com.example.tobbe.uoweme.R;

import helper.Communicator;

/**
 * Created by Tobbe on 2015-03-14.
 */
public class NewMemberActivity extends Activity implements View.OnClickListener{

    private static Context mContext;
    EditText nameText;
    EditText phoneText;
    Button createButton;
    Button cancelButton;
    ImageButton contactButton;
    private final int PICK_CONTACT = 70;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);

        mContext = getBaseContext();

        createButton = (Button) findViewById(R.id.createMemberButton);
        createButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);

        contactButton = (ImageButton) findViewById(R.id.addFromContacts);
        contactButton.setOnClickListener(this);

        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createMemberButton:
                createNewMember();
                this.finish();
                break;
            case R.id.cancelButton:
                this.finish();
                break;
            case R.id.addFromContacts:
                readContact();
                break;
        }
    }

    private void readContact(){
        try {
            //Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts//people"));
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, PICK_CONTACT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_CONTACT:
                if(resultCode == Activity.RESULT_OK){
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null,null,null);
                    //Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if(c.moveToFirst()){
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if(hasPhone.equalsIgnoreCase("1")){
                            //Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=" + id, null, null);
                            //phones.moveToFirst();
                            //String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            int numberColumn = c.getColumnIndex(Phone.NUMBER);
                            String phoneNumber = c.getString(numberColumn);
                            phoneText.setText(phoneNumber);
                            //phones.close();
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME));
                        nameText.setText(name);
                        c.close();

                    }
                    Log.d("NewMemberActivity", "Got a good result from caontacts");
                }
                break;
        }
    }

    private void createNewMember(){
        String name = nameText.getText().toString();
        String phoneNr = phoneText.getText().toString();
        Person person = new Person(name, phoneNr);
        ExpenseGroup group = MainActivity.GroupSettingsFragment.getActiveGroup();
        group.addMember(person);
        Log.d("MemberActivity", "Adding new member, " + person.getName() + " to group");

    }

    public static Context getContext(){
        return mContext;
    }
}
