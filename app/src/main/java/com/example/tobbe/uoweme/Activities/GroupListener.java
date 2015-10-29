package com.example.tobbe.uoweme.Activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.example.tobbe.uoweme.Activities.MainActivity;
import com.example.tobbe.uoweme.ExpenseGroup;
import com.example.tobbe.uoweme.NavigationDrawerFragment;
import com.example.tobbe.uoweme.R;
import com.example.tobbe.uoweme.adapters.GroupAdapter;

/**
 * Created by TobiasOlsson on 15-03-21.
 */
public class GroupListener implements View.OnClickListener {
    private final Context context;
    private View rootView;


    GroupListener(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    GroupListener(Context context) {
        this.context = context;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addMember:
                Intent startIntent = new Intent(context.getString(R.string.new_member_intent));
                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startIntent);

                break;
            case R.id.createGroupButton:
                EditText editTitle = (EditText) rootView.findViewById(R.id.titleText);
                EditText editDescription = (EditText) rootView.findViewById(R.id.descriptionText);
                Button createButton = (Button) v;
                ExpenseGroup activeGroup = MainActivity.GroupSettingsFragment.getActiveGroup();
                activeGroup.setTitle(editTitle.getText().toString());
                activeGroup.setDescription(editDescription.getText().toString());
                if (createButton.getText().toString().equals(context.getString(R.string.save_group_changes))) {//MainActivity.mDbHelper.updateDbRow(MainActivity.GroupSettingsFragment.getActiveGroup());
                    MainActivity.db.updateGroup(MainActivity.GroupSettingsFragment.getActiveGroup());
                } else {

                    NavigationDrawerFragment.mGroupAdapter.addGroup(activeGroup);
                }
                break;
            case R.id.titleText:
                EditText titleText = (EditText) v.findViewById(R.id.titleText);
                if (titleText.getText().equals(context.getString(R.string.default_title))) {
                    titleText.setText("");
                }

                break;
            case R.id.descriptionText:
                EditText descriptionText = (EditText) v.findViewById(R.id.descriptionText);
                if (descriptionText.getText().equals(context.getString(R.string.default_description))) {
                    descriptionText.setText("");
                }
                break;

        }
    }

    public static class GroupLongListener implements AdapterView.OnItemLongClickListener {


        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ExpenseGroup activeGroup = GroupAdapter.getExpenseGroup(MainActivity.activeGroupId);
            activeGroup.deleteAllMembers();
            activeGroup.deleteAllExpenses();
            return true;
        }

    }
}
