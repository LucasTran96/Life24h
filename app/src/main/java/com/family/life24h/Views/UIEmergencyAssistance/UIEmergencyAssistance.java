package com.family.life24h.Views.UIEmergencyAssistance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.family.life24h.Presenters.Safety.pre_Safety;
import com.family.life24h.R;
import com.family.life24h.Utils.viewUtils;

/*
 *  Date created: 02/04/2020
 *  Last updated: 02/04/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIEmergencyAssistance extends AppCompatActivity {
    private final Context context = this;

    private CountDownTimer countDownTimer;

    private TextView tvTime;

    private Button btnCancel;

    private MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_assistance);

        viewUtils.setTransparentStatusBar((AppCompatActivity) context);

        tvTime = findViewById(R.id.tvTime);
        btnCancel = findViewById(R.id.btnCancel);


        mPlayer = MediaPlayer.create(context, R.raw.waring);
        mPlayer.start();
        mPlayer.setLooping(true);

        countDownTimer = new CountDownTimer(11000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_number));
                tvTime.setText(millisUntilFinished/1000 + "");
            }

            @Override
            public void onFinish() {
                pre_Safety.sendEmergencyAssistanceHelp(context,new pre_Safety.iEmergencyAssistanceHelp() {
                    @Override
                    public void onResult(boolean isSuccess, String message) {
                        if(isSuccess){
                            Toast.makeText(context, R.string.Successfully_informed_everyone_in_your_family, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, R.string.Error_please_try_again_later, Toast.LENGTH_SHORT).show();
                        }

                        finish();
                    }
                });

            }
        };

        countDownTimer.start();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        mPlayer.stop();
    }
}
