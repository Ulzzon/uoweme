package helper;

import android.util.Log;

import com.example.tobbe.uoweme.Expense;
import com.example.tobbe.uoweme.ExpenseGroup;
import com.example.tobbe.uoweme.PaymentClass;
import com.example.tobbe.uoweme.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TobiasOlsson on 15-06-07.
 */
public class CalculateExpenses {

    private static CalculateExpenses mInstance = null;

    private String logTitle;

    private CalculateExpenses() {
        logTitle = "CalculateExpenses";
    }

    public static CalculateExpenses getInstance() {
        if (mInstance == null) {
            mInstance = new CalculateExpenses();
        }
        return mInstance;
    }

    private int calculateIndividualDebt(Person individual, ArrayList<Expense> expenses) {
        long individualDbId = individual.getDbId();
        int debtAmount = 0;
        long[] tmpAffectedIds;
        for (Expense e : expenses) {
            tmpAffectedIds = e.getAffectedMembersIds();
            for (long id : tmpAffectedIds) {
                if (individualDbId == id) {
                    debtAmount += Math.ceil(e.getAmount() / tmpAffectedIds.length);
                }
            }

        }
        return debtAmount;
    }

    private int calculateIndividualExpense(Person individual, ArrayList<Expense> expenses) {
        long individualDbId = individual.getDbId();
        int totalExpenseAmount = 0;
        for (Expense e : expenses) {
            if (e.getOwnerId() == individualDbId) {
                totalExpenseAmount += e.getAmount();
            }
        }
        return totalExpenseAmount;
    }

    public int calculateIndividualTotal(Person individual, ArrayList<Expense> expenses) {
        int total;
        total = calculateIndividualExpense(individual, expenses) - calculateIndividualDebt(individual, expenses);
        return total;
    }


    public ArrayList<PaymentClass> calculateSplitPayment(ExpenseGroup group) {
        ArrayList<Person> members = group.getMembers();
        ArrayList<Expense> expenses = group.getExpenses();
        List<SortExpenses> personalDebts = new ArrayList<>();
        List<SortExpenses> personalExpenses = new ArrayList<>();


        for (Person p : members) {
            int pExpense = calculateIndividualTotal(p, expenses);
            if (pExpense > 0) {
                personalExpenses.add(new SortExpenses(p, pExpense));
            } else if (pExpense < 0) {  // Use absolute value even if list contains debts
                personalDebts.add(new SortExpenses(p, Math.abs(pExpense)));
            }   // else person has no expenses or debts
        }

        return findPaymentSuggestion(personalExpenses, personalDebts);
        //findPaymentSuggestion(personalPositiveExpenseMap, personalNegativeExpenseMap);

    }

    private ArrayList<PaymentClass> findPaymentSuggestion(List<SortExpenses> personalExpenses, List<SortExpenses> personalDebts) {
        ArrayList<PaymentClass> solvedPayments = new ArrayList<>();
        Collections.sort(personalDebts);
        Collections.sort(personalExpenses);

        Iterator<SortExpenses> debtIterator = personalDebts.iterator();
        Iterator<SortExpenses> expensesIterator = personalExpenses.iterator();
        //Find any perfect match

        while (debtIterator.hasNext()) {
            SortExpenses debt = debtIterator.next();
            while (expensesIterator.hasNext()) {
                SortExpenses expense = expensesIterator.next();
                // The perfect match exist?
                if (debt.getExpenseAmount() == expense.getExpenseAmount()) {
                    solvedPayments.add(new PaymentClass(debt.getPerson(), expense.getPerson(), debt.getExpenseAmount()));
                    debtIterator.remove();
                    expensesIterator.remove();
                    Log.d(logTitle, "Found a perfect match");
                    break;
                } else if (expense.getExpenseAmount() < debt.getExpenseAmount()) {
                    break;
                }
            }
        }

        Collections.sort(personalDebts);
        Collections.sort(personalExpenses);
        debtIterator = personalDebts.iterator();
        expensesIterator = personalExpenses.iterator();


        debtLoop:
        while (debtIterator.hasNext()) {
            SortExpenses debt = debtIterator.next();
            Log.d(logTitle, "Searching for a good match for: " + debt.getExpenseAmount());
            PaymentClass tmpSuggestion = new PaymentClass();
            SortExpenses tmpExpense = new SortExpenses();
            Collections.sort(personalExpenses);

            expenseLoop:
            while (expensesIterator.hasNext()) {
                SortExpenses expense = expensesIterator.next();
                // Is single payments possible?
                if (debt.getExpenseAmount() < expense.getExpenseAmount()) {
                    tmpSuggestion = new PaymentClass(debt.getPerson(), expense.getPerson(), debt.getExpenseAmount());
                    tmpExpense = expense;
                    Log.d(logTitle, "Found one tmp match");
                } // Will not find anny better match
                else if (debt.getExpenseAmount() > expense.getExpenseAmount()) {

                    // Is there any expense that is bigger then the debt?
                    if (tmpSuggestion.getAmount() != 0) {
                        solvedPayments.add(tmpSuggestion);
                        debtIterator.remove();
                        int location = personalExpenses.indexOf(tmpExpense);
                        tmpExpense.setExpenseAmount(tmpExpense.getExpenseAmount() - debt.getExpenseAmount());
                        personalExpenses.set(location, tmpExpense);
                    }
                    else {
                        solvedPayments.add(new PaymentClass(debt.getPerson(), expense.getPerson(), expense.getExpenseAmount()));
                        expensesIterator.remove();
                        int location = personalDebts.indexOf(debt);
                        debt.setExpenseAmount(debt.getExpenseAmount() - tmpExpense.getExpenseAmount());
                        personalDebts.set(location, debt);
                    }

                }
            }
        }
        return solvedPayments;
    }



    public class SortExpenses implements Comparable {

        private Person person;
        private int expenseAmount;

        public SortExpenses(Person person, int expenseAmount) {
            this.person = person;
            this.expenseAmount = expenseAmount;
        }

        public SortExpenses() {
        }

        @Override
        public int compareTo(Object o) {

            SortExpenses f = (SortExpenses) o;

            if (expenseAmount < f.expenseAmount) {
                return 1;
            } else if (expenseAmount > f.expenseAmount) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return Integer.toString(this.expenseAmount);
        }

        public Person getPerson() {
            return person;
        }

        public int getExpenseAmount() {
            return expenseAmount;
        }

        public void setExpenseAmount(int expenseAmount) {
            this.expenseAmount = expenseAmount;
        }
    }
}