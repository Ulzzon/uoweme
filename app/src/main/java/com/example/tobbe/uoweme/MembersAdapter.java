package com.example.tobbe.uoweme;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TobiasOlsson on 15-05-17.
 */
public class MembersAdapter extends BaseAdapter {


    private final Context context;
    private ArrayList<Person> myPersons;
    private LayoutInflater inflater = null;

    MembersAdapter(Context context, ArrayList<Person> persons){

        this.context = context;
        myPersons = persons;

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
        expenseView.setTextColor(Color.RED);
        expenseView.setText("Expense: -340");

        return vi;
    }
}
