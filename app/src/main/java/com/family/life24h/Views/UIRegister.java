package com.family.life24h.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.lbt.waitingdialog.WaitingDialog;
import com.family.life24h.Presenters.Account.pre_Account;
import com.family.life24h.Presenters.Account.view_Account;
import com.family.life24h.R;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Utils.viewUtils;

/*
 *  Date created: 12/04/2019
 *  Last updated: 11/04/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */
public class UIRegister extends AppCompatActivity implements view_Account {
    private final Context context = this;

    private pre_Account mRegister;

    private TextView tvLogin;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;

    private Button btnRegister;

    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewUtils.setTransparentStatusBar((AppCompatActivity) context);
        initView();

        setAction();
    }

    private void setAction() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setVisibility(View.GONE);
                btnRegister.setEnabled(false);
                mRegister.actionSignUp(tilEmail.getEditText().getText().toString(),
                        tilPassword.getEditText().getText().toString(),
                        tilConfirmPassword.getEditText().getText().toString());
            }
        });
    }

    private void initView() {
        mRegister = new pre_Account(context,this);
        tvLogin = findViewById(R.id.tvLogin);

        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        btnRegister = findViewById(R.id.btnRegister);

        tvError = findViewById(R.id.tvError);
    }


    @Override
    public void errorEmailOrPassword(String ErrorEmail, String ErrorPassword) {

    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {
        if(type.matches(keyUtils.ResultRegister)){
            WaitingDialog.dismissDialog();

            btnRegister.setEnabled(true);

            if(isSuccess)
            {
                Intent intent = new Intent(context, UISetupProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else{
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(message);
            }
        }
    }


    @Override
    public void errorInputRegister(String errorEmail, String errorPassword, String errorConfirmPassword) {
        WaitingDialog.dismissDialog();

        btnRegister.setEnabled(true);

        tilEmail.setErrorEnabled(errorEmail != null);
        tilConfirmPassword.setErrorEnabled(errorConfirmPassword != null);
        tilPassword.setErrorEnabled(errorPassword != null);

        tilConfirmPassword.setError(errorConfirmPassword);
        tilEmail.setError(errorEmail);
        tilPassword.setError(errorPassword);
    }

    @Override
    public void errorInputSettingProfile(String errorUsername, String errorPhoneNumber) {

    }

    @Override
    public void errorInputEditProfile(String errorUsername, String errorPhoneNumber, String errorFamilyName) {

    }

    @Override
    public void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm) {

    }

    @Override
    public void resultCheckTimeTrial(boolean isExpired) {

    }

}