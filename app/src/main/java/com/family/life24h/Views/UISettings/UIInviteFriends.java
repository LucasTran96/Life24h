package com.family.life24h.Views.UISettings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.family.life24h.Models.objApplication.objFamily;
import com.family.life24h.R;
import com.family.life24h.Utils.adsUtils;
import com.family.life24h.Utils.viewUtils;

import static com.family.life24h.Utils.keyUtils.LINK_GOOGLE_PLAY;

/*
 *  Date created: 01/06/2020
 *  Last updated: 12/31/2019
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIInviteFriends extends AppCompatActivity {
    private final Context context = this;

    private TextView tvInviteCode;
    private Button btnSendCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        viewUtils.setupToolbar(context,R.id.toolbar,R.color.black,null,R.color.colorThemeStatusBar);

        initView();

        tvInviteCode.setText(objFamily.getMyFamily(context).getInviteCode());

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUtils.shareWith(context,String.format(getResources().getString(R.string.This_is_my_family_invitation_code) + LINK_GOOGLE_PLAY, tvInviteCode.getText().toString()));
            }
        });
    }

    private void initView() {
        tvInviteCode = findViewById(R.id.tvInviteCode);
        btnSendCode = findViewById(R.id.btnSendCode);

        adsUtils.setupAds(context);
    }
}
