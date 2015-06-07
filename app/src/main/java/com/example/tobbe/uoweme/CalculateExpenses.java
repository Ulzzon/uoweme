package com.example.tobbe.uoweme;

import java.util.ArrayList;

/**
 * Created by TobiasOlsson on 15-06-07.
 */
public enum CalculateExpenses {
    INSTANCE;


    public int calculateDebt(Person individual, ExpenseGroup group){
        long individualDbId = individual.getDbId();
        int debtAmount = 0;
        ArrayList<Expense> allExpenses = group.getExpenses();
        long[] tmpEffectedIds;
        for(Expense e : allExpenses){
            tmpEffectedIds = e.getAffectedMembersIds();
            for(long id : tmpEffectedIds){
                if(individualDbId == id){
                    debtAmount += e.getAmount();
                }
            }

        }
        return debtAmount;
    }

    public int calculateIndividualExpense(Person individual, ExpenseGroup group){
        long individualDbId = individual.getDbId();
        int totalExpenseAmount = 0;
        ArrayList<Expense> allExpenses = group.getExpenses();
        for(Expense e : allExpenses){
            if(e.getOwnerId() == individualDbId){
                totalExpenseAmount += e.getAmount();
            }
        }
        return totalExpenseAmount;
    }
}
