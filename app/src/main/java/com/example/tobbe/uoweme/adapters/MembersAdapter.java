package com.example.tobbe.uoweme.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
            CalculateExpenses calculator = new CalculateExpenses();
            int debt = calculator.calculateIndividualTotal(member, expenses);
            /*if(debt < 0){
                expenseView.setTextColor(Color.RED);
            }else{
                expenseView.setTextColor(Color.GREEN);
            }
            expenseView.setText("Expense: " + debt);
            */
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
        return vi;
    }

}
