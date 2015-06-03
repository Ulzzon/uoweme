package com.example.tobbe.uoweme;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.beans.IndexedPropertyChangeEvent;


public class NewExpenseActivity extends Activity implements View.OnClickListener{

    private final String LOG = "NewExpenseActivity";//getLocalClassName();
    private EditText titleText;
    private EditText amountText;
    private Button createButton;
    private Button cancelButton;
    private int groupId =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        groupId = getIntent().getIntExtra(getString(R.string.group_number), 1);

        titleText = (EditText) findViewById(R.id.expenseTitleText);
        titleText.setOnClickListener(this);

        amountText = (EditText) findViewById(R.id.expenseAmountText);
        amountText.setOnClickListener(this);

        createButton = (Button) findViewById(R.id.createExpenseButton);
        createButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
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
                createNewExpense();
                this.finish();
                break;
            case(R.id.cancelButton):
                this.finish();
                break;
        }
    }

    private void createNewExpense(){
        Expense addingExpense = new Expense();
        try {
            addingExpense.setAmount(Integer.parseInt(amountText.getText().toString()));
        } catch (Exception e){
            Log.d(LOG, "Failed to parse Integer");
        }
        addingExpense.setTitle(titleText.getText().toString());
        addingExpense.setOwnerId(1);
        ExpenseGroup group = GroupAdapter.getExpenseGroup(groupId);
        group.addExpense(addingExpense);
    }
}
