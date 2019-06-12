package com.example.examproject_mobile_bank.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.examproject_mobile_bank.model.BankAccount;
import com.example.examproject_mobile_bank.model.Bill;
import com.example.examproject_mobile_bank.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "KEABANK.db";

    private static final String accounts = "ACCOUNTS_table";
    private static final String account_ID = "ID";
    private static final String account_name = "acc_name";
    private static final String account_user = "acc_user";
    private static final String account_holding = "acc_holding";
    private static int account_rowID = 1;


    private static final String user = "USERS_table";
    private static final String user_ID = "ID";
    private static final String user_username = "user_username";
    private static final String user_password = "user_password";
    private static final String user_phone = "user_phone";
    private static final String user_mail = "user_mail";
    private static final String user_street = "user_street";
    private static final String user_city = "user_city";
    private static final String user_zip = "user_zip";
    private static final String user_dep = "user_dep";
    private static final String user_bday = "user_bday";

    private static final String bills = "BILLS_TABLE";
    private static final String bill_id = "ID";
    private static final String bill_user = "bill_user";
    private static final String bill_date = "bill_date";
    private static final String bill_amount = "bill_amount";
    private static final String bill_account = "bill_account";
    private static final String bill_target = "bill_target";


    enum departments {
        Copenhagen,
        Odense
    }

    enum accountsEnum {
        Savings,
        Budget,
        Pension,
        Default,
        Business
    }

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(create_users());
        } catch (RuntimeException e) {
            Log.w("TABLE EXCEPTION", e.getMessage());
        }
        try {
            db.execSQL(create_accounts());
        } catch (RuntimeException e) {
            Log.w("TABLE EXCEPTION", e.getMessage());
        }

        try {
            db.execSQL(create_bills());
        } catch (RuntimeException e) {
            Log.w("TABLE EXCEPTION", e.getMessage());
        }

    }

    public void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + user);
        db.execSQL("DROP TABLE IF EXISTS " + accounts);
        db.execSQL("DROP TABLE IF EXISTS "+ bills);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private String create_users() {
        return "CREATE TABLE " + user
                + " ( " +

                user_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                user_username + " TEXT, " +
                user_password + " TEXT, " +
                user_mail + " TEXT, " +
                user_phone + " TEXT, " +
                user_street + " TEXT, " +
                user_city + " TEXT, " +
                user_zip + " INTEGER, " +
                user_dep + " TEXT " +

                " ); ";
    }

    private String create_accounts() {
        return "CREATE TABLE " + accounts
                + " ( " +
                account_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                account_name + " TEXT, " +
                account_user + " INTEGER, " +
                account_holding + " INTEGER " +
                " ); ";
    }

    public String create_bills() {
        return "CREATE TABLE " + bills
                + " ( " +
                bill_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                bill_user + " INTEGER, " +
                bill_date + " TEXT, " +
                bill_amount + " INTEGER, " +
                bill_account + " TEXT, " +
                bill_target + " TEXT " +
                " ); ";
    }

    public long new_user(SQLiteDatabase db, String[] values) {
        ContentValues subject_values = new ContentValues();

        subject_values.put(user_username, values[0]);
        subject_values.put(user_password, values[1]);
        subject_values.put(user_phone, values[2]);
        subject_values.put(user_mail, values[3]);
        subject_values.put(user_street, values[4]);
        subject_values.put(user_city, values[5]);
        subject_values.put(user_zip, values[6]);
        subject_values.put(user_bday, values[7]);
        if (Integer.valueOf(values[6]) > 5000) {
            String department = String.valueOf(departments.Copenhagen);
            subject_values.put(user_dep, department);
        } else {
            String department = String.valueOf(departments.Odense);
            subject_values.put(user_dep, department);
        }

        return db.insert(user, null, subject_values);

    }

    public User fetch_user_ID(SQLiteDatabase db, String value) {

        Cursor result = db.query(user, null, "ID = ?", new String[]{value}, null, null, null);

        int id = -1;
        String name = "";
        String phone = "";
        String mail = "";
        String street = "";
        String city = "";
        String zip = "";
        String department = "";
        String b_day = "";
        while (result.moveToNext()) {
            id = result.getInt(result.getColumnIndex(user_ID));
            name = result.getString(result.getColumnIndex(user_username));
            phone = result.getString(result.getColumnIndex(user_phone));
            mail = result.getString(result.getColumnIndex(user_mail));
            street = result.getString(result.getColumnIndex(user_street));
            city = result.getString(result.getColumnIndex(user_username));
            zip = result.getString(result.getColumnIndex(user_zip));
            department = result.getString(result.getColumnIndex(user_dep));
            b_day = result.getString(result.getColumnIndex(user_bday));
        }
        result.close();
        return new User(id, name, phone, mail, street, city, zip, department, user_bday);
    }

    public User fetch_user_login(SQLiteDatabase db, String[] values) {

        Cursor result = db.query(user, null, user_username + " = ? AND " + user_password + " = ?", values, null, null, null);

        int id = -1;
        String name = "";
        String phone = "";
        String mail = "";
        String street = "";
        String city = "";
        String zip = "";
        String department = "";
        String b_day = "";

        while (result.moveToNext()) {
            id = result.getInt(result.getColumnIndex(user_ID));
            name = result.getString(result.getColumnIndex(user_username));
            phone = result.getString(result.getColumnIndex(user_phone));
            mail = result.getString(result.getColumnIndex(user_mail));
            street = result.getString(result.getColumnIndex(user_street));
            city = result.getString(result.getColumnIndex(user_username));
            zip = result.getString(result.getColumnIndex(user_zip));
            department = result.getString(result.getColumnIndex(user_dep));
            b_day = result.getString(result.getColumnIndex(user_bday));
        }

        result.close();
        return new User(id, name, phone, mail, street, city, zip, department, b_day);
    }

    public ArrayList<BankAccount> fetch_all_accounts(SQLiteDatabase db) {
        Cursor result = db.query(accounts, null, null, null, null, null, null);
        ArrayList<BankAccount> allAccounts = new ArrayList<>();
        while (result.moveToNext()) {

            int id = result.getInt(result.getColumnIndex(account_ID));
            String name = result.getString(result.getColumnIndex(account_name));
            int user = result.getInt(result.getColumnIndex(account_user));
            User u = fetch_user_ID(db, String.valueOf(user));

            int holding = result.getInt(result.getColumnIndex(account_holding));

            BankAccount b = new BankAccount(id, name, u, user, holding);

            allAccounts.add(b);
        }
        return allAccounts;
    }

    public ArrayList<BankAccount> fetch_user_accounts(SQLiteDatabase db, String values) {
        ArrayList<BankAccount> allAccounts = new ArrayList<>();

        Cursor result = db.query(accounts, null, account_user + " = ?", new String[]{values}, null, null, null);

        int id = -1;
        String name = "";
        int user_id = -1;
        int holding = 0;


        while (result.moveToNext()) {
            id = result.getInt(result.getColumnIndex(account_ID));
            name = result.getString(result.getColumnIndex(account_name));
            user_id = result.getInt(result.getColumnIndex(account_user));
            holding = result.getInt(result.getColumnIndex(account_holding));

            BankAccount bank = new BankAccount(id, name, user_id, holding);
            allAccounts.add(bank);
        }
        result.close();

        return allAccounts;
    }

    public void first_account(SQLiteDatabase db, String values) {
        ContentValues subject_values = new ContentValues();

        User u = fetch_user_ID(db, values);
        int id = u.getId();

        subject_values.put(account_name, String.valueOf(accountsEnum.Default));
        subject_values.put(account_user, id);
        subject_values.put(account_holding, 1000);

        db.insert(accounts, null, subject_values);

        subject_values.put(account_name, String.valueOf(accountsEnum.Budget));
        subject_values.put(account_user, id);
        subject_values.put(account_holding, 1000);

        db.insert(accounts, null, subject_values);
    }

    public void new_account(SQLiteDatabase db, String[] values) {
        ContentValues subject_values = new ContentValues();

        subject_values.put(account_name, values[0]);
        subject_values.put(account_user, values[1]);
        subject_values.put(account_holding, 0);

        db.insert(accounts, null, subject_values);
    }

    public void internal_transfer(SQLiteDatabase db, int amount, int user_id, String name1, String name2) {
//        From
        ContentValues f_values = new ContentValues();

        String where = account_user + " = ? AND " + account_name + " = ?";
        Cursor result = db.query(accounts, null, where, new String[]{String.valueOf(user_id), name1}, null, null, null);

        while (result.moveToNext()) {
            String id = String.valueOf(result.getInt(result.getColumnIndex(account_ID)));
            int holding = result.getInt(result.getColumnIndex(account_holding));
            f_values.put(account_holding, (holding - amount));
            db.update(accounts, f_values, account_ID + " = ?", new String[]{id});
        }
        result.close();

//        To
        ContentValues t_values = new ContentValues();
        result = db.query(accounts, null, where, new String[]{String.valueOf(user_id), name2}, null, null, null);

        while (result.moveToNext()) {
            String id = String.valueOf(result.getInt(result.getColumnIndex(account_ID)));
            int holding = result.getInt(result.getColumnIndex(account_holding));
            t_values.put(account_holding, holding + amount);
            System.out.println(db.update(accounts, t_values, account_ID + " = ?", new String[]{id}));
        }

        result.close();


    }

    public void reduce_funds(SQLiteDatabase db, String[] values, int amount, String to) {
        ContentValues f_values = new ContentValues();

        String where = account_ID+" = ?";
        Cursor result = db.query(accounts, null, where, values, null, null, null);

        while (result.moveToNext()) {
            String id = String.valueOf(result.getInt(result.getColumnIndex(account_ID)));
            int holding = result.getInt(result.getColumnIndex(account_holding));
            f_values.put(account_holding, (holding - amount));
            db.update(accounts, f_values, account_ID + " = ?", new String[]{id});
        }
        result.close();

    }

    public void new_bill(SQLiteDatabase db, String[] values){
        ContentValues subject_values = new ContentValues();

        subject_values.put(bill_user, values[0]);
        subject_values.put(bill_date, values[1]);
        subject_values.put(bill_amount, values[2]);
        subject_values.put(bill_account, values[3]);
        subject_values.put(bill_target, values[4]);

        db.insert(bills, null, subject_values);
    }

    public ArrayList<Bill> fetch_all_due_user_bills(SQLiteDatabase db, String[] values) throws ParseException {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String now = sdf.format(new Date());
        Date d1 = sdf.parse(now);

        String where = bill_user+" = ?";

        Cursor result = db.query(bills, null, where, values, null, null, null);

        ArrayList<Bill> due_bills = new ArrayList<>();

        while (result.moveToNext()) {

            int id = result.getInt(result.getColumnIndex(bill_id));
            int user = result.getInt(result.getColumnIndex(bill_user));
            String b_date = result.getString(result.getColumnIndex(bill_date));
            int amount = result.getInt(result.getColumnIndex(bill_amount));
            int account = result.getInt(result.getColumnIndex(bill_account));
            int target = result.getInt(result.getColumnIndex(bill_target));

            Date bill_date = sdf.parse(b_date);

            if (bill_date.before(d1) || bill_date.compareTo(d1)==0){
                Bill b = new Bill(id, user, b_date, amount, account, target);
                due_bills.add(b);
            }
        }
        result.close();
        return due_bills;
    }

    public void pay_bills(SQLiteDatabase db, ArrayList<Bill> bill_list){
        String where = bill_id+" = ?";

        for (Bill bill : bill_list) {
            reduce_funds(db,
                    new String[]{
                            String.valueOf(bill.getAccount())
                    },
                    bill.getAmount(),
                    String.valueOf(bill.getTarget()));

            db.delete(bills, where, new String[]{String.valueOf(bill.getId())});
        }
    }




}
