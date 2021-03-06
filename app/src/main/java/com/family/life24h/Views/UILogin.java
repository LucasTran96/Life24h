

package com.family.life24h.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.InputDialog;
import com.lbt.waitingdialog.WaitingDialog;
import com.family.life24h.Presenters.Family.pre_Family;
import com.family.life24h.R;
import com.family.life24h.Presenters.Account.pre_Account;
import com.family.life24h.Presenters.Account.view_Account;
import com.family.life24h.Utils.directionalController;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Utils.viewUtils;
import com.family.life24h.Views.UIWellcome.UITutorial;

import java.util.Arrays;
import java.util.List;
import static com.family.life24h.Utils.keyUtils.REQUEST_LOGIN;
import static com.family.life24h.Utils.keyUtils.REQUEST_PERMISSION;

/*
 *  Date created: 12/04/2019
 *  Last updated: 11/04/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */
public class UILogin extends AppCompatActivity implements view_Account{
//    private final String TAG = "UILogin";
    private final Context context = this;

    private TextView tvRegister;

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;

    private TextView tvError;

    private Button btnLogin;
    private static final int LOCATION_REQUEST = 1000;

    private pre_Account mLogin;

    private Button btnSignInWithGoogle;

    private TextView tvForgotPassword;

    private static final String TAG = "InAppBilling";
    // In-app products. Currently only selling "ad removal"
    static final String ITEM_SKU_ADREMOVAL = "preminium_1_month";

    private BillingClient mBillingClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG,TAG);
        viewUtils.setTransparentStatusBar((AppCompatActivity) context);
        initPermission();
        initView();
        setAction();
        checkAutoStart();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

                Log.d("packageName",UILogin.this.getPackageName());
                Intent myIntent = new Intent();
                myIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                myIntent.setData(Uri.parse("package:" + UILogin.this.getPackageName()));
                startActivity(myIntent);
            }
        }

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

                Log.d("packageName",UILogin.this.getPackageName());
                Intent myIntent = new Intent();
                myIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                myIntent.setData(Uri.parse("package:" + UILogin.this.getPackageName()));
                startActivity(myIntent);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 7 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[3] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[4] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[5] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[6] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

        else
        {
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.length != 5 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[3] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[4] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    /**
     * checkAutoStart this is the method that requires the user to turn on the auto start function
     * so that the application can run in the background stably.
     */
    public void checkAutoStart()
    {
        String manufacturer = android.os.Build.MANUFACTURER;
        Log.d("manufacturer",manufacturer);
        try {
            Intent intent = new Intent();
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autoStart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAction() {

        //go to activity register
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, UIRegister.class));
            }
        });

        //Action login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setVisibility(View.GONE);
                btnLogin.setEnabled(false);

                WaitingDialog.showDialog(context);

                mLogin.actionSignIn(tilEmail.getEditText().getText().toString(), tilPassword.getEditText().getText().toString());
            }
        });

        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSignInIntent();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.show((AppCompatActivity) context,
                        getResources().getString(R.string.Reset_Password),
                        null, getResources().getString(R.string.ok),
                        getResources().getString(R.string.Cancel))
                        .setHintText(getResources().getString(R.string.Email))
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                if (!inputStr.matches("")) {
                                    WaitingDialog.showDialog(context);
                                    mLogin.forgetPassword(inputStr);
                                    return false;
                                }
                                //Invalid email
                                else {
                                    Toast.makeText(context, R.string.Email_is_not_empty, Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        });
            }
        });


    }

    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                REQUEST_LOGIN);
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                mLogin.signInWithGoogle(FirebaseAuth.getInstance().getCurrentUser());
            } else {
                Log.d("CheckApp", "RESULT CODE NOT OK");
                Toast.makeText(context, getResources().getString(R.string.Login_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        mLogin = new pre_Account(context,this);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvError = findViewById(R.id.tvError);

        btnSignInWithGoogle = findViewById(R.id.btnSignInWithGoogle);
    }


    @Override
    public void errorEmailOrPassword(String errorEmail, String errorPassword) {
        //after results
        btnLogin.setEnabled(true);


        WaitingDialog.dismissDialog();

        tilEmail.setErrorEnabled(errorEmail != null);
        tilPassword.setErrorEnabled(errorPassword != null);

        tilEmail.setError(errorEmail);
        tilPassword.setError(errorPassword);
    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {

        if(type.matches(keyUtils.ResultLogin) || type.matches(keyUtils.ResultRegister)){


            WaitingDialog.dismissDialog();

            //after result
            btnLogin.setEnabled(true);

            if(isSuccess) {
                pre_Family.checkFamilyID(new pre_Family.onResultCheckFamilyID() {
                    @Override
                    public void onResult(boolean isSuccess, long size, String message) {
                        if(isSuccess && size > 0){
                            if(!UITutorial.isFlagTutorial(context))
                                startActivity(new Intent(context, UITutorial.class));
                            else{
                                directionalController.goToUILoadingData(context);
                            }

                            finish();
                        }
                        else{
                            Intent intent = new Intent(context, UISetupProfile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }else{
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(message);
            }
        }

        if(type.matches(keyUtils.forgetPassword)){
            WaitingDialog.dismissDialog();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void errorInputRegister(String email, String password, String confirmPassword) {

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

