package com.example.examproject_mobile_bank.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.example.examproject_mobile_bank.R;

public class NemIDDialog extends DialogFragment implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

    private String input;
    private String pass;
    private boolean auth;
    EditText nem2;
    CallBackListener listener;


    public NemIDDialog() {

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        setInput(args.getString("input"));
        setPass(args.getString("pass"));
    }

    public interface CallBackListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getString(R.string.alert_title);
        LayoutInflater lf = requireActivity().getLayoutInflater();

        AlertDialog.Builder ad_builder = new AlertDialog.Builder(getActivity());
        ad_builder.setView(lf.inflate(R.layout.dialog_nemid, null));
        ad_builder.setTitle(title);
        ad_builder.setMessage(getString(R.string.alert_message)+": "+getInput());
        ad_builder.setIcon(R.drawable.ic_auth);
        ad_builder.setPositiveButton(R.string.alert_OK, this);
        ad_builder.setNegativeButton(R.string.alert_NO, this);

        return ad_builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            nem2 = (EditText) getDialog().findViewById(R.id.nem2);

            if(getPass().contentEquals(nem2.getText().toString())){
                setAuth(true);
            }
            listener.onDialogPositiveClick(NemIDDialog.this);
        }

        else {
            setAuth(false);
            listener.onDialogNegativeClick(NemIDDialog.this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the CallBackListener so we can send events to the host
            listener = (CallBackListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(" must implement CallBackListener");
        }

    }

    @Override
    public Dialog getDialog() {
        return super.getDialog();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
