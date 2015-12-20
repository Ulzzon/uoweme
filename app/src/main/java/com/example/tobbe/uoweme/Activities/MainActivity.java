package com.example.tobbe.uoweme.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tobbe.uoweme.Expense;
import com.example.tobbe.uoweme.ExpenseGroup;
import com.example.tobbe.uoweme.NavigationDrawerFragment;
import com.example.tobbe.uoweme.Person;
import com.example.tobbe.uoweme.R;
import com.example.tobbe.uoweme.adapters.ExpenseExpandAdapter;
import com.example.tobbe.uoweme.adapters.GroupAdapter;
import com.example.tobbe.uoweme.adapters.MembersAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import helper.Communicator;
import helper.DatabaseHelper;
import android.provider.Settings.Secure;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private static CharSequence mTitle;
    public static int activeGroupId = 0;
    public static DatabaseHelper db;

    public static android.support.v4.app.FragmentManager fragmentManager;



    public static String android_id;

/*    public static Socket connectionSocket;
    {
        try{
            connectionSocket = IO.socket("http://192.168.1.246:8888");
            Log.d("Communicator","Setting up socket");
        }catch (Exception e){
            Log.d("Socket", "ERROR: " +e.toString());
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getBaseContext());
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        fragmentManager = getSupportFragmentManager();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Communicator.startCommunication();
 /*       connectionSocket.on("hand_shake", handShakeListener);
        if(connectionSocket != null){
            connectionSocket.connect();
        }*/

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, GroupViewFragment.newInstance(position))
                .commit();
    }

/*    private Emitter.Listener handShakeListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        connectionSocket.emit("hand_shake_ok", "hello");
                    }catch (Exception e){
                        Log.d("emit", "Handshake failed");
                        return;
                    }

                }
            });
//            J

            //int numUsers;
            //try {
                //numUsers = data.getInt("numUsers");
            //} catch (JSONException e) {
            //    return;
            //}

            //Intent intent = new Intent();
            //intent.putExtra("handshake", "ok");
            //intent.putExtra("username", mUsername);
            //intent.putExtra("numUsers", numUsers);
            //setResult(RESULT_OK, intent);
            //finish();
        }
    };*/

    @Override
    public void onDestroy(){
        db.updateGroup(GroupAdapter.getExpenseGroup(activeGroupId));
        super.onDestroy();
        Communicator.killConnection();

    }

    public void onSectionAttached(String title) {
        mTitle = title;
        restoreActionBar();
    }

    public void onSectionAttached(int number) {
        try {
            mTitle = GroupAdapter.getExpenseGroup(number).getTitle();
            activeGroupId = number;
        } catch (Exception e) {
            mTitle = "NoTitle";
            Log.d("SectionAttached", "No group found ");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFragment.newInstance())
                    .commit();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int group = activeGroupId;
        fragmentManager = getSupportFragmentManager();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, GroupSettingsFragment.newInstance(group))
                    .commit();

            return true;
        }else if(id == R.id.menu_profile){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFragment.newInstance())
                    .commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void showNewGroupFragment() {
        int position = GroupAdapter.getNumberOfGroups() + 1;

        fragmentManager.beginTransaction()
                .replace(R.id.container, GroupSettingsFragment.newInstance(position))
                .commit();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class GroupViewFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private String LOG = "GroupViewFragment";
        private ExpenseGroup activeExpenseGroup;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_TITLE = "section_title";
        private final int expenseResultCode = 90;
        private ExpandableListView eExpenseList;
        private ListView membersList;


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GroupViewFragment newInstance(int sectionNumber) {
            GroupViewFragment fragment = new GroupViewFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_TITLE, "HI");
            fragment.setArguments(args);
            return fragment;
        }

        public GroupViewFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_group_main, container, false);
            TextView description = (TextView) rootView.findViewById(R.id.section_label);
            activeExpenseGroup = new ExpenseGroup();
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            try {
                activeExpenseGroup = GroupAdapter.getExpenseGroup(position);
                description.setText(activeExpenseGroup.getDescription());
            }catch(Exception e){
                Log.d(LOG,"Not able to grab group"  );
            }

            membersList = (ListView) rootView.findViewById(R.id.memberList);
            eExpenseList = (ExpandableListView) rootView.findViewById(R.id.eExpenseList);
            loadGroupData();
/*
            ArrayList<String> listOfMembers = new ArrayList<>();
            for(Person p : activeExpenseGroup.getMembers()){
                listOfMembers.add(p.getName());
            }
            ListView membersList = (ListView) rootView.findViewById(R.id.memberList);

            MembersAdapter membersAdapter = new MembersAdapter(getActivity().getBaseContext(),
                    activeExpenseGroup.getMembers(),
                    activeExpenseGroup.getExpenses());
            membersList.setAdapter(membersAdapter);

            ExpandableListView eExpenseList = (ExpandableListView) rootView.findViewById(R.id.eExpenseList);

            LinkedHashMap<Long, List<String>> mappedDetails = new LinkedHashMap<>();

            ArrayList<String> affectedNames;
            ArrayList<Expense> expenses = activeExpenseGroup.getExpenses();
            for(Expense e : expenses){
                affectedNames = new ArrayList<>();
                for(long aId : e.getAffectedMembersIds()){
                    Person member = activeExpenseGroup.getMemberById(aId);
                    if(member != null) {
                        affectedNames.add(member.getName());
                    }
                }
                mappedDetails.put(e.getDbId(), affectedNames);
            }

            ExpenseExpandAdapter expenseAdapter = new ExpenseExpandAdapter(getActivity(),
                    expenses,
                    mappedDetails
                    );

            eExpenseList.setAdapter(expenseAdapter);
*/
            Button suggestPaymentButton = (Button) rootView.findViewById(R.id.addExpenseButton);

            suggestPaymentButton.setText("Payments");
            suggestPaymentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent paymentActivityIntent = new Intent(getActivity().getBaseContext().getString(R.string.new_payment_intent));
                    paymentActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    paymentActivityIntent.putExtra(getString(R.string.group_number), activeGroupId);
                    startActivityForResult(paymentActivityIntent, expenseResultCode);

                }
            });
            TextView totalGroupExpenses = (TextView) rootView.findViewById(R.id.totalExpenseTextView);
            //totalGroupExpenses.setText("Groups Total: " + expenseAdapter.getTotalExpenses() + "kr");

            return rootView;
        }

        public void loadGroupData(){
            ArrayList<String> listOfMembers = new ArrayList<>();
            for(Person p : activeExpenseGroup.getMembers()){
                listOfMembers.add(p.getName());
            }


            MembersAdapter membersAdapter = new MembersAdapter(getActivity().getBaseContext(),
                    activeExpenseGroup.getMembers(),
                    activeExpenseGroup.getExpenses());
            membersList.setAdapter(membersAdapter);



            LinkedHashMap<Long, List<String>> mappedDetails = new LinkedHashMap<>();

            ArrayList<String> affectedNames;
            ArrayList<Expense> expenses = activeExpenseGroup.getExpenses();
            for(Expense e : expenses){
                affectedNames = new ArrayList<>();
                for(long aId : e.getAffectedMembersIds()){
                    Person member = activeExpenseGroup.getMemberById(aId);
                    if(member != null) {
                        affectedNames.add(member.getName());
                    }
                }
                mappedDetails.put(e.getDbId(), affectedNames);
            }

            ExpenseExpandAdapter expenseAdapter = new ExpenseExpandAdapter(getActivity(),
                    expenses,
                    mappedDetails
            );

            eExpenseList.setAdapter(expenseAdapter);
        }

        @Override   //TODO: Remove if no use for this code is found
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == expenseResultCode){
                loadGroupData();
            }
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /* ##### Settings for group ##### */

    public static class GroupSettingsFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_TITLE = "section_title";
        private int groupNumber;
        private static ExpenseGroup activeGroup;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GroupSettingsFragment newInstance(int sectionNumber) {
            GroupSettingsFragment fragment = new GroupSettingsFragment();
            Bundle args = new Bundle();
            if(sectionNumber <= GroupAdapter.getNumberOfGroups()){
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                args.putString(ARG_SECTION_TITLE, GroupAdapter.getExpenseGroup(sectionNumber).getTitle());
            }else{
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                args.putString(ARG_SECTION_TITLE, "New Group");
            }

            fragment.setArguments(args);
            return fragment;
        }

        public GroupSettingsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_settings_group, container, false);
            Context mContext = getActivity().getBaseContext();
            ArrayList<String> allNames = new ArrayList<>();
            groupNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            if(getArguments().getString(ARG_SECTION_TITLE).equals("New Group")){
                activeGroup = new ExpenseGroup("New Group");
            }else{

                activeGroup = GroupAdapter.getExpenseGroup(groupNumber);
                ArrayList<Person> activeGroupMembers = activeGroup.getMembers();
                for(Person p : activeGroupMembers){
                    allNames.add(p.getName());
                }
                loadFragmentWithGroup(rootView, activeGroup);
            }


            ListView membersList = (ListView) rootView.findViewById(R.id.listMembers);
            ArrayAdapter<String> membersAdapter = new ArrayAdapter<>(getActivity().getBaseContext(),
                    R.layout.item_member_list,
                    R.id.memberName,
                    allNames);

            membersAdapter.setNotifyOnChange(true);
            membersList.setAdapter(membersAdapter);


            Button addMember = (Button) rootView.findViewById(R.id.addMember);
            addMember.setOnClickListener(new GroupListener(mContext));
            membersList.setOnItemLongClickListener(new GroupListener.GroupLongListener(getActivity()));
            Button createButton = (Button) rootView.findViewById(R.id.createGroupButton);
            createButton.setOnClickListener(new GroupListener(mContext, rootView));
            EditText titleText = (EditText) rootView.findViewById(R.id.titleText);
            EditText descText = (EditText) rootView.findViewById(R.id.descriptionText);
            titleText.setOnClickListener(new GroupListener(mContext));
            descText.setOnClickListener(new GroupListener(mContext));

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getString(ARG_SECTION_TITLE));
        }

        private void loadFragmentWithGroup(View editGroupView, ExpenseGroup loadGroup) {
            EditText titleText = (EditText) editGroupView.findViewById(R.id.titleText);
            EditText descText = (EditText) editGroupView.findViewById(R.id.descriptionText);
            Button createButton = (Button) editGroupView.findViewById(R.id.createGroupButton);
            titleText.setText(loadGroup.getTitle());
            descText.setText(loadGroup.getDescription());
            createButton.setText(R.string.save_group_changes);
        }

        public static ExpenseGroup getActiveGroup() {
            return activeGroup;
        }
    }


    public static class ProfileFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_TITLE = "section_title";
        private Person owner;
        private EditText textName;
        private EditText textPhoneNr;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ProfileFragment newInstance() {
            ProfileFragment fragment = new ProfileFragment();
            Bundle args = new Bundle();
            //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_TITLE, "Profile");
            fragment.setArguments(args);

            return fragment;
        }

        public ProfileFragment() {
            owner = new Person();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);


            textName = (EditText) rootView.findViewById(R.id.profileName);
            textPhoneNr = (EditText) rootView.findViewById(R.id.phoneText);

            if(db.getContactTableSize() == 0){
                db.savePersonToDb(owner);
            }else{
                Person profileOwner = db.getPerson(1);
                textName.setText(profileOwner.getName());
                textPhoneNr.setText(profileOwner.getNumber());
            }

            return rootView;
        }

        @Override
        public void onDetach() {
            owner.setName(textName.getText().toString() + " (Me)");
            owner.setNumber(textPhoneNr.getText().toString());
            //owner.setDbId(1);
            db.updatePerson(owner);
            super.onDetach();
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getString(ARG_SECTION_TITLE));
        }
    }
}
