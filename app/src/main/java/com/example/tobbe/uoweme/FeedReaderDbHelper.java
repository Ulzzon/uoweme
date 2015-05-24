package com.example.tobbe.uoweme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by TobiasOlsson on 15-03-25.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GroupDB.db";


    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION+TEXT_TYPE+
                    // Any other options for the CREATE command
                    " )";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    private SQLiteDatabase mDb = getWritableDatabase();

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void saveGroupsToDb(ArrayList<ExpenseGroup> groups){
//        SQLiteDatabase mDb = getWritableDatabase();
        int i = 0;
        ContentValues values = new ContentValues();
        for(ExpenseGroup g : groups){

// Create a new map of values, where column names are the keys

            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, i);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, g.getTitle());
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, g.getDescription());
            i++;

// Insert the new row, returning the primary key value of the new row
            long newRowId;

            newRowId = mDb.insert(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    null,
                    //FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                    values);
        }

        /*ExpenseGroup group;
        for(int i = 0; i < groups.size(); i++){
            group = groups.get(i);

// Create a new map of values, where column names are the keys

            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, i);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, group.getTitle());
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, group.getDescription());


// Insert the new row, returning the primary key value of the new row
            long newRowId;

            newRowId = mDb.insert(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    null,
                    //FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                    values);
        }*/
    }

    public long saveGroupsToDb(ExpenseGroup group){
//        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
// Create a new map of values, where column names are the keys
        //values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, i);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, group.getTitle());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, group.getDescription());


// Insert the new row, returning the primary key value of the new row
        long newRowId;

        newRowId = mDb.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                //FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }

    public ExpenseGroup readGroupFromDb(int position){
//        SQLiteDatabase db = getReadableDatabase();
        String title;
        String description;
        int rowId = position;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " DESC";

        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(rowId) };

        Cursor c = mDb.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        //c.moveToFirst();
        try {
            c.moveToPosition(position);
            //int itemId = c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID));
            title = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
            description = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION));
        }catch(Exception e){
            title = "No Title";
            description = "No Description";
        }
        return new ExpenseGroup(title, description);
    }

    public ArrayList<ExpenseGroup> readGroupFromDb(){
//        SQLiteDatabase db = getReadableDatabase();
        String title;
        String description;
        ArrayList<ExpenseGroup> groups = new ArrayList<>();
        int rowId = 1;
        //int rowId = position;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " DESC";

        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "Group 1" };//String.valueOf(rowId) };

        Cursor c = mDb.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();

        //int itemId = c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID));
        //title = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
        //description = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION));
        //groups.add(new ExpenseGroup(title, description));

        for(int i = 0; i<c.getCount(); i++){
            ExpenseGroup readGroup = new ExpenseGroup("Group Title", "This description");
            readGroup.setTitle(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            readGroup.setDescription(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION)));
            readGroup.setDbID(c.getInt(c.getColumnIndexOrThrow((FeedReaderContract.FeedEntry._ID))));
            groups.add(readGroup);
            c.moveToNext();
        }
        return groups;
    }

    public int getDbCount(){
//        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION,
        };
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " DESC";

        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = {"Title"};
        Cursor c = mDb.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                             // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return c.getCount();
    }

    public void updateDbRow(ExpenseGroup group){
//        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
// Create a new map of values, where column names are the keys
        //values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, i);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, group.getTitle());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, group.getDescription());
        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry._ID + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(group.getDbID()) };

// Insert the new row, returning the primary key value of the new row
        mDb.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                //FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                selection,
                selectionArgs);
    }

    public void deleteRowFromDb(int rowId){
//        SQLiteDatabase db = getWritableDatabase();
    // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry._ID + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(rowId) };
// Issue SQL statement.
        mDb.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void deleteDb(){
//        SQLiteDatabase db = getWritableDatabase();

// Issue SQL statement.
        mDb.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null);
    }
}
