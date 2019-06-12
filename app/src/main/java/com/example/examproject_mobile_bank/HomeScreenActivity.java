package com.example.examproject_mobile_bank;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examproject_mobile_bank.data.DatabaseHandler;
import com.example.examproject_mobile_bank.model.BankAccount;
import com.example.examproject_mobile_bank.model.Bill;
import com.example.examproject_mobile_bank.model.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawer;
    private TextView accountName;

    private Button payment;
    private Button transfer;
    private Button requestAcc;

    private Intent i1;
    private Intent i2;
    private Intent i3;

    private DatabaseHandler db;
    private SQLiteDatabase database;


    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

//        NAVBAR
        Toolbar toolbar = findViewById(R.id.n_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.welcome);
        drawer = findViewById(R.id.d_layout);
        ActionBarDrawerToggle t_toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.n_d_o, R.string.n_d_c);
        drawer.addDrawerListener(t_toggle);

        t_toggle.syncState();

        NavigationView navbar = findViewById(R.id.n_view);
        LinearLayout header = (LinearLayout) navbar.getHeaderView(0);
        TextView account_name = (TextView) header.getChildAt(1);

        if(getIntent().getExtras().containsKey(getString(R.string.username))){
            user = (User) getIntent().getExtras().get(getString(R.string.username));

            if (user != null) {
                String welcome = getString(R.string.home_toast)+" "+ user.getName();
                account_name.setText(user.getName());
                Toast welcometoast = Toast.makeText(this, welcome, Toast.LENGTH_LONG);
                welcometoast.show();
            }
        }



        init();
    }

    private void init(){

        db = new DatabaseHandler(getApplicationContext());
        database = db.getWritableDatabase();

        payment = (Button) findViewById(R.id.payment);
        transfer = (Button) findViewById(R.id.transfer);
        requestAcc = (Button) findViewById(R.id.requestaccount);

        payment.setOnClickListener(this);
        requestAcc.setOnClickListener(this);
        transfer.setOnClickListener(this);

        i1 = new Intent(getBaseContext(), TransferActivity.class);
        i2 = new Intent(getBaseContext(), BillingActivity.class);

        try {
            final ArrayList<Bill> bills = db.fetch_all_due_user_bills(database, new String[] {String.valueOf(user.getId() ) });
            if (bills.size() > 0){
                final Snackbar snb = Snackbar.make(drawer, R.string.message_bill, Snackbar.LENGTH_INDEFINITE);
                snb.setAction(R.string.message_pay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.pay_bills(database, bills);
                        snb.dismiss();
                    }
                });
                snb.show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.transfer:
                ArrayList<BankAccount> allAccounts = db.fetch_user_accounts(database, String.valueOf(user.getId()));
                i1.putExtra(getString(R.string.accounts), allAccounts);
                i1.putExtra(getString(R.string.username), user);
                startActivity(i1);
                break;

            case R.id.payment:
                ArrayList<BankAccount> user_accounts = db.fetch_user_accounts(database, String.valueOf(user.getId()));
                i2.putExtra(getString(R.string.accounts), user_accounts);
                i2.putExtra(getString(R.string.username), user);
                startActivity(i2);
                break;
        }

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }


}
