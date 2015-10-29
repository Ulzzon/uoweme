package com.example.tobbe.uoweme.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tobbe.uoweme.Expense;
import com.example.tobbe.uoweme.PaymentClass;
import com.example.tobbe.uoweme.Person;
import com.example.tobbe.uoweme.R;

import java.util.ArrayList;

import helper.CalculateExpenses;

/**
 * Created by TobiasOlsson on 15-10-26.
 */
public class PaymentsAdapter extends BaseAdapter{

    private final Context context;
    private ArrayList<PaymentClass> payments;
    private LayoutInflater inflater = null;
/*    private ArrayList<Person> myPersons;
    private ArrayList<Expense> expenses;
*/
    public PaymentsAdapter(Context context, ArrayList<PaymentClass> payments){

        this.context = context;
        this.payments = payments;
    }

    @Override
    public int getCount() {
        return payments.size();
    }

    @Override
    public Object getItem(int position) {
        return payments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (vi == null)
        {
            vi = inflater.inflate(R.layout.item_payment_list, null);
        }

        //Person member = myPersons.get(position);
        PaymentClass payment = payments.get(position);
        TextView payingPersonView = (TextView) vi.findViewById(R.id.fromPerson_TV);
        payingPersonView.setText(payment.getPersonToPay().getName());
        //nameView.setText(member.getName());
        TextView receivingPersonView = (TextView) vi.findViewById(R.id.toPerson_TV);
        receivingPersonView.setText(payment.getReceiver().getName());
        //phoneNrView.setText(member.getNumber());
        TextView amountToPayView = (TextView) vi.findViewById(R.id.payAmount_TV);
        amountToPayView.setText(payment.getAmount());

        return vi;
    }
}
