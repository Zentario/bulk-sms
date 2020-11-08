package com.example.bulksms;

import android.telephony.SmsManager;
import java.util.ArrayList;

public class Batch {
    private ArrayList<String> batch_list;
    private int size;

    // Constructor init with maximum size of this batch
    public Batch(int size) {
        this.batch_list = new ArrayList<>(size);
        this.size = size;
    }

    // Add a number to this batch
    public boolean add(String number) {
        if(this.getBatchList().size() < this.getMaxSize()) {
            this.batch_list.add(number);
            return true;
        }
        return false;
    }

    // Send message to each phone number of this Batch
    public void sendMessage(String message) {
        for (int i = 0; i < this.getBatchList().size(); i++) {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(this.batch_list.get(i), null, message, null, null);
        }
    }

    // Get the array list of numbers in this batch
    public ArrayList<String> getBatchList() {
        return this.batch_list;
    }

    // Get current size of the batch
    public int getSize() {
        return this.batch_list.size();
    }

    // Get maximum possible size of this batch
    public int getMaxSize() {
        return this.size;
    }
}
