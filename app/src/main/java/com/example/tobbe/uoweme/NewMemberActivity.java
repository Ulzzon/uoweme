package com.example.tobbe.uoweme;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Tobbe on 2015-03-14.
 */
public class NewMemberActivity extends Activity implements View.OnClickListener{

    private static Context mContext;
    EditText nameText;
    EditText phoneText;
    Button createButton;
    Button cancelButton;
    boolean click = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);

        mContext = getBaseContext();

        createButton = (Button) findViewById(R.id.createMemberButton);
        createButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);

        nameText = (EditText) findViewById(R.id.nameText);
        nameText.setOnClickListener(this);

        phoneText = (EditText) findViewById(R.id.phoneText);
        phoneText.setOnClickListener(this);
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
            case R.id.nameText:
                nameText.setText("");
                break;
            case R.id.phoneText:
                phoneText.setText("");
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
