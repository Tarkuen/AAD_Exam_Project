package com.example.examproject_mobile_bank;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.examproject_mobile_bank.data.DatabaseHandler;
import com.example.examproject_mobile_bank.model.BankAccount;
import com.example.examproject_mobile_bank.model.DatePicker;
import com.example.examproject_mobile_bank.model.NemIDDialog;
import com.example.examproject_mobile_bank.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BillingActivity extends AppCompatActivity implements NemIDDialog.CallBackListener {

    Spinner accounts;
    EditText amount;
    EditText pbs;
    EditText date;
    Button paytransfer;
    DialogFragment date_dialog;
    ArrayList<BankAccount> allAccounts;
    ArrayAdapter<BankAccount> spinner_array;
    User user;
    Intent i1;

    DatabaseHandler db;
    SQLiteDatabase database;

    NemIDDialog nem_dia;
    String id;
    String name;
    String target;
    String t_amount;
    String payment_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        if (getIntent().hasExtra(getString(R.string.accounts))) {
            allAccounts = getIntent().getExtras().getParcelableArrayList(getString(R.string.accounts));
            assert allAccounts != null;
        }
        if (getIntent().hasExtra(getString(R.string.username))) {
            user = (User) getIntent().getParcelableExtra(getString(R.string.username));
        }

        init();
    }

    public void init() {

        db = new DatabaseHandler(getApplicationContext());
        database = db.getWritableDatabase();

        accounts = (Spinner) findViewById(R.id.account_spinner);
        spinner_array = new ArrayAdapter<BankAccount>(
                this, android.R.layout.simple_spinner_item, allAccounts
        );
        spinner_array.setDropDownViewResource(android.R.layout.simple_spinner_item);
        accounts.setAdapter(spinner_array);

        amount = (EditText) findViewById(R.id.bill_amount);
        pbs = (EditText) findViewById(R.id.pbs_number);
        date = (EditText) findViewById(R.id.bill_date);
        paytransfer = (Button) findViewById(R.id.planpay);

        i1 = new Intent(this, HomeScreenActivity.class);
        i1.putExtra(getString(R.string.username), user);

        final ArrayList<EditText> all_text_fields = new ArrayList<>();
        all_text_fields.add(amount);
        all_text_fields.add(pbs);
        all_text_fields.add(date);

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    date_dialog = new DatePicker();
                    date_dialog.show(getSupportFragmentManager(), "datePicker");
                } else {
                    date.setText(date_dialog.toString());
                }
            }
        });

        paytransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EditText field : all_text_fields) {
                    if (field.getText().toString().isEmpty()) {
                        String warning = getString(R.string.reg_toast);
                        warning = warning.concat(getResources().getResourceEntryName(field.getId())).toUpperCase();
                        Toast toast = Toast.makeText(getBaseContext(), warning, Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                }

                BankAccount f = (BankAccount) accounts.getSelectedItem();
                id = String.valueOf(f.getId());
                name = f.getName();
                if (name.equalsIgnoreCase("Pension")) {
                    boolean exec = handle_pension_account();
                    if (!exec) {
                        Toast t = Toast.makeText(getBaseContext(), R.string.message_age, Toast.LENGTH_LONG);
                        t.show();
                    }
                }
                target = pbs.getText().toString();
                t_amount = amount.getText().toString();
                payment_date = date.getText().toString();

                String input = " 0123 ";
                handle_dialog(input, "0123");


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
        if (nem_dia.isAuth()) {
//            TODO: ADD REPEAT CHECKBOX HERE
            int repeat = 0;
            String[] values = new String[]{String.valueOf(user.getId()), payment_date, t_amount, id, target, String.valueOf(repeat)};
            db.new_bill(database, values);
            Bundle extras_bundle = new Bundle();
            extras_bundle.putParcelable(getString(R.string.username), user);
            i1.putExtras(extras_bundle);
            startActivity(i1);
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
