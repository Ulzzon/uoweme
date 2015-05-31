package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tobbe.uoweme.Expense;
import com.example.tobbe.uoweme.ExpenseGroup;
import com.example.tobbe.uoweme.Person;

import java.util.ArrayList;

/**
 * Created by TobiasOlsson on 15-04-25.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "expenseManager";

    // Table Names
    private static final String TABLE_GROUP = "groups";
    private static final String TABLE_MEMBER = "members";
    private static final String TABLE_EXPENSE = "expenses";

    // Common column names
    private static final String KEY_ID= "id";
    private static final String KEY_MODIFIED_AT = "created_at";

    // Group Table - column names
    private static final String KEY_GROUP_TITLE= "title";
    private static final String KEY_GROUP_DESCRIPTION= "description";
    private static final String KEY_GROUP_MEMBERS = "members";
    private static final String KEY_GROUP_EXPENSES = "expenses";

    // Members Table - column names
    private static final String KEY_MEMBER_NAME = "name";
    private static final String KEY_MEMBER_PHONE = "number";
    //private static final String KEY_STATUS = "status";

    // Expense Table - column names
    private static final String KEY_EXPENSE_ID = "expense_id";
    private static final String KEY_EXPENSE_TITLE = "title";
    private static final String KEY_EXPENSE_MEMBERS = "expense_members";
    private static final String KEY_EXPENSE_VALUE = "expense_value";
    private static final String KEY_EXPENSE_OWNER = "owner_id";


    // Table Create Statements
    // Group table create statement
    private static final String CREATE_TABLE_GROUP = "CREATE TABLE " + TABLE_GROUP
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_GROUP_TITLE + " TEXT,"
            + KEY_GROUP_DESCRIPTION + " TEXT,"
            + KEY_GROUP_MEMBERS + " TEXT,"
            + KEY_GROUP_EXPENSES + " TEXT,"
            + KEY_MODIFIED_AT + " DATETIME" + ")";

    // Member table create statement
    private static final String CREATE_TABLE_MEMBER = "CREATE TABLE " + TABLE_MEMBER
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_MEMBER_NAME + " TEXT,"
            + KEY_MEMBER_PHONE + " TEXT,"
            + KEY_MODIFIED_AT + " DATETIME" + ")";

    // Expense table create statement
    private static final String CREATE_TABLE_EXPENSE = "CREATE TABLE " + TABLE_EXPENSE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_EXPENSE_TITLE + " TEXT,"
            + KEY_EXPENSE_VALUE + " INTEGER,"
            + KEY_EXPENSE_MEMBERS + " TEXT,"
            + KEY_EXPENSE_OWNER + " INTEGER,"
            + KEY_MODIFIED_AT + " DATETIME" + ")";

    private static final String SQL_DELETE_GROUP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_GROUP;

    private static final String SQL_DELETE_MEMBER_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_MEMBER;

    private static final String SQL_DELETE_EXPENSE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_EXPENSE;



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_MEMBER);
        db.execSQL(CREATE_TABLE_EXPENSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL((SQL_DELETE_GROUP_TABLE));
        db.execSQL(SQL_DELETE_MEMBER_TABLE);
        db.execSQL(SQL_DELETE_EXPENSE_TABLE);
        onCreate(db);
    }

    /* ##### Group ##### */

    public ExpenseGroup getGroup(long group_id){
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_GROUP + " WHERE "
                + KEY_ID + " = " + group_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c == null){
            return null;
        }
        c.moveToFirst();

        ExpenseGroup eg = new ExpenseGroup("");
        eg.setDbID(c.getInt(c.getColumnIndex(KEY_ID)));
        eg.setTitle((c.getString(c.getColumnIndex(KEY_GROUP_TITLE))));
        eg.setDescription(c.getString(c.getColumnIndex(KEY_GROUP_DESCRIPTION)));
        c.close();
        return eg;
    }

    public ArrayList<ExpenseGroup> getAllGroupsFromDb(){
        SQLiteDatabase db = getWritableDatabase();
/*        String selectQuery = "SELECT * FROM " + TABLE_GROUP + " WHERE "
                + KEY_ID + "=" + group_id;

        Log.e(LOG, selectQuery);
*/        String[] projection = {
                KEY_ID,
                KEY_GROUP_TITLE,
                KEY_GROUP_DESCRIPTION,
                KEY_GROUP_MEMBERS,
                KEY_GROUP_EXPENSES
        };
        String sortBy = KEY_ID + " DESC";

        //Cursor c = db.rawQuery(selectQuery, null);
        Cursor c = db.query(TABLE_GROUP,
                projection,
                null,
                null,
                null,
                null,
                sortBy );

        if (c == null){
            return null;
        }
        c.moveToFirst();
        //ExpenseGroup eg = new ExpenseGroup("");
        ArrayList<ExpenseGroup> allGroups = new ArrayList<>();
        for(int i = 0; i < c.getCount(); i++) {
            ExpenseGroup eg = new ExpenseGroup();
            eg.setDbID(c.getInt(c.getColumnIndex(KEY_ID)));
            eg.setTitle((c.getString(c.getColumnIndex(KEY_GROUP_TITLE))));
            eg.setDescription(c.getString(c.getColumnIndex(KEY_GROUP_DESCRIPTION)));
            String memberString = c.getString(c.getColumnIndex(KEY_GROUP_MEMBERS));
            //eg.setDbMembersString(memberString);
            long[] membersArray = splitStringToId(memberString);
            eg.setMembersArray(membersArray);
            String expensesString = c.getString(c.getColumnIndex(KEY_GROUP_EXPENSES));
            long[] expenseArray = splitStringToId(expensesString);
            eg.setExpenseId(expenseArray);
            allGroups.add(eg);
            c.moveToNext();
            Log.d(LOG, "Title:" +eg.getTitle() + ", Db nr " +eg.getDbID() + ", members: "
                    +createStringOfId(eg.getMembersId()));
        }
        c.close();
        return allGroups;
    }

    public void updateGroup(ExpenseGroup saveGroup){
        SQLiteDatabase db = getWritableDatabase();
        String select = KEY_ID + " LIKE ? ";
        String[] selectionArgs = { String.valueOf(saveGroup.getDbID()) };
     //   String stringMembers = saveGroup.getDbMembersString();
        String stringMembers = createStringOfId(saveGroup.getMembersId());
        ArrayList<Person> members = saveGroup.getMembers();
        for(Person p : members){
            stringMembers += p.getDbId() + ", ";
        }

        String stringExpenses = "";
        ArrayList<Expense> expenses = saveGroup.getExpenses();
        for(Expense e : expenses){
            stringExpenses += e.getDbId() + ", ";
        }
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_TITLE, saveGroup.getTitle());
        values.put(KEY_GROUP_DESCRIPTION, saveGroup.getDescription());
        values.put(KEY_GROUP_MEMBERS, stringMembers);
        values.put(KEY_GROUP_EXPENSES, stringExpenses);
        db.update(TABLE_GROUP, values, select, selectionArgs);
        Log.d(LOG, "Title: " +saveGroup.getTitle() + ", Db nr " + saveGroup.getDbID() );

    }

    public long saveGroupToDb(ExpenseGroup saveGroup){
        SQLiteDatabase db = getWritableDatabase();

/*        String stringMembers = saveGroup.getDbMembersString();
        ArrayList<Person> members = saveGroup.getMembers();
        if(!members.isEmpty()) {
            for (Person p : members) {
                stringMembers += p.getDbId() + ", ";
            }
            Log.d(LOG, "Save members: " +stringMembers);
        }
*/
        String stringMembers = createStringOfId(saveGroup.getMembersId());
        ContentValues values = new ContentValues();
// Create a new map of values, where column names are the keys
        values.put(KEY_GROUP_TITLE, saveGroup.getTitle());
        values.put(KEY_GROUP_DESCRIPTION, saveGroup.getDescription());
        values.put(KEY_GROUP_MEMBERS, stringMembers);
        values.put(KEY_GROUP_EXPENSES, createStringOfId(saveGroup.getExpenseId()));
        

// Insert the new row, returning the primary key value of the new row
        long group_id = db.insert(TABLE_GROUP, null, values);
        saveGroup.setDbID(group_id);
        return group_id;
    }

    public void deleteGroup(long groupId){
        SQLiteDatabase db = getWritableDatabase();
        // Define 'where' part of query.
        String selection = KEY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(groupId) };
        // Issue SQL statement.
        db.delete(TABLE_GROUP, selection, selectionArgs);
    }


    /* ##### Person / Members ##### */

    public Person getPerson(long person_id){
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_MEMBER + " WHERE "
                + KEY_ID + " = " + person_id;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c == null){
            return null;
        }
        c.moveToFirst();

        Person person = new Person();
        person.setDbId(c.getLong(c.getColumnIndex(KEY_ID)));
        person.setName((c.getString(c.getColumnIndex(KEY_MEMBER_NAME))));
        person.setNumber(c.getString(c.getColumnIndex(KEY_MEMBER_PHONE)));
        c.close();
        return person;
    }


    public void updatePerson(Person savePerson){
        SQLiteDatabase db = getWritableDatabase();
        String select = KEY_ID + " LIKE ? ";
        String[] selectionArgs = { String.valueOf(savePerson.getDbId()) };

        ContentValues values = new ContentValues();
        values.put(KEY_MEMBER_NAME, savePerson.getName());
        values.put(KEY_MEMBER_PHONE, savePerson.getNumber());

        Log.d(LOG, "Update Person with: " +values.toString());
        db.update(TABLE_MEMBER, values, select, selectionArgs);


    }

    public long savePersonToDb(Person savePerson){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MEMBER_NAME, savePerson.getName());
        values.put(KEY_MEMBER_PHONE, savePerson.getNumber());

        long person_id = db.insert(TABLE_MEMBER, null, values);
        savePerson.setDbId(person_id);
        Log.d(LOG, "Save Person with id: " +person_id + " & " +values.toString());
        return person_id;
    }


    public void deletePerson(long personId){
        SQLiteDatabase db = getWritableDatabase();
        // Define 'where' part of query.
        String selection = KEY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(personId) };
        // Issue SQL statement.
        db.delete(TABLE_MEMBER, selection, selectionArgs);
        Log.d(LOG,"Delete Person: " +personId);
    }



    /* ##### Expenses ##### */

    public long saveExpenseToDb(Expense saveExpense){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_TITLE, saveExpense.getTitle());
        values.put(KEY_EXPENSE_VALUE, saveExpense.getAmount());
        values.put(KEY_EXPENSE_MEMBERS, createStringOfId(saveExpense.getAffectedMembersIds()));
        values.put(KEY_EXPENSE_OWNER, saveExpense.getOwnerId());

        long expense_id = db.insert(TABLE_EXPENSE, null, values);
        saveExpense.setDbId(expense_id);
        Log.d(LOG, "Save Expense with id: " +expense_id + " & " +values.toString());
        return expense_id;
    }

    public Expense getExpense(long expense_id){
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE + " WHERE "
                + KEY_ID+ " = " + expense_id ;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c == null){
            return null;
        }
        c.moveToFirst();

        Expense expense = new Expense();
        expense.setDbId(c.getLong(c.getColumnIndex(KEY_ID)));
        expense.setTitle((c.getString(c.getColumnIndex(KEY_EXPENSE_TITLE))));
        expense.setAmount(c.getInt(c.getColumnIndex(KEY_EXPENSE_VALUE)));
        expense.setOwnerId(c.getLong(c.getColumnIndex(KEY_EXPENSE_OWNER)));
        expense.setAffectedMembersIds(splitStringToId(
                c.getString(c.getColumnIndex(KEY_EXPENSE_MEMBERS))));
        c.close();
        return expense;
    }

    public ArrayList<Expense> getAllExpenses(long[] ids){
        SQLiteDatabase db = getWritableDatabase();
    /*  String selectQuery = "SELECT * FROM " + TABLE_EXPENSE + " WHERE "
                + KEY_ID+ " = " + expense_id ;
*/
        String[] projection = {
                KEY_ID,
                KEY_EXPENSE_TITLE,
                KEY_EXPENSE_VALUE,
                KEY_EXPENSE_OWNER,
                KEY_EXPENSE_MEMBERS
        };
        String sortBy = KEY_ID + " DESC";

        //Cursor c = db.rawQuery(selectQuery, null);
        Cursor c = db.query(TABLE_EXPENSE,
                projection,
                null,
                null,
                null,
                null,
                sortBy );

        if (c == null){
            return null;
        }
        c.moveToFirst();

        ArrayList<Expense> allExpenses = new ArrayList<>();
        for(int i=0; i<c.getCount(); i++){
            Expense expense = new Expense();
            expense.setDbId(c.getLong(c.getColumnIndex(KEY_ID)));
            expense.setTitle((c.getString(c.getColumnIndex(KEY_EXPENSE_TITLE))));
            expense.setAmount(c.getInt(c.getColumnIndex(KEY_EXPENSE_VALUE)));
            expense.setOwnerId(c.getLong(c.getColumnIndex(KEY_EXPENSE_OWNER)));
            expense.setAffectedMembersIds(splitStringToId(
                    c.getString(c.getColumnIndex(KEY_EXPENSE_MEMBERS))));
            allExpenses.add(expense);
            c.moveToNext();
        }
        c.close();
        return allExpenses;
    }


    public void deleteExpense(long expenseId){
        SQLiteDatabase db = getWritableDatabase();
        // Define 'where' part of query.
        String selection = KEY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(expenseId) };
        // Issue SQL statement.
        db.delete(TABLE_EXPENSE, selection, selectionArgs);
        Log.d(LOG,"Delete Expense: " +expenseId);

    }

    /* ##### General ##### */


    public long[] splitStringToId(String string){
        if(string.equals("") || string.isEmpty() ||string.equals(null)){
            return new long[1];
        }
        String[] ids = string.split(", ");
        if(ids.length == 0){
            return new long[1];
        }
        long[] id = new long[ids.length];
        int count = 0;
        for( String i : ids){
            id[count] = Long.parseLong(i);
            count++;
        }
        return id;
    }


    public String createStringOfId(long[] idArray){

        String idString = "";
        for(long m : idArray){
            idString += m + ", ";
        }
        return idString;
    }

    public int getContactTableSize(){
        SQLiteDatabase db = getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_EXPENSE;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int size = mcursor.getInt(0);
        mcursor.close();
        return size;
    }
}
