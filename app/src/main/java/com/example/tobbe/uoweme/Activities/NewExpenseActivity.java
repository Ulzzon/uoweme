package com.example.tobbe.uoweme.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tobbe.uoweme.Expense;
import com.example.tobbe.uoweme.ExpenseGroup;
import com.example.tobbe.uoweme.Person;
import com.example.tobbe.uoweme.R;
import com.example.tobbe.uoweme.adapters.GroupAdapter;

import java.util.ArrayList;


public class NewExpenseActivity extends Activity implements View.OnClickListener{

    private final String LOG = "NewExpenseActivity";//getLocalClassName();
    private EditText titleText;
    private EditText amountText;
    private Button createButton;
    private Button cancelButton;
    private ListView listAllMembers;
    private int groupId =1;
    private long memberId;
    private ExpenseGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        groupId = getIntent().getIntExtra(getString(R.string.group_number), 1);
        memberId = getIntent().getLongExtra(getString(R.string.member_db_id), 1);
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
            return false;
        }
        addingExpense.setTitle(titleText.getText().toString());
        addingExpense.setOwnerId(memberId);

        SparseBooleanArray checked = listAllMembers.getCheckedItemPositions();
        if(checked.size() == 0){
            return false;
        }
        long[] membersDbIds = new long[checked.size()];
        ArrayList<Person> members = group.getMembers();

        for(int k = 0; k < checked.size(); k++){
            membersDbIds[k] = members.get(checked.keyAt(k)).getDbId();
        }

        addingExpense.setAffectedMembersIds(membersDbIds);

        group.addExpense(addingExpense);
        return true;
    }
}
