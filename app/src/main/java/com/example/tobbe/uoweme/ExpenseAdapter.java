package com.example.tobbe.uoweme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by TobiasOlsson on 15-05-17.
 */
public class ExpenseAdapter extends BaseAdapter {


    private static String LOG = "ExpenseAdapter";

    private static ArrayList<Expense> expenses;
    private Context context;
    private static LayoutInflater inflater = null;

    public ExpenseAdapter(Context context, ArrayList<Expense> groupArrayList) {
        this.context = context;
        expenses = groupArrayList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return expenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null)
        {
            vi = inflater.inflate(R.layout.item_expense_list, null);
        }
        TextView title = (TextView) vi.findViewById(R.id.expenseTitle);
        TextView amount = (TextView) vi.findViewById(R.id.expenseAmount);
        TextView owner = (TextView) vi.findViewById(R.id.expenseOwner);
        Expense newExpense = expenses.get(position);
        title.setText(newExpense.getTitle());
        amount.setText("Cash: " +newExpense.getAmount());
        owner.setText("Id: " +newExpense.getOwnerId());
        return vi;
    }

    public void addExpense(Expense addExpense){
        expenses.add(addExpense);
        notifyDataSetChanged();
    }
}
