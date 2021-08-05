package com.family.life24h.Views.UIWellcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.family.life24h.R;

/*
 *  Date created: 01/13/2020
 *  Last updated: 01/13/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIWelcome_first extends AppCompatActivity {

    private Button btn_Next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiwelcome_first);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        btn_Next = findViewById(R.id.btn_Next_WelcomeFirst);
//        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.animate_diagonal_right_exit);
//        btn_Next.startAnimation(animation);
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentWelcome = new Intent(UIWelcome_first.this,UIWelcome_second.class);
                startActivity(intentWelcome);
                //finish();
            }
        });
    }
}
