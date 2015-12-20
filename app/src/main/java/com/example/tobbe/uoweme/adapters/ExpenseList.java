package com.example.tobbe.uoweme.adapters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tobbe.uoweme.Expense;
import com.example.tobbe.uoweme.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Created by TobiasOlsson on 15-11-14.
 */
public class ExpenseList extends ArrayList{

    HashMap<Long, Integer> indexMap = new HashMap();
    String logTitle = "ExpenseList";

    public ExpenseList() {
        super();
    }


    public boolean addExpense(Expense expense) {
        if (super.add(expense)){
            indexMap.put(expense.getDbId(), super.indexOf(expense));
            return true;
        }
        return false;
    }

    @Override
    public void add(int index, Object object) {
        Log.d(logTitle, "Trying to call suppressed method");
    }

    @Override
    public void clear() {
        indexMap.clear();
        super.clear();
    }

    public Expense remove(long expenseId) {
        int index = indexMap.get(expenseId);
        indexMap.remove(expenseId);
        return (Expense)super.remove(index);
    }


    public Expense getById(long expenseId) {
        int index = indexMap.get(expenseId);
        return (Expense)super.get(index);
    }

    @Override
    public Expense get(int index) {
        return (Expense) super.get(index);
    }
}
