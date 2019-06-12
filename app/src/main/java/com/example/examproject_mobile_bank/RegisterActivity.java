package com.example.examproject_mobile_bank;

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

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {


    EditText name;
    EditText pass;
    EditText phone;
    EditText mail;
    EditText street;
    EditText city;
    EditText zip;
    Button register;
    EditText date;
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
        street = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        zip = (EditText) findViewById(R.id.zipcode);
        register = (Button) findViewById(R.id.registeracc);
        date = (EditText) findViewById(R.id.register_bday);

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    date_dialog = new DatePicker();
                    date_dialog.show(getSupportFragmentManager(), "datePicker");
                }
                else{
                    date.setText(date_dialog.toString());
                }
            }
        });




        final ArrayList<EditText> all_text_fields = new ArrayList<>();
        all_text_fields.add(name);
        all_text_fields.add(pass);
        all_text_fields.add(phone);
        all_text_fields.add(mail);
        all_text_fields.add(street);
        all_text_fields.add(city);
        all_text_fields.add(zip);
        all_text_fields.add(date);

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
                        return;
                    }
                }
                User user = new User(name.getText().toString(),
                        phone.getText().toString(),
                        mail.getText().toString(),
                        street.getText().toString(),
                        city.getText().toString(),
                        zip.getText().toString(),
                        date.getText().toString()
                );

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
                System.out.println("ID: "+ id);
                db.first_account(database, String.valueOf(id));
                i.putExtra(getString(R.string.username), user);
                startActivity(i);
            }
        });

    }
}
