package com.example.tobbe.uoweme.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tobbe.uoweme.Activities.MainActivity;
import com.example.tobbe.uoweme.Expense;
import com.example.tobbe.uoweme.Person;
import com.example.tobbe.uoweme.R;

import java.util.ArrayList;

import helper.CalculateExpenses;

/**
 * Created by TobiasOlsson on 15-05-17.
 */
public class MembersAdapter extends BaseAdapter {


    private final Context context;
    private ArrayList<Person> myPersons;
    private LayoutInflater inflater = null;
    private ArrayList<Expense> expenses;

    public MembersAdapter(Context context, ArrayList<Person> persons){

        this.context = context;
        myPersons = persons;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MembersAdapter(Context context, ArrayList<Person> persons, ArrayList<Expense> expenses){

        this.context = context;
        myPersons = persons;
        this.expenses = expenses;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myPersons.size();
    }

    @Override
    public Object getItem(int position) {
        return myPersons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myPersons.get(position).getDbId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (vi == null)
        {
            vi = inflater.inflate(R.layout.item_member_list, null);
        }
        Person member = myPersons.get(position);
        TextView nameView = (TextView) vi.findViewById(R.id.memberName);
        nameView.setText(member.getName());
        TextView phoneNrView = (TextView) vi.findViewById(R.id.memberPhoneNr);
        phoneNrView.setText(member.getNumber());
        TextView expenseView = (TextView) vi.findViewById(R.id.membersExpense);

        if(expenses != null) {
            int debt = CalculateExpenses.getInstance().calculateIndividualTotal(member, expenses);
            if( debt < 0){
                expenseView.setText(Html.fromHtml("<font color=#000000>Expense: </font> <font color=#ff0000>" + debt + "</font>"));
            }
            else if( debt > 0){
                expenseView.setText(Html.fromHtml("<font color=#000000>Expense: </font> <font color=#009933>" + debt + "</font>"));
            }
            else {
                expenseView.setText("Expense: " + debt);
            }
        }else {
            expenseView.setText("Expense: -443");
        }

        ImageButton addExpenseButton = (ImageButton) vi.findViewById(R.id.addExpenseButton);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent expenseActivityIntent = new Intent(context.getString(R.string.new_expense_intent));
                expenseActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                expenseActivityIntent.putExtra(context.getString(R.string.group_number), MainActivity.activeGroupId);
                expenseActivityIntent.putExtra(context.getString(R.string.member_db_id), getItemId(position));
                context.startActivity(expenseActivityIntent);
            }
        });

        return vi;
    }

}
