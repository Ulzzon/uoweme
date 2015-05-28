package com.example.tobbe.uoweme;

/**
 * Created by Tobbe on 2015-03-14.
 */
public class Expense {
    private String title;
    private int amount = 1;
    private long dbId;
    private long ownerId;
    private long[] affectedMembersIds;
    //private enum exchange;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public long getDbId() {
        return dbId;
    }

    public int getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public void setOwnerId(long owner) {
        this.ownerId = owner;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setAffectedMembersIds(long[] affectedMembersIds) {
        this.affectedMembersIds = affectedMembersIds;
    }

    public long[] getAffectedMembersIds() {
        return affectedMembersIds;
    }

}
