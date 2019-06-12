package com.example.examproject_mobile_bank;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examproject_mobile_bank.data.DatabaseHandler;
import com.example.examproject_mobile_bank.model.BankAccount;
import com.example.examproject_mobile_bank.model.Bill;
import com.example.examproject_mobile_bank.model.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView n_view;
    private TextView accountName;
    private TextView department;
    private LinearLayout header;
    private ListView accounts_listview;

    private Intent i1;
    private Intent i2;
    private Intent i3;
    private Intent i4;

    private DatabaseHandler db;
    private SQLiteDatabase database;

    private User user;
    private ArrayList<BankAccount> allAccounts;

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

        n_view = (NavigationView) findViewById(R.id.n_view);
        n_view.bringToFront();
        n_view.setNavigationItemSelectedListener(this);
        header = (LinearLayout) n_view.getHeaderView(0);
        accountName = (TextView) header.getChildAt(1);
        department = (TextView) header.getChildAt(2);

        if (getIntent().hasExtra(getString(R.string.username))) {
            user = (User) getIntent().getParcelableExtra(getString(R.string.username));

            if (user != null) {
                String welcome = getString(R.string.home_toast) + " " + user.getName();
                accountName.setText(user.getName());
                department.setText(new String(getString(R.string.department)+": "+user.getDep()));
                Toast welcometoast = Toast.makeText(this, welcome, Toast.LENGTH_LONG);
                welcometoast.show();
            }
        }


        try {
            init();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void init() throws ParseException {
        db = new DatabaseHandler(getApplicationContext());
        database = db.getWritableDatabase();

        allAccounts = db.fetch_user_accounts(database, String.valueOf(user.getId()));
        db.handle_repeat_bills(database,allAccounts);

        accounts_listview = (ListView) findViewById(R.id.accounts_listview);

        ArrayAdapter<BankAccount> listview_adapter = new ArrayAdapter<BankAccount>(this, android.R.layout.simple_spinner_item, allAccounts );
        listview_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        accounts_listview.setAdapter(listview_adapter);

        i1 = new Intent(getBaseContext(), TransferActivity.class);
        i2 = new Intent(getBaseContext(), BillingActivity.class);
        i3 = new Intent(getBaseContext(), MainActivity.class);
        i4 = new Intent(getBaseContext(), RequestAccountActivity.class);

        try {
            final ArrayList<Bill> bills = db.fetch_all_due_user_bills(database, new String[]{String.valueOf(user.getId()), String.valueOf(0)});
            if (bills.size() > 0) {
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
    public void onBackPressed() {

        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        allAccounts = db.fetch_user_accounts(database, String.valueOf(user.getId()));
        Bundle extra_bundle = new Bundle();

        switch (menuItem.getItemId()) {
            case R.id.request_account:
                extra_bundle.putParcelableArrayList(getString(R.string.accounts), allAccounts);
                extra_bundle.putParcelable(getString(R.string.username), user);
                i4.putExtras(extra_bundle);
                startActivity(i4);
                break;

            case R.id.new_transfer:
                extra_bundle = new Bundle();
                extra_bundle.putParcelableArrayList(getString(R.string.accounts), allAccounts);
                extra_bundle.putParcelable(getString(R.string.username), user);
                i1.putExtras(extra_bundle);
                startActivity(i1);
                break;

            case R.id.menu_payment:
                extra_bundle.putParcelableArrayList(getString(R.string.accounts), allAccounts);
                extra_bundle.putParcelable(getString(R.string.username), user);
                i2.putExtras(extra_bundle);
                startActivity(i2);
                break;

            case R.id.nav_logout:
                startActivity(i3);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
