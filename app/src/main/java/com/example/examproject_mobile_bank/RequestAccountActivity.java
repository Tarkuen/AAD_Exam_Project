package com.example.examproject_mobile_bank;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examproject_mobile_bank.data.DatabaseHandler;
import com.example.examproject_mobile_bank.model.BankAccount;
import com.example.examproject_mobile_bank.model.Bill;
import com.example.examproject_mobile_bank.model.User;

import java.util.ArrayList;

public class RequestAccountActivity extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener{

    User user;
    Spinner spinner;
    Button request;
    EditText monthly_deposit;
    TextView deposit_text;
    ArrayList<String> account_names;
    ArrayList<BankAccount> user_accounts;

    private DatabaseHandler db;
    private SQLiteDatabase database;

    ArrayAdapter<String> spinner_array;
    Intent i1;



    enum accountsEnum {
        Budget,
        Savings,
        Pension,
        Business
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_account);

        if (getIntent().hasExtra(getString(R.string.username))) {
            user = (User) getIntent().getParcelableExtra(getString(R.string.username));
        }
        i1 = new Intent(this, HomeScreenActivity.class);
        i1.putExtra(getString(R.string.username), user);
        init();
    }

    public void init() {

        db = new DatabaseHandler(getApplicationContext());
        database = db.getWritableDatabase();
        monthly_deposit = (EditText) findViewById(R.id.monthly_deposit);
        monthly_deposit.setVisibility(View.INVISIBLE);
        deposit_text = (TextView) findViewById(R.id.deposit_text);
        deposit_text.setVisibility(View.INVISIBLE);
        spinner = (Spinner) findViewById(R.id.request_spinner);

        user_accounts = db.fetch_user_accounts(database, String.valueOf(user.getId()));

        account_names = new ArrayList<>();
        for (accountsEnum value : accountsEnum.values()) {
            account_names.add(String.valueOf(value));
        }

        spinner_array = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, account_names
        );

        spinner_array.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinner_array);

        spinner.setOnItemSelectedListener(this);

        request = (Button) findViewById(R.id.requestaccount_button);
        request.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String name = (String) spinner.getItemAtPosition(position);
        BankAccount ba = new BankAccount();
        for (BankAccount user_account : user_accounts) {
            if(user_account.getName().equalsIgnoreCase(name)){
                ba = user_account;
            }
        }

        switch (name){
            case "Savings":
                monthly_deposit.setVisibility(View.VISIBLE);
                deposit_text.setVisibility(View.VISIBLE);
                Bill savings_bill = db.fetch_repeat_bills(database, String.valueOf(user.getId()),String.valueOf(ba.getId()));
                try{
                    int am = savings_bill.getAmount();
                    monthly_deposit.setText(String.valueOf(am));
                } catch (NullPointerException ex){
                    monthly_deposit.setText(String.valueOf(0));
                }
                break;

            case "Budget":
                monthly_deposit.setVisibility(View.VISIBLE);
                deposit_text.setVisibility(View.VISIBLE);
                Bill budget_bill = db.fetch_repeat_bills(database, String.valueOf(user.getId()),String.valueOf(ba.getId()));
                System.out.println(budget_bill);
                monthly_deposit.setText(String.valueOf(budget_bill.getAmount()));
                break;
            case "Pension":
                monthly_deposit.setVisibility(View.INVISIBLE);
                deposit_text.setVisibility(View.INVISIBLE);
                break;
            case "Business":
                monthly_deposit.setVisibility(View.INVISIBLE);
                deposit_text.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

//        parent.setSelection(0);
    }

    @Override
    public void onClick(View v) {
        String name = (String) spinner.getSelectedItem();
        BankAccount ba = new BankAccount();
        for (BankAccount user_account : user_accounts) {
            if(user_account.getName().equalsIgnoreCase(name)){
                ba = user_account;
            }
        }
        Toast t = Toast.makeText(getBaseContext(), getString(R.string.account_update), Toast.LENGTH_SHORT);

        switch ((String) spinner.getSelectedItem()) {
            case "Business":
                boolean def = false;
                for (BankAccount u_a : user_accounts) {
                    if (u_a.getName().equalsIgnoreCase("Default")) {
                        def = true;
                    } else if (u_a.getName().equalsIgnoreCase("Business")) {
                        t.show();
                        return;
                    }
                }
                if (def) {
                    db.new_account(database, new String[]{
                            accountsEnum.Business.toString(),
                            String.valueOf(user.getId())
                    });
                    startActivity(i1);
                    finish();
                } else {
                    Toast t1 = Toast.makeText(getBaseContext(), getString(R.string.no_default), Toast.LENGTH_SHORT);
                    t1.show();
                }
                break;

            case "Pension":
                for (BankAccount u_a : user_accounts) {
                    if (u_a.getName().equalsIgnoreCase("Pension")) {
                        t.show();
                        return;
                    }
                }
                db.new_account(database, new String[]{
                        accountsEnum.Pension.toString(),
                        String.valueOf(user.getId())
                });
                startActivity(i1);
                finish();

                break;
            case "Savings":
                for (BankAccount u_a : user_accounts) {
                    if (u_a.getName().equalsIgnoreCase("Savings")) {
                        t.show();
                        db.update_repeat_bill(database,String.valueOf(user.getId()),String.valueOf(u_a.getId()),monthly_deposit.getText().toString());
                        startActivity(i1);
                        return;
                    }
                }
                Long account_id = db.new_account(database, new String[]{
                        accountsEnum.Savings.toString(),
                        String.valueOf(user.getId())
                });
                db.update_repeat_bill(database,String.valueOf(user.getId()),String.valueOf(account_id),monthly_deposit.getText().toString());
                startActivity(i1);
                break;

            case "Budget":
                for (BankAccount u_a : user_accounts) {
                    if (u_a.getName().equalsIgnoreCase("Budget")) {
                        db.update_repeat_bill(database,String.valueOf(user.getId()),String.valueOf(u_a.getId()),monthly_deposit.getText().toString());
                        startActivity(i1);
                        return;
                    }
                }
                break;
        }
    }


}

