package com.example.examproject_mobile_bank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.examproject_mobile_bank.data.DatabaseHandler;
import com.example.examproject_mobile_bank.model.BankAccount;
import com.example.examproject_mobile_bank.model.NemIDDialog;
import com.example.examproject_mobile_bank.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransferActivity extends AppCompatActivity implements NemIDDialog.CallBackListener {

    Switch switch1;
    Spinner spinner1;
    Spinner spinner2;
    ArrayList<BankAccount> allAccounts;
    EditText amount;
    EditText account;
    Button transfer;
    User user;
    NemIDDialog nem_dia;

    Intent i1;

    private DatabaseHandler db;
    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        if (getIntent().hasExtra(getString(R.string.accounts))) {
            allAccounts = getIntent().getParcelableArrayListExtra(getString(R.string.accounts));
            assert allAccounts != null;
        }
        if (getIntent().hasExtra(getString(R.string.username))) {
            user = (User) getIntent().getParcelableExtra(getString(R.string.username));
        }

        init();


    }

    public void init() {

        amount = (EditText) findViewById(R.id.amount);
        account = (EditText) findViewById(R.id.account);
        transfer = (Button) findViewById(R.id.transfer);

        switch1 = (Switch) findViewById(R.id.switch1);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<BankAccount> spinner_array = new ArrayAdapter<BankAccount>(
                this, android.R.layout.simple_spinner_item, allAccounts
        );
        spinner_array.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner1.setAdapter(spinner_array);
        spinner2.setAdapter(spinner_array);

        db = new DatabaseHandler(getApplicationContext());
        database = db.getWritableDatabase();
        i1 = new Intent(this, HomeScreenActivity.class);
        i1.putExtra(getString(R.string.username), user);

        actions();

    }

    public void actions() {

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    account.setVisibility(View.INVISIBLE);
                    account.setText("");
                    spinner2.setVisibility(View.VISIBLE);
                } else {
                    account.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.INVISIBLE);
                }
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankAccount f = (BankAccount) spinner1.getSelectedItem();
                int trans_amount = Integer.valueOf(amount.getText().toString());
                if (trans_amount> f.getHoldings()){
                    Toast t = Toast.makeText(getBaseContext(), R.string.amount_check, Toast.LENGTH_LONG);
                    t.show();
                    return;
                }
                if (switch1.isChecked()) {
                    BankAccount to = (BankAccount) spinner2.getSelectedItem();

                    if(f.getName().equalsIgnoreCase("Budget") || f.getName().equalsIgnoreCase("Savings")){
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.MONTH, 1);
                        c.set(Calendar.DATE, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date next_month = c.getTime();
                        String pay_date = sdf.format(next_month);
                        int repeat = 0;
                        String[] values = new String[]{String.valueOf(user.getId()), pay_date, String.valueOf(trans_amount), String.valueOf(f.getId()), to.getName(), String.valueOf(repeat)};

                        db.new_bill(database,values);
                        startActivity(i1);
                    }
                    else{
                        db.internal_transfer(database, trans_amount, user.getId(), f.getName(), to.getName());
                        startActivity(i1);
                    }

                } else {
//                  TODO: Toast Message on mainscreen.


                    handle_dialog(" 0123 ", "0123");
                }

            }
        });
    }

    private void handle_dialog(String input, String pass) {
        nem_dia = new NemIDDialog();
        Bundle dialog_arguments = new Bundle();

        dialog_arguments.putString("input", input);
        dialog_arguments.putString("pass", pass);

        nem_dia.setArguments(dialog_arguments);
        nem_dia.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        BankAccount f = (BankAccount) spinner1.getSelectedItem();

        if (f.getName().equalsIgnoreCase("Pension")) {
            boolean exec = handle_pension_account();
            if (!exec) {
//                        TODO: Add Toast Explaining belov 70 age
            }
        }

        if (nem_dia.isAuth()) {
            int trans_amount = Integer.valueOf(amount.getText().toString());
            String to = account.getText().toString();

            db.reduce_funds(database, new String[]{String.valueOf(f.getId())}, trans_amount, to);

            startActivity(i1);

            Log.i("TRANSFER INFO", "TRANSFERED AMOUTN FROM ACCOUNT: " + f.toString() + " TO: " + to);
        } else {
            Toast toast = Toast.makeText(this, R.string.alert_wrong, Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    public boolean handle_pension_account() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String now = sdf.format(new Date());
        try {
            Date d1 = sdf.parse(now);
            Date d2 = sdf.parse(user.getB_day());
            Calendar c = Calendar.getInstance();

            c.setTimeInMillis(d2.getTime());
            int mYear = c.get(Calendar.YEAR);
            c.setTimeInMillis(d1.getTime());
            int nYear = c.get(Calendar.YEAR);

            if (nYear - mYear >= 70) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
