package com.example.tobbe.uoweme.adapters;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.tobbe.uoweme.Expense;
import com.example.tobbe.uoweme.R;

public class ExpenseExpandAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> expenseDetails;
    private ArrayList<Expense> expenses;

    public ExpenseExpandAdapter(Activity context, ArrayList<Expense> expenses,
                                 Map<String, List<String>> expenseDetails) {
        this.context = context;
        this.expenseDetails = expenseDetails;
        this.expenses = expenses;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return expenseDetails.get(expenses.get(groupPosition).getTitle()).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String details = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expense_detail_list, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.expenseDetails);

        //ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        /*delete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =
                                        expenseDetails.get(expenses.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
*/
        item.setText(details);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return expenseDetails.get(expenses.get(groupPosition).getTitle()).size();
    }

    public Expense getGroup(int groupPosition) {
        return expenses.get(groupPosition);
    }

    public int getGroupCount() {
        return expenses.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Expense expense = getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflateInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflateInflater.inflate(R.layout.item_expense_list,
                    null);
        }
        String expenseTitle = expense.getTitle();
        TextView expenseTitleText = (TextView) convertView.findViewById(R.id.expenseTitle);
        expenseTitleText.setTypeface(null, Typeface.BOLD);
        expenseTitleText.setText(expenseTitle);
        long amount = expense.getAmount();
        TextView expenseAmountText = (TextView) convertView.findViewById(R.id.expenseAmount);
        expenseAmountText.setText("Expense: " + amount);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}