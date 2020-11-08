package com.example.bulksms;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BatchManager {

    private static final String TAG = "BatchManager";
    private ArrayList<Batch> batches;

    // Create array list of Batch objects depending on batch size variable
    public ArrayList<Batch> createBatches(ArrayList<String> numbersList, int batchSize) {
        batches = new ArrayList<>();
        Batch batch = new Batch(batchSize);

        for (int i = 0; i < numbersList.size(); i++) {
            batch.add(numbersList.get(i));
            if(batch.getSize() == batchSize) {
                batches.add(batch);
                batch = new Batch(batchSize);
            }
        }
        if(numbersList.size() > 0 && numbersList.size() % batchSize > 0) {
            batches.add(batch);
        }

        Log.d(TAG, "createBatches: " + batches.toString());
        return batches;
    }

    // Read the list of numbers from file uri and return array list of each line
    public ArrayList<String> readNumbersFromUri(Context context, Uri uri) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            if(line.length() == 10) {
                lines.add(line);
            }
        }
        if(inputStream != null) {
            inputStream.close();
        }
        reader.close();

        Log.d(TAG, "readNumbersFromUri: " + lines.toString());
        return lines;
    }

    // Return Batch number and available numbers in that batch
    public ArrayList<String> getBatchNames() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < batches.size(); i++) {
            String builder = "Batch - " + (i + 1) + "   Available numbers - " + batches.get(i).getSize();
            names.add(builder);
        }
        return names;
    }

}
