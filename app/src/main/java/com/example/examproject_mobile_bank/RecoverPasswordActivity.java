package com.example.examproject_mobile_bank;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.examproject_mobile_bank.data.DatabaseHandler;

public class RecoverPasswordActivity extends AppCompatActivity {


    EditText answer;
    EditText username;
    EditText mail;
    TextView recover_pass;
    Button recover_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        init();
    }

    private void init(){

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        final SQLiteDatabase database = db.getWritableDatabase();

        answer = (EditText) findViewById(R.id.recover_answer);
        username = (EditText) findViewById(R.id.recover_username);
        mail = (EditText) findViewById(R.id.recover_mail);
        recover_request = (Button) findViewById(R.id.recover_request);
        recover_pass = (TextView) findViewById(R.id.recovered_pass);

        recover_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recover_pass.setText(db.test_secret(database,username.getText().toString(),mail.getText().toString(), answer.getText().toString()));
            }
        });

    }



}
