package com.example.examproject_mobile_bank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.examproject_mobile_bank.data.DatabaseHandler;
import com.example.examproject_mobile_bank.model.BankAccount;
import com.example.examproject_mobile_bank.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button login;
    private Button register;
    private EditText usern;
    private EditText pass;
    private SharedPreferences sharedPreferences;
    private Intent i1;
    private Intent i2;
    private DatabaseHandler db;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();
//        db.onUpgrade(database);
        db.onCreate(database);

    }


    private void init(){

        db = new DatabaseHandler(getApplicationContext());
        database = db.getWritableDatabase();

        login = (Button) findViewById(R.id.button_login);
        register = (Button) findViewById(R.id.button_register);
        usern = (EditText) findViewById(R.id.usern);
        pass = (EditText) findViewById(R.id.password);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.button_login) ,MODE_PRIVATE);

        i1 = new Intent(getBaseContext(), RegisterActivity.class);
        i2 = new Intent(getBaseContext(), HomeScreenActivity.class);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_login:

                String result1 = sharedPreferences.getString(getString(R.string.username), "-1");

                assert result1 != null;
                if(result1.equalsIgnoreCase("-1") || !result1.equalsIgnoreCase(usern.getText().toString())){
                    Log.w("LOGIN 500", "LOGIN: INCORRECT USERNAME");
                    System.out.println("LOGIN: INCORRECT USERNAME");
                    return;
                }

                String result2 =sharedPreferences.getString(getString(R.string.pass), "-1");

                assert result2 != null;
                if(result2.equalsIgnoreCase("-1") || !result2.equalsIgnoreCase(pass.getText().toString())){
                    Log.w("LOGIN 500", "LOGIN: INCORRECT PASSWORD");
                    System.out.println("LOGIN: INCORRECT PASSWORD");
                    return;
                }
                Log.w("LOGIN 200", "LOGIN SUCCESFULL");

                System.out.println("LOGIN SUCCESFULL");
                String[] where = {usern.getText().toString(), pass.getText().toString() };
                User user =  db.fetch_user_login(database, where);
                Bundle extra_bundle = new Bundle();
                extra_bundle.putParcelable(getString(R.string.username), user);
                i2.putExtras(extra_bundle);
                startActivity(i2);
                break;

            case R.id.button_register :
                startActivity(i1);
                break;
        }


    }
}
