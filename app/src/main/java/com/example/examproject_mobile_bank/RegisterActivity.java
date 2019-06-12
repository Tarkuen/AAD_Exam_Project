package com.example.examproject_mobile_bank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.examproject_mobile_bank.data.DatabaseHandler;
import com.example.examproject_mobile_bank.model.DatePicker;
import com.example.examproject_mobile_bank.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnFocusChangeListener {


    EditText name;
    EditText pass;
    EditText phone;
    EditText mail;
    EditText street;
    EditText city;
    EditText zip;
    Button register;
    EditText date;
    EditText secret;
    DialogFragment date_dialog;

    DatabaseHandler db;
    SQLiteDatabase database;


    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();


    }


    private void init(){

        db = new DatabaseHandler(getApplicationContext());
        database = db.getWritableDatabase();

        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        mail = (EditText) findViewById(R.id.mail);
        mail.setOnFocusChangeListener(this);
        street = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        zip = (EditText) findViewById(R.id.zipcode);
        register = (Button) findViewById(R.id.registeracc);
        date = (EditText) findViewById(R.id.register_bday);
        date.setOnFocusChangeListener(this);
        secret = (EditText) findViewById(R.id.answer);


        final ArrayList<EditText> all_text_fields = new ArrayList<>();
        all_text_fields.add(name);
        all_text_fields.add(pass);
        all_text_fields.add(phone);
        all_text_fields.add(mail);
        all_text_fields.add(street);
        all_text_fields.add(city);
        all_text_fields.add(zip);
        all_text_fields.add(date);
        all_text_fields.add(secret);

        i = new Intent(getBaseContext(), HomeScreenActivity.class);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EditText field : all_text_fields) {
                    if(field.getText().toString().isEmpty()){
                        String warning = getString(R.string.reg_toast);
                        warning = warning.concat(getResources().getResourceEntryName(field.getId())).toUpperCase();
                        Toast toast = Toast.makeText(getBaseContext(), warning, Toast.LENGTH_LONG);
                        toast.show();
                        field.setTextColor(getResources().getColor(R.color.colorAccent));
                        return;
                    }
                    else{
                        field.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String now = sdf.format(new Date());
                try {
                    Date d1 = sdf.parse(now);
                    Date d2 = sdf.parse(date.getText().toString());
                    Calendar c = Calendar.getInstance();

                    c.setTimeInMillis(d2.getTime());
                    int mYear = c.get(Calendar.YEAR);
                    c.setTimeInMillis(d1.getTime());
                    int nYear = c.get(Calendar.YEAR);
                    if (nYear - mYear <= 15){
                        String warning = "Age Restricted: Must be 15 or older";
                        Toast toast = Toast.makeText(getBaseContext(), warning, Toast.LENGTH_LONG);
                        date.setTextColor(getResources().getColor(R.color.colorAccent));
                        toast.show();
                        return;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                User user = new User();

                SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.button_login) ,MODE_PRIVATE);
                sharedPreferences.edit().putString(getString(R.string.pass), pass.getText().toString()).apply();
                sharedPreferences.edit().putString(getString(R.string.username), name.getText().toString()).apply();



                String[] values = {name.getText().toString(),
                        pass.getText().toString(),
                        phone.getText().toString(),
                        mail.getText().toString(),
                        street.getText().toString(),
                        city.getText().toString(),
                        zip.getText().toString(),
                        date.getText().toString()
                };

                Long id = db.new_user(database, values);
                db.first_account(database, String.valueOf(id));
                user = db.fetch_user_by_ID(database, String.valueOf(id));
                db.new_secret(database, String.valueOf(user.getId()),getString(R.string.question),secret.getText().toString());
                i.putExtra(getString(R.string.username), user);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()){
            case R.id.mail:
                if(!v.hasFocus()){
                    if (!mail.getText().toString().contains("@")){
                        mail.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    else {
                        mail.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }

                break;
            case R.id.register_bday:
                if (hasFocus){
                    date_dialog = new DatePicker();
                    date_dialog.show(getSupportFragmentManager(), "datePicker");
                }
                else {
                    date.setText(date_dialog.toString());
                    break;
                }

        }

    }
}
