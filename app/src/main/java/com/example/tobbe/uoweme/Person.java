package com.example.tobbe.uoweme;

/**
 * Created by Tobbe on 2015-03-14.
 */
public class Person {

    private String number = "null";
    private String name = "none";
    private long dbId = 1;

    public Person(){}
    public Person(String name, String number){
        this.name = name;
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() { return this.number; }

    public void setDbId(long id){ this.dbId = id; }

    public long getDbId(){ return this.dbId; }
}
