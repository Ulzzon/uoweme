package com.example.tobbe.uoweme;

import java.util.ArrayList;

/**
 * Created by TobiasOlsson on 15-06-07.
 */
public enum CalculateExpenses {
    INSTANCE;


    public int calculateDebt(Person individual, ArrayList<Expense> expenses){
        long individualDbId = individual.getDbId();
        int debtAmount = 0;
        long[] tmpEffectedIds;
        for(Expense e : expenses){
            tmpEffectedIds = e.getAffectedMembersIds();
            for(long id : tmpEffectedIds){
                if(individualDbId == id){
                    debtAmount += Math.ceil(e.getAmount() / tmpEffectedIds.length);
                }
            }

        }
        return debtAmount;
    }

    public int calculateIndividualExpense(Person individual, ArrayList<Expense> expenses){
        long individualDbId = individual.getDbId();
        int totalExpenseAmount = 0;
        for(Expense e : expenses){
            if(e.getOwnerId() == individualDbId){
                totalExpenseAmount += e.getAmount();
            }
        }
        return totalExpenseAmount;
    }

    public int calculateIndividualTotal(Person individual, ArrayList<Expense> expenses){
        int total = 0;
        total = calculateIndividualExpense(individual,expenses) - calculateDebt(individual,expenses);
        return total;
    }
}
