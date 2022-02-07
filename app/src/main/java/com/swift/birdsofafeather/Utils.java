package com.swift.birdsofafeather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;

public class Utils {
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        else return str.equals("");
    }

    public static int toIntNullsafe(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        return Integer.parseInt(str);
    }

    public static boolean hasPermission(Context context, String permission) {
        int res = context.checkSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static void showAlert(Activity activity, String message){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder.setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {dialog.cancel();})
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
