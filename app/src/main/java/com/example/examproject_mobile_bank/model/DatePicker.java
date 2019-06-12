package com.example.examproject_mobile_bank.model;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import java.util.Calendar;

public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    String year;
    String month;
    String dayOfMonth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        this.year = String.valueOf(year);
        this.month = String.valueOf(month);
        this.dayOfMonth = String.valueOf(dayOfMonth);
    }

    @Override
    public String toString() {
        return dayOfMonth+'/'+month+'/'+year;
    }


}