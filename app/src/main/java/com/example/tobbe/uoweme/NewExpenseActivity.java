package com.example.tobbe.uoweme;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.beans.IndexedPropertyChangeEvent;
import java.util.ArrayList;


public class NewExpenseActivity extends Activity implements View.OnClickListener{

    private final String LOG = "NewExpenseActivity";//getLocalClassName();
    private EditText titleText;
    private EditText amountText;
    private Button createButton;
    private Button cancelButton;
    private ListView listAllMembers;
    private int groupId =1;
    private ExpenseGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        groupId = getIntent().getIntExtra(getString(R.string.group_number), 1);
        group = GroupAdapter.getExpenseGroup(groupId);

        titleText = (EditText) findViewById(R.id.expenseTitleText);
        titleText.setOnClickListener(this);

        amountText = (EditText) findViewById(R.id.expenseAmountText);
        amountText.setOnClickListener(this);

        createButton = (Button) findViewById(R.id.createExpenseButton);
        createButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);

        ArrayList<String> allNames = new ArrayList<>();
        ArrayList<Person> allMembers = group.getMembers();
        for(Person p : allMembers){
            allNames.add(p.getName());
        }
        listAllMembers = (ListView) findViewById(R.id.listGroupMembers);
        ArrayAdapter<String> membersAdapter = new ArrayAdapter<String>(getBaseContext(),
                R.layout.item_member_list,
                R.id.memberName,
                allNames);
        listAllMembers.setAdapter(membersAdapter);


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.expenseTitle):
                titleText.setText("");
                break;
            case(R.id.expenseAmount):
                amountText.setText("");
                break;
            case(R.id.createExpenseButton):
                if(createNewExpense()){
                    this.finish();
                }
                else{
                    Toast.makeText(getBaseContext(), "Need to select effected members!", Toast.LENGTH_SHORT).show();
                }

                break;
            case(R.id.cancelButton):
                this.finish();
                break;
        }
    }

    private boolean createNewExpense(){
        Expense addingExpense = new Expense();
        try {
            addingExpense.setAmount(Integer.parseInt(amountText.getText().toString()));
        } catch (Exception e){
            Log.d(LOG, "Failed to parse Integer");
        }
        addingExpense.setTitle(titleText.getText().toString());
        addingExpense.setOwnerId(1);

        boolean noneSelected = true;
        SparseBooleanArray checked = listAllMembers.getCheckedItemPositions();
        long[] membersDbIds = new long[checked.size()];
        ArrayList<Person> members = group.getMembers();
        for(int i = 0; i < checked.size(); i++){
            if(checked.get(i)){
                membersDbIds[i] = members.get(i).getDbId();
                noneSelected = false;
            }
        }
        if(noneSelected){
            return false;
        }
        addingExpense.setAffectedMembersIds(membersDbIds);

        group.addExpense(addingExpense);
        return true;
    }
}
