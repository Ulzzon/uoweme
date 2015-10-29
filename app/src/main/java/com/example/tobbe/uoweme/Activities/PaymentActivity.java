package com.example.tobbe.uoweme.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tobbe.uoweme.R;
import com.example.tobbe.uoweme.adapters.GroupAdapter;
import com.example.tobbe.uoweme.adapters.PaymentsAdapter;

import java.util.ArrayList;

/**
 * Created by TobiasOlsson on 15-10-26.
 */
public class PaymentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ListView paymentList = (ListView) findViewById(R.id.listPaymentSuggestions);
        /*PaymentsAdapter pAdater = new PaymentsAdapter();

        paymentList.setAdapter(pAdater);
        */
    }
}
