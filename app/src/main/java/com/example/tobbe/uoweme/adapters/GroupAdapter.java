package com.example.tobbe.uoweme.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tobbe.uoweme.ExpenseGroup;
import com.example.tobbe.uoweme.MainActivity;
import com.example.tobbe.uoweme.R;

import java.util.ArrayList;

/**
 * Created by Tobbe on 2015-03-14.
 */
public class GroupAdapter extends BaseAdapter{

/*    private ArrayList<String> titleArray = new ArrayList<String>();
    private ArrayList<String> descriptionArray = new ArrayList<String>();*/
    Context context;

    private static String LOG = "GroupAdapter";

    private static ArrayList<ExpenseGroup> myGroups;

    public static ExpenseGroup getExpenseGroup(int position){
        if(myGroups != null && myGroups.size() > position) {
            return myGroups.get(position);
        }else throw new ArrayIndexOutOfBoundsException(position);
    }

    private ExpenseGroup newGroup;

    private static LayoutInflater inflater = null;

    public GroupAdapter(Context context){

        this.context = context;
        myGroups = new ArrayList<>();

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GroupAdapter(Context context, String[] title, String[] description) {

        this.context = context;
        myGroups = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
           /* titleArray.add(title[i]);
            descriptionArray.add(description[i]);*/
            newGroup = new ExpenseGroup(title[i], description[i]);
            myGroups.add(newGroup);
        }
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GroupAdapter(Context context, ExpenseGroup expense){
        this.context = context;
        myGroups = new ArrayList<>();
        myGroups.add(expense);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public GroupAdapter(Context context, ArrayList<ExpenseGroup> groupArrayList){
        this.context = context;
        myGroups = groupArrayList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return myGroups.get(position);
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
            vi = inflater.inflate(R.layout.item_group_list, null);
        }
        TextView title = (TextView) vi.findViewById(R.id.groupTitle);
        TextView desc = (TextView) vi.findViewById(R.id.groupDescription);
        ExpenseGroup newGroup = myGroups.get(position);
        title.setText(newGroup.getTitle());
        desc.setText(newGroup.getDescription());
        return vi;
    }

    public void addGroup(String title, String description){
        addGroup(new ExpenseGroup(title, description));
//        myGroups.add(new ExpenseGroup(title, description));
    }
    public void addGroup(ExpenseGroup expenseGroup){
        //long rowId = MainActivity.mDbHelper.saveGroupsToDb(expenseGroup);
        //expenseGroup.setDbID(rowId);
        Log.d(LOG, "Add group: " +expenseGroup.getTitle());
        MainActivity.db.saveGroupToDb(expenseGroup);
        myGroups.add(expenseGroup);
        notifyDataSetChanged();
    }

    public ExpenseGroup getGroup(int position){
        return myGroups.get(position);
    }

    public void removeGroup(int position){
        ExpenseGroup groupToDelete = myGroups.get(position);
        Log.d(LOG, "Delete Group: " + groupToDelete.getTitle() + " DbId: " + groupToDelete.getDbID());
        // Remove expenses that is connected to this group
        groupToDelete.deleteAllExpenses();
        groupToDelete.deleteAllMembers();
        myGroups.remove(position);
        MainActivity.db.deleteGroup(groupToDelete.getDbID());
        notifyDataSetChanged();
    }

    public static int getNumberOfGroups(){
        return myGroups.size();
    }

}
