package com.family.life24h.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.lbt.waitingdialog.WaitingDialog;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Models.objApplication.objAreaCode;
import com.family.life24h.Models.objApplication.objInviteCode;
import com.family.life24h.Models.objectFirebase.account.fb_Gps;
import com.family.life24h.Models.objectFirebase.account.fb_Location;
import com.family.life24h.Presenters.Account.pre_Account;
import com.family.life24h.Presenters.Account.view_Account;
import com.family.life24h.Presenters.Family.pre_Family;
import com.family.life24h.R;
import com.family.life24h.SQLite.tb_CurrentFamilyID;
import com.family.life24h.Utils.gpsUtils;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Utils.viewUtils;
import com.family.life24h.Views.UIWellcome.UITutorial;

import static com.family.life24h.Utils.directionalController.goToUISelectCountry;
import static com.family.life24h.Utils.keyUtils.REQUEST_CODE_PICK_IMAGE;
import static com.family.life24h.Utils.keyUtils.REQUEST_PERMISSION;

/*
 *  Date created: 01/06/2020
 *  Last updated: 01/06/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UISetupProfile extends AppCompatActivity implements view_Account {

    private final Context context = this;
    public static final String TAG = UISetupProfile.class.getSimpleName();

    public static objAreaCode areaCode;

    private TextView tvAreaCode;
    private TextView tvChooseCountry;

    private EditText edtUsername;
    private EditText edtPhoneNumber;
    private RadioButton rdoMale;
    private RadioButton rdoFemale;
    private RadioButton rdoOther;

    private EditText edtInviteCode;

    private TextView tvErrorPhoneNumber;
    private TextView tvErrorUsername;
    private TextView tvErrorInviteCode;

    private ImageView imvAvatar;
    private TextView tvChooseAvatar;

    private Button btnContinue;

    private Uri uriAvatar;

    private objAccount mAccount;

    private pre_Account preAccount;

    private objInviteCode inviteCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        viewUtils.setColorStatusBar((AppCompatActivity) context,R.color.colorStartGradientBlue);

        initPermission();

        initView();

        setupData();
    }

    private void setupData() {

        tvChooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUISelectCountry(context,TAG);
            }
        });

        //Choose image from storage
        imvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an Intent with action as ACTION_PICK
                Intent intent=new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                // Launching the Intent
                startActivityForResult(intent,REQUEST_CODE_PICK_IMAGE);
            }
        });

        btnContinue.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_button_save_profile));

        //Submit data to the server including account information and avatar
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAccount!=null){

                    String gender;
                    if(rdoFemale.isChecked())
                        gender = "Female";
                    else if (rdoMale.isChecked())
                        gender = "Male";
                    else
                        gender = "Other";

                    mAccount.setGender(gender);
                    mAccount.setName(edtUsername.getText().toString());
                    mAccount.setPhone(tvAreaCode.getText().toString() + edtPhoneNumber.getText().toString());
                    mAccount.setGps(new fb_Gps(gpsUtils.getInstance(context).isGPS()));

                    final String myInviteCode = edtInviteCode.getText().toString().toUpperCase();

                    mAccount.setLocation(new fb_Location(0,0));
                    WaitingDialog.showDialog(context);
                    //Check invite code
                    if(!myInviteCode.matches("")
                            && myInviteCode.length() == 6 ){

                        pre_Family.checkInviteCode(context,myInviteCode, new pre_Family.onResultInviteCode() {
                            @Override
                            public void onResult(objInviteCode inviteCode, String message) {
                                WaitingDialog.dismissDialog();

                                if(inviteCode != null){
                                    //Hide error
                                    tvErrorInviteCode.setVisibility(View.GONE);

                                    //Write a value to node IDFamily list
                                    pre_Family.addUidToFamilyID(context, inviteCode.getIdFamily(), new pre_Family.onResultAddUidToFamilyID() {
                                        @Override
                                        public void onResult(boolean isSuccess, String message) {
                                            WaitingDialog.dismissDialog();
                                            if(isSuccess){
                                                //Save current family id
                                                tb_CurrentFamilyID.getInstance(context).addOrUpdateID(inviteCode.getIdFamily());

                                                preAccount.profileSetting(uriAvatar,mAccount.getFireBaseAccount());
                                            }
                                            else
                                                Toast.makeText(UISetupProfile.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                //No invitation code found
                                else{
                                    //Show error
                                    tvErrorInviteCode.setVisibility(View.VISIBLE);
                                    tvErrorInviteCode.setText(getResources().getString(R.string.Could_not_find_the_invite_code));
                                }
                            }

                            @Override
                            public void onError(String error) {
                                WaitingDialog.dismissDialog();
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    //There is no invitation code to create a new family
                    else{
                        if(myInviteCode.length() != 6 && !myInviteCode.matches("")){
                            WaitingDialog.dismissDialog();

                            //Show error
                            tvErrorInviteCode.setVisibility(View.VISIBLE);
                            tvErrorInviteCode.setText(getResources().getString(R.string.Invalid_invitation_code));
                        }
                        //Do not enter the invitation code
                        else if(myInviteCode.matches("")){
                            pre_Family.createFamily(context,"", new pre_Family.onResultCreateFamily() {
                                @Override
                                public void onResult(boolean isSuccess, String message) {
                                    WaitingDialog.dismissDialog();
                                    if(isSuccess)
                                        preAccount.profileSetting(uriAvatar,mAccount.getFireBaseAccount());
                                    else
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                //Account null
                else{
                    Toast.makeText(context, R.string.Error_Please_restart_the_application, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == RESULT_OK)
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                //data.getData returns the content URI for the selected Image
                assert data != null;
                uriAvatar = data.getData();
                imvAvatar.setImageURI(uriAvatar);
                tvChooseAvatar.setVisibility(View.GONE);
            }
    }


    private void initView() {
        preAccount = new pre_Account(context,this);

        tvAreaCode = findViewById(R.id.tvAreaCode);
        tvChooseCountry = findViewById(R.id.tvChooseCountry);

        edtUsername = findViewById(R.id.edtUsername);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtInviteCode = findViewById(R.id.edtInviteCode);
        tvErrorPhoneNumber = findViewById(R.id.tvErrorPhoneNumber);
        tvErrorUsername = findViewById(R.id.tvErrorUsername);
        tvErrorInviteCode = findViewById(R.id.tvErrorInviteCode);

        rdoMale = findViewById(R.id.rdoMale);
        rdoFemale = findViewById(R.id.rdoFemale);
        rdoOther = findViewById(R.id.rdoOther);

        btnContinue = findViewById(R.id.btnContinue);

        imvAvatar = findViewById(R.id.imvAvatar);
        tvChooseAvatar = findViewById(R.id.tvChooseAvatar);

        mAccount = objAccount.getAccountFromSQLite(context, FirebaseAuth.getInstance().getUid());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length != 3 ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Init permission read storage
     */
    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void errorEmailOrPassword(String ErrorEmail, String ErrorPassword) {

    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {
        if(type.matches(keyUtils.ResultSettingProfile)){

            WaitingDialog.dismissDialog();

            if(isSuccess){

                Intent intent = new Intent(context, UITutorial.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void errorInputRegister(String email, String password, String confirmPassword) {

    }

    @Override
    public void errorInputSettingProfile(String errorUsername, String errorPhoneNumber) {
        tvErrorPhoneNumber.setVisibility(errorPhoneNumber == null ? View.GONE : View.VISIBLE);
        tvErrorUsername.setVisibility(errorUsername == null ? View.GONE : View.VISIBLE);
        tvErrorUsername.setText(errorUsername);
        tvErrorPhoneNumber.setText(errorPhoneNumber);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(areaCode!=null){
            tvAreaCode.setText(areaCode.getId());
            tvChooseCountry.setText(areaCode.getCountriesName());
        }

    }
}
