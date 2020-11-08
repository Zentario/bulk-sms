package com.example.bulksms.util;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import java.util.regex.Pattern;

public class Utils {

    // Reusable neutral alert dialog template
    public static void showNeutralAlertDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    // Validation check for a phone number
    public static boolean isPhoneNumber(String number) {
        return Pattern.matches("^\\d[10]$", number);
    }

}
