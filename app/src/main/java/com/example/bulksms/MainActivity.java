package com.example.bulksms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.bulksms.util.Utils;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** Views */
    private Button btnChooseFile;
    private ListView lvBatchList;
    private TextView tvBatchDetails;

    /** Constants */
    private static final int FILE_EXPLORER_CODE = 10;
    private static final String TAG = "MAIN_ACTIVITY";

    /** Variables */
    private ArrayList<Batch> batch_list;


    /**
     * TODO (1) Override onSaveInstanceState and save data on device orientation change
     * TODO (2) Use the savedInstanceState in onCreate load data and change visibility of views
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started");

        // setup views
        initViews();

        // setup listeners
        btnChooseFile.setOnClickListener(new EventChooseFile());
    }


    /**
     * Event on btnChooseFile click
     */
    private class EventChooseFile implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            try {
                startActivityForResult(intent, FILE_EXPLORER_CODE);
            } catch (android.content.ActivityNotFoundException e) {
                Utils.showNeutralAlertDialog(MainActivity.this, "Error, file explorer not found",
                        "Please install a file explorer to continue");
            }
        }
    }


    /**
     * Event on lvBatch list item click
     */
    private class EventBatchListItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            // Show custom dialog builder on item click here
            final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_send_message_dialog, null);
            final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setView(dialogView)
                    .setTitle("Enter message for Batch - " + (position+1))
                    .show();

            // Alert dialog send button listener
            dialogView.findViewById(R.id.btnViewSend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Read the edit text for message
                    EditText msg = dialogView.findViewById(R.id.edtViewMessageBody);
                    if(msg != null && (!msg.getText().toString().isEmpty())) {
                        // Send SMS to selected batch
                        batch_list.get(position).sendMessage(msg.getText().toString());
                        dialog.dismiss();
                    }
                }
            });
        }
    }


    /**
     * Triggers after startActivityForResult() returns result from another activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // File explorer activity
        if(resultCode == RESULT_OK && requestCode == FILE_EXPLORER_CODE) {
            if(data != null && data.getData() != null) {
                try {
                    // Read all the numbers into an array list
                    BatchManager manager = new BatchManager();
                    ArrayList<String> numbers_list = manager.readNumbersFromUri(MainActivity.this, data.getData());
                    batch_list = manager.createBatches(numbers_list, 50);

                    // Create a basic adapter and set the list view
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_list_item_1, manager.getBatchNames());
                    lvBatchList.setAdapter(adapter);
                    lvBatchList.setOnItemClickListener(new EventBatchListItemClick());

                    // Show details and list view items
                    String details;
                    if(manager.getBatchNames().size() == 0) {
                        Utils.showNeutralAlertDialog(MainActivity.this, "No numbers found",
                                "Please choose a file with usable numbers");
                        return;
                    } else if(manager.getBatchNames().size() == 1){
                        details = manager.getBatchNames().size() + " Batch with " + numbers_list.size() + " numbers";
                    } else {
                        details = manager.getBatchNames().size() + " Batches with " + numbers_list.size() + " numbers";
                    }
                    tvBatchDetails.setTypeface(null, Typeface.BOLD);
                    tvBatchDetails.setText(details);
                    tvBatchDetails.setVisibility(View.VISIBLE);
                    lvBatchList.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        else if(resultCode == RESULT_CANCELED) {
            Utils.showNeutralAlertDialog(MainActivity.this, "No file chosen",
                    "Please choose a file with list of phone numbers to continue");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Setup views for this activity
     */
    private void initViews() {
        btnChooseFile = findViewById(R.id.btnChooseFile);
        lvBatchList = findViewById(R.id.lvBatchList);
        tvBatchDetails = findViewById(R.id.tvBatchDetails);

        Log.d(TAG, "onCreate: View setup done");
    }

}
