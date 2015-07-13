package helper;

import com.example.tobbe.uoweme.Expense;
import com.example.tobbe.uoweme.ExpenseGroup;
import com.example.tobbe.uoweme.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by TobiasOlsson on 15-06-07.
 */
public class CalculateExpenses {

    private int calculateDebt(Person individual, ArrayList<Expense> expenses){
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
        int total = 0;
        total = calculateIndividualExpense(individual,expenses) - calculateDebt(individual,expenses);
        return total;
    }


    public void calculateSplitPayment(ExpenseGroup group){
        ArrayList<Person> members = group.getMembers();
        ArrayList<Expense> expenses = group.getExpenses();
        //expenses = sortArrayList(expenses);
        LinkedHashMap<Long, Integer> personalPositiveExpenseMap = new LinkedHashMap<>();  // Have bigger expenses then debts
        LinkedHashMap<Long, Integer> personalNegativeExpenseMap = new LinkedHashMap<>();  // Have bigger debts then expenses

        for(Person p : members){
            int pExpense = calculateIndividualTotal(p, expenses);
            if(pExpense > 0){
                personalPositiveExpenseMap.put(p.getDbId(), pExpense);
            }
            else if(pExpense < 0){
                personalNegativeExpenseMap.put(p.getDbId(), Math.abs(pExpense));
            }   // else person has no expenses or debts
        }


        findPaymentSuggestion(personalPositiveExpenseMap,personalNegativeExpenseMap);



//TODO: remove this if added in findPayment


        for(int p = 0; p < personalPositiveExpenseMap.size(); p++){

            for(int n = 0; n < personalNegativeExpenseMap.size(); n++){

                if(personalPositiveExpenseMap.get(p) == personalNegativeExpenseMap.get(n)){
                    //solvedPersonMap.put()
                    personalNegativeExpenseMap.remove(n);
                    personalPositiveExpenseMap.remove(p);
                }
            }
        }
    }

    private void findPaymentSuggestion(Map<Long, Integer> positiveExpenses, Map<Long, Integer> negativeExpenses){
        LinkedHashMap<Long, List<Long>> solvedPersonMap = new LinkedHashMap<>();
        List<Long> payOfDebtTo = new LinkedList<>();    // Store keys (dbId) for persons to pay of debt to

        OuterLoop:
        for (Map.Entry<Long, Integer> positive : positiveExpenses.entrySet()) {
            // First see to find perfect match
            if (negativeExpenses.containsValue(positive.getValue())) {
                for (Map.Entry<Long, Integer> negative : negativeExpenses.entrySet())
                {
                    if(positive.getValue() == negative.getValue()){
                        payOfDebtTo.add(positive.getKey());
                        positiveExpenses.remove(positive.getKey());
                        negativeExpenses.remove(negative.getKey());
                        break;
                    }
                }
             //Second see if we can pay of everything to one person
            }else{
                for (Map.Entry<Long, Integer> negative : negativeExpenses.entrySet())
                {
                    if(positive.getValue() > negative.getValue()){
                        payOfDebtTo.add(positive.getKey());
                        positiveExpenses.put(positive.getKey(), positive.getValue()-negative.getValue());
                        negativeExpenses.remove(negative.getKey());
                        continue OuterLoop;
                    }
                }

            }

        }



        // TODO: Continue here to get
        for (Map.Entry<Long, Integer> positive : positiveExpenses.entrySet()) {
            for (Map.Entry<Long, Integer> negative : negativeExpenses.entrySet()) {
                if (positive.getValue() < negative.getValue()) {
                    payOfDebtTo.add(positive.getKey());
                    positiveExpenses.put(positive.getKey(), positive.getValue() - negative.getValue());
                    negativeExpenses.remove(negative.getKey());
                    break;
                }
            }
        }
    }

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
