package com.aarushsystems.goatdiary.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.aarushsystems.goatdiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DialogDateUtil {

    private String message;
    private Context context;

    public DialogDateUtil(Context context, String message) {
        Log.i("CUSTOM", "const");
        this.context = context;
        this.message = message;
    }

    public DialogDateUtil(Context context) {
        this.context = context;
    }

    public void showMessage() {
        Log.i("CUSTOM", "showing message...");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(context.getString(R.string.okay),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.button_blue));
    }

    public void showMessage(String message) {
        Log.i("CUSTOM", "showing message...");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(context.getString(R.string.okay),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.button_blue));
    }

    public boolean isDateInPast(String dateData) {
        Calendar calendar = Calendar.getInstance();
        String todayDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.YEAR);
        Date date = null;
        Date today = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateData);
            today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(todayDate);
            Log.i("CUSTOM", "Today = " + today.toString() + " date = " + date.toString());
        } catch (ParseException e) {
            Log.i("CUSTOM", "Dialog util exception = " + e.getMessage());
        }
        if (today.compareTo(date) > 0) {
            return true;
        } else if (today.compareTo(date) < 0) {
            return false;
        }
        // return true if today;s date is allowed
        // else return false
        return true;
    }

    public boolean isDateInFuture(String dateData) {
        Calendar calendar = Calendar.getInstance();
        String todayDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.YEAR);
        Date date = null;
        Date today = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateData);
            today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(todayDate);
            Log.i("CUSTOM", "Today = " + today.toString() + " date = " + date.toString());
        } catch (ParseException e) {
            Log.i("CUSTOM", "Dialog util exception = " + e.getMessage());
        }
        if (today.compareTo(date) > 0) {
            return false;
        } else if (today.compareTo(date) < 0) {
            return true;
        }
        // return true if today;s date is allowed
        // else return false
        return false;
    }

    public boolean isProposedDateBeforeDate(String proposedDate, String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date proposed = simpleDateFormat.parse(proposedDate);
        Date current = simpleDateFormat.parse(date);
        assert proposed != null;
        if (proposed.compareTo(current) > 0) {
            return false;
        } else if (proposed.compareTo(current) < 0) {
            return true;
        }
        // return false if today;s date is allowed
        // else return true
        return false;
    }

    public boolean isProposedDateAfterDate(String proposedDate, String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date proposed = simpleDateFormat.parse(proposedDate);
        Date current = simpleDateFormat.parse(date);
        assert proposed != null;
        if (proposed.compareTo(current) > 0) {
            return true;
        } else if (proposed.compareTo(current) < 0) {
            return false;
        }
        // return false if today;s date is allowed
        // else return true
        return false;
    }

    public boolean isDateEqual(String date1, String date2) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateD1 = simpleDateFormat.parse(date1);
        Date dateD2 = simpleDateFormat.parse(date2);
        assert dateD1 != null;
        if (dateD1.compareTo(dateD2) == 0) {
            return true;
        }
        return false;
    }

    public String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.YEAR);
    }

    public int getAge(String birthdate) {
        int age = 0;
        Calendar calendar = Calendar.getInstance();
        String todayDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.YEAR);
        Date date = null;
        Date today = null;
        Log.i("CUSTOM", "birthdate = " + birthdate);
        int years = calendar.get(Calendar.YEAR) - Integer.parseInt(birthdate.split("/")[2]);
        int months = calendar.get(Calendar.MONTH) + 1 - Integer.parseInt(birthdate.split("/")[1]);
        Log.i("CUSTOM", "years = " + years);
        if (years != 0) {
            age = years * 12 + months;
        } else {
            age = months;
        }
        return age;
    }

    public void showCriticalMessage(final Activity activity, final String flag) {
        Log.i("CUSTOM", "showing critical message...");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = "Please Check your Internet Connection and then try again.";
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.okay),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (flag.equals("d")) {
                            activity.finish();
                        }
                    }
                });
        builder.show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.button_blue));
    }
}
