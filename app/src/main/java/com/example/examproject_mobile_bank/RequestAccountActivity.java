package com.example.examproject_mobile_bank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.examproject_mobile_bank.data.DatabaseHandler;
import com.example.examproject_mobile_bank.model.BankAccount;
import com.example.examproject_mobile_bank.model.User;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RequestAccountActivity extends AppCompatActivity implements View.OnClickListener{

    User user;
    Spinner spinner;
    Button request;
    ArrayList<String> account_names;
    ArrayList<BankAccount> user_accounts;

    private DatabaseHandler db;
    private SQLiteDatabase database;

    ArrayAdapter<String> spinner_array;
    Intent i1;

    enum accountsEnum {
        Savings,
        Pension,
        Business
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_account);

        if(getIntent().getExtras().containsKey(getString(R.string.username))){
            user = (User) getIntent().getExtras().get(getString(R.string.username));
        }
        i1 = new Intent(this, HomeScreenActivity.class);
        init();
    }

    public void init(){

        db = new DatabaseHandler(getApplicationContext());
        database = db.getWritableDatabase();
        spinner = (Spinner) findViewById(R.id.request_spinner);

        user_accounts = db.fetch_user_accounts(database, String.valueOf(user.getId()));

        account_names = new ArrayList<>();
        for (accountsEnum value : accountsEnum.values()) { account_names.add(String.valueOf(value));
        }

        spinner_array= new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, account_names
        );

        spinner_array.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinner_array);

        request = (Button) findViewById(R.id.requestaccount_button);
        request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch ((String) spinner.getSelectedItem()){
            case "Business":
                boolean def = false;
                for (BankAccount u_a : user_accounts) {
                    if (u_a.getName().equalsIgnoreCase("Default")){
                        def = true;
                    }
                    else if (u_a.getName().equalsIgnoreCase("Business")){
                        def = false;
                    }
                }
                if (def){
                    db.new_account(database, new String[]{
                            accountsEnum.Business.toString(),
                            String.valueOf(user.getId())
                    });
                    startActivity(i1);
                    }

                else {
//                    TODO: Add Toast Explaining problem.
                }
                break;
            case "Pension":
//                TODO: Add to Transfer Money & Billings on Account Spinner

                boolean pen = true;
                for (BankAccount u_a: user_accounts) {
                    if (u_a.getName().equalsIgnoreCase("Pension")){
                        pen= false;
                    }
                }
                if (pen){
                    db.new_account(database, new String[] {
                            accountsEnum.Pension.toString(),
                            String.valueOf(user.getId())
                    });
                    startActivity(i1);
                }
                else{
//                    TODO: Add Toast Explaining Already Existing.
                }
                break;
            case "Savings":

                boolean sav = true;
                for (BankAccount u_a : user_accounts) {
                    if (u_a.getName().equalsIgnoreCase("Savings")){
                        sav = false;
                    }
                }
                if (sav){
                    db.new_account(database, new String[] {
                            accountsEnum.Savings.toString(),
                            String.valueOf(user.getId())
                    });
                    startActivity(i1);
                }
                else{
//                    TODO: EXPLAIN PRBOELM
                }
                break;
        }

    }
}
