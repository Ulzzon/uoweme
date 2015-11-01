package com.example.tobbe.uoweme;

import android.util.Log;

/**
 * Created by TobiasOlsson on 15-10-29.
 */
public class PaymentClass {
    private Person personToPay;
    private Person receiver;
    private int amount = 0;

    public PaymentClass(){

    }

    public PaymentClass(Person personToPay, Person receivingPerson, int amount) {
        this.personToPay = personToPay;
        this.receiver = receivingPerson;
        this.amount = amount;
        Log.d("PaymentClass", "New payment found, PtP:" + personToPay.getName() + ", RP: " +
                receivingPerson.getName() + ", amount: " + amount);
    }

    public Person getPersonToPay() {
        return personToPay;
    }

    public Person getReceiver() {
        return receiver;
    }

    public int getAmount() {
        return amount;
    }

    public void setPersonToPay(Person personToPay) {
        this.personToPay = personToPay;
    }

    public void setReceiver(Person receiver) {
        this.receiver = receiver;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
