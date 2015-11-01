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

    private CalculateExpenses(){
        logTitle = "CalculateExpenses";
    }

    public static CalculateExpenses getInstance(){
        if(mInstance == null)
        {
            mInstance = new CalculateExpenses();
        }
        return mInstance;
    }

    private int calculateIndividualDebt(Person individual, ArrayList<Expense> expenses){
        long individualDbId = individual.getDbId();
        int debtAmount = 0;
        long[] tmpAffectedIds;
        for(Expense e : expenses){
            tmpAffectedIds = e.getAffectedMembersIds();
            for(long id : tmpAffectedIds){
                if(individualDbId == id){
                    debtAmount += Math.ceil(e.getAmount() / tmpAffectedIds.length);
                }
            }

        }
        return debtAmount;
    }

    private int calculateIndividualExpense(Person individual, ArrayList<Expense> expenses){
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
        int total;
        total = calculateIndividualExpense(individual,expenses) - calculateIndividualDebt(individual, expenses);
        return total;
    }


    public void calculateSplitPayment(ExpenseGroup group){
        ArrayList<Person> members = group.getMembers();
        ArrayList<Expense> expenses = group.getExpenses();
        //expenses = sortArrayList(expenses);
        //LinkedHashMap<Person, Integer> personalPositiveExpenseMap = new LinkedHashMap<>();  // Have bigger expenses then debts
 //       LinkedHashMap<Person, Integer> personalNegativeExpenseMap = new LinkedHashMap<>();  // Have bigger debts then expenses
//        TreeMap<Person, Integer> personalPositiveExpenseMap = new TreeMap<>();  // Have bigger expenses then debts
//        TreeMap<Person, Integer> personalNegativeExpenseMap = new TreeMap<>();  // Have bigger debts then expenses

        List<SortExpenses> personalDebts = new ArrayList<>();
        List<SortExpenses> personalExpenses = new ArrayList<>();


        for(Person p : members){
            int pExpense = calculateIndividualTotal(p, expenses);
            if(pExpense > 0){
                personalExpenses.add(new SortExpenses(p,pExpense));
                //personalPositiveExpenseMap.put(p, pExpense);
            }
            else if(pExpense < 0){  // Use absolute value even if list contains debts
                personalDebts.add(new SortExpenses(p,Math.abs(pExpense)));
                //personalNegativeExpenseMap.put(p, Math.abs(pExpense));
            }   // else person has no expenses or debts
        }

        findPaymentSuggestion(personalExpenses, personalDebts);
        //findPaymentSuggestion(personalPositiveExpenseMap, personalNegativeExpenseMap);

    }

    private ArrayList<PaymentClass> findPaymentSuggestion(List<SortExpenses> personalExpenses, List<SortExpenses> personalDebts){
        ArrayList<PaymentClass> solvedPayments = new ArrayList<>();
        Collections.sort(personalDebts);
        Collections.sort(personalExpenses);

        //Find any perfect match
        for(SortExpenses debt : personalDebts){
            for(SortExpenses expense : personalExpenses){
                // The perfect match exist?
                if(debt.getExpenseAmount() == expense.getExpenseAmount()){
                    solvedPayments.add(new PaymentClass(debt.getPerson(), expense.getPerson(), debt.getExpenseAmount()));
                    personalDebts.remove(debt);
                    personalExpenses.remove(expense);
                    Log.d("CalculteExp", "Found a perfect match");
                    break;
                }
                else if(expense.getExpenseAmount() < debt.getExpenseAmount()){
                    break;
                }
            }
        }

        Collections.sort(personalDebts);

        for(SortExpenses debt : personalDebts){
            Log.d("CalculateExp/findPay", "Searching for a good match for: " + debt.getExpenseAmount());
            PaymentClass tmpSuggestion = new PaymentClass();
            SortExpenses tmpExpense = new SortExpenses();
            Collections.sort(personalExpenses);
            for(SortExpenses expense : personalExpenses){
                // Is single payments possible?
                if(debt.getExpenseAmount() < expense.getExpenseAmount()){
                    tmpSuggestion = new PaymentClass(debt.getPerson(), expense.getPerson(), debt.getExpenseAmount());
                    tmpExpense = expense;
                    break;
                } // Will not find anny better match
                else if(debt.getExpenseAmount() > expense.getExpenseAmount()){

                    // Is there any expense that is bigger then the debt?
                    if(tmpSuggestion.getAmount() != 0){
                        solvedPayments.add(tmpSuggestion);
                        personalDebts.remove(debt);
                        int location = personalExpenses.indexOf(tmpExpense);
                        tmpExpense.setExpenseAmount(tmpExpense.getExpenseAmount()-debt.getExpenseAmount());
                        personalExpenses.set(location, tmpExpense);
                    }
                    break;
                }
            }
        }

        return solvedPayments;
    }

/*
    private void findPaymentSuggestion(Map<Person, Integer> positiveExpenses, Map<Person, Integer> negativeExpenses){
        //LinkedHashMap<Person, List<Long>> solvedPersonMap = new LinkedHashMap<>();
        ArrayList<PaymentClass> solvedPayments;
        //List<Long> payOfDebtTo = new LinkedList<>();    // Store keys (dbId) for persons to pay of debt to

        OuterLoop:
        for (Map.Entry<Person, Integer> positive : positiveExpenses.entrySet()) {
            // First see to find perfect match
            PaymentClass payment = new PaymentClass();
            if (negativeExpenses.containsValue(positive.getValue())) {
                for (Map.Entry<Person, Integer> negative : negativeExpenses.entrySet())
                {
                    if(positive.getValue() == negative.getValue()){
                        //payOfDebtTo.add(positive.getKey());
                        payment.setPersonToPay(negative.getKey());
                        payment.setReceiver(positive.getKey());
                        payment.setAmount(positive.getValue());
                        positiveExpenses.remove(positive.getKey());
                        negativeExpenses.remove(negative.getKey());
                        break;
                    }
                }
             //Second see if we can pay of everything to one person
            }else{
                for (Map.Entry<Person, Integer> negative : negativeExpenses.entrySet())
                {
                    if(positive.getValue() > negative.getValue()){
                        //payOfDebtTo.add(positive.getKey());
                        payment.setPersonToPay(negative.getKey());
                        payment.setReceiver(positive.getKey());
                        payment.setAmount(negative.getValue());
                        positiveExpenses.put(positive.getKey(), positive.getValue() - negative.getValue());
                        negativeExpenses.remove(negative.getKey());
                        continue OuterLoop;
                    }
                }

            }

        }



        // TODO: Continue here to get
        for (Map.Entry<Person, Integer> positive : positiveExpenses.entrySet()) {
            for (Map.Entry<Person, Integer> negative : negativeExpenses.entrySet()) {
                if (positive.getValue() < negative.getValue()) {
                    //payOfDebtTo.add(positive.getKey());
                    positiveExpenses.put(positive.getKey(), positive.getValue() - negative.getValue());
                    negativeExpenses.remove(negative.getKey());
                    break;
                }
            }
        }
    }
*/


    public class SortExpenses implements Comparable{

        private Person person;
        private int expenseAmount;

        public SortExpenses(Person person, int expenseAmount){
            this.person = person;
            this.expenseAmount = expenseAmount;
        }

        public SortExpenses(){}

        @Override
        public int compareTo(Object o) {

            SortExpenses f = (SortExpenses)o;

            if (expenseAmount < f.expenseAmount) {
                return 1;
            }
            else if (expenseAmount >  f.expenseAmount) {
                return -1;
            }
            else {
                return 0;
            }

        }

        @Override
        public String toString(){
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


/* ##### REMOVE ##### */

    private ArrayList<Expense> sortArrayList(Map<Long, Integer> positiveExpenses, Map<Long, Integer> negativeExpenses) {
        Collections.sort((List<Map.Entry<Long, Integer>>) positiveExpenses, new Comparator<Map.Entry<Long, Integer>>() {

            @Override
            public int compare(Map.Entry<Long, Integer> lhs, Map.Entry<Long, Integer> rhs) {

                return 0;
            }
        });
        return null;
    }




    public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Double)val);
                    break;
                }

            }

        }
        return sortedMap;
    }
}
