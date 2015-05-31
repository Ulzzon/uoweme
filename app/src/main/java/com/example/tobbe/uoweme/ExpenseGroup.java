package com.example.tobbe.uoweme;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Tobbe on 2015-03-15.
 */
public class ExpenseGroup {
    private long dbID = 1;
    private String mTitle = "New Group";
    private String mDescription = "";
    private ArrayList<Person> members = new ArrayList<Person>();
    private String dbMembersString = "";
    private long[] membersId = {0};      // TODO: use this instead of membersString
    private long[] expenseId = {0};
    private static final String COMMA_SEP = ", ";
    private ArrayList<Expense> expenses = new ArrayList<>();

    private String LOG = "ExpenseGroup: ";


    public ExpenseGroup(){

    }


    public ExpenseGroup(String title ){
        this.mTitle = title;
    }

    public ExpenseGroup(String title, String description ){
        this.mTitle = title;
        this.mDescription = description;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public String getDescription(){ return mDescription; }

    public void setDescription(String description){ mDescription = description; }

    public void addMember(Person person){
        members.add(person);
        MainActivity.db.savePersonToDb(person);
    }

    public ArrayList<Person> getMembers(){
        loadAllMembersFromDb();
        return members;
    }

    public void setDbID (long id){this.dbID = id;}

    public long getDbID(){return dbID;}

// TODO: Remove string and use array for members instead
//    public void setDbMembersString(String membersString){ this.dbMembersString = membersString; }

 //   public String getDbMembersString(){ return dbMembersString; }

    public void setMembersArray(long[] membersIds){
        this.membersId = membersIds;
    }

    public long[] getMembersId(){
        return membersId;
    }

    public void loadAllMembersFromDb(){
        //long[] members = membersId;
        //String[] membersId = dbMembersString.split(COMMA_SEP);
        if(membersId.length != 0 ) {
            for (long id : membersId) {
                try {
                    //id.trim();
                    //Person newMember = MainActivity.db.getPerson(Long.parseLong(id));
                    Person newMember = MainActivity.db.getPerson(id);
                    if(!personExistAsMember(newMember)) {
                        members.add(newMember);
                        //members.add(newMember);
                        Log.d(LOG, mTitle + " Add member: " + newMember.getName());
                    }else{
                        Log.d(LOG, "Member: " +newMember.getName() +" all ready exist");
                    }
                    //members.add(MainActivity.db.getPerson(Long.parseLong(id)));
                }
                catch(Exception e){
                    Log.d(LOG, "Failed to get member: "+id);
                }
            }
        }
    }

    private boolean personExistAsMember(Person person){
        for(Person p : members){
            if(p.getName().equals(person.getName()) && p.getNumber().equals(person.getNumber())){
                return true;
            }
        }
        return false;
    }


    public void deleteAllMembers(){
        if(!members.isEmpty()) {
            for (Person p : members) {
                MainActivity.db.deletePerson(p.getDbId());
            }
            members.clear();
            dbMembersString = "";
            MainActivity.db.updateGroup(this);
        }
    }


    /* ##### Expenses ##### */
    public void addExpense(Expense expense){
        expenses.add(expense);
        MainActivity.db.saveExpenseToDb(expense);
        MainActivity.db.updateGroup(this);

    }

    public void deleteAllExpenses(){
        if(!expenses.isEmpty()){
            for(Expense e : expenses){
                MainActivity.db.deleteExpense(e.getDbId());
            }
            expenses.clear();
        }
    }

    public void setExpenseId(long[] expensesId){
        expenseId = expensesId;
    }

    public long[] getExpenseId(){ return expenseId; }

    public void loadAllExpensesFromDb(){
        if(expenseId.length != 0){
            for(long eId : expenseId){
                try{
                    Expense loadExpense = MainActivity.db.getExpense(eId);
                    if(!expenseExistInArray(loadExpense)){
                        expenses.add(loadExpense);
                        Log.d(LOG, mTitle + " Add expense: " + loadExpense.getTitle());
                    }
                }catch(Exception e){
                    Log.d(LOG, mTitle + "Expense failed to load on to group");
                }
            }
        }
    }

    public ArrayList<Expense> getExpenses(){
        loadAllExpensesFromDb();
        return expenses;
    }


    private boolean expenseExistInArray(Expense expenseToLookFor){
        for(Expense e : expenses){
            if(e.getDbId() == expenseToLookFor.getDbId()){
                return true;
            }
        }
        return false;
    }

}
