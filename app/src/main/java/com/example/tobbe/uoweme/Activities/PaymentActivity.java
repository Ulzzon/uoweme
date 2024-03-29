package com.example.tobbe.uoweme.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tobbe.uoweme.ExpenseGroup;
import com.example.tobbe.uoweme.PaymentClass;
import com.example.tobbe.uoweme.R;
import com.example.tobbe.uoweme.adapters.GroupAdapter;
import com.example.tobbe.uoweme.adapters.PaymentsAdapter;

import java.util.ArrayList;

import helper.CalculateExpenses;

/**
 * Created by TobiasOlsson on 15-10-26.
 */
public class PaymentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        int groupId = getIntent().getIntExtra(getString(R.string.group_number), 1);
        ExpenseGroup group = GroupAdapter.getExpenseGroup(groupId);
        Log.d("PaymentActivity", "Open view for " + group.getTitle() + " group");
        ArrayList<PaymentClass> payments = CalculateExpenses.getInstance().calculateSplitPayment(group);

        ListView paymentList = (ListView) findViewById(R.id.listPaymentSuggestions);
        PaymentsAdapter pAdater = new PaymentsAdapter(getApplicationContext(), payments);

        paymentList.setAdapter(pAdater);

    }
}
