package com.family.life24h.Views.UIChat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.family.life24h.Adapter.aRclvAllImageMsg;
import com.family.life24h.Models.objApplication.objChat;
import com.family.life24h.Models.objApplication.objDetailImage;
import com.family.life24h.Models.objApplication.objMessage;
import com.family.life24h.Models.objectFirebase.chat.fb_Chat;
import com.family.life24h.Models.objectFirebase.chat.fb_Message;
import com.family.life24h.Presenters.Chat.pre_Chat;
import com.family.life24h.Presenters.Chat.view_Chat;
import com.family.life24h.R;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Utils.timeUtils;
import com.family.life24h.Utils.viewUtils;
import com.family.life24h.SQLite.tb_Account;

import java.util.ArrayList;

import static com.family.life24h.Utils.patternFormatDateTime.MMM_dd_yyyy_hh_mm_a;

/*
 *  Date created: 12/18/2019
 *  Last updated: 12/17/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIViewImageMessage extends AppCompatActivity implements view_Chat {
    private final Context context = this;
    private pre_Chat preChat;

    private RecyclerView rclvAllImageMessage;


    private TextView tvUsername;
    private TextView tvTimeCreate;
    private PhotoView photoView;

    private String urlImage;
    private fb_Message mMessage;
    private String idChat;


    private ArrayList<objDetailImage> detailImageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_message);
        getDataBundle();

        initView();

        setDataToView(urlImage, tb_Account.getInstance(context).getAccountByID(mMessage.getAuth()).getName(),mMessage.getTime());

    }

    private void setDataToView(String url, String username, long timeCreate) {
        Glide.with(context).load(url)
                .placeholder(R.color.black)
                .error(R.drawable.no_image)
                .into(photoView);

        tvUsername.setText(username);
        tvTimeCreate.setText(timeUtils.convertMillisecondToStringFormat(timeCreate,MMM_dd_yyyy_hh_mm_a));
    }

    private void getDataBundle() {
        Bundle bundle = getIntent().getBundleExtra(keyUtils.data);
        if(bundle != null){
            idChat = bundle.getString(keyUtils.dataIDChat,keyUtils.NULL);
            urlImage = bundle.getString(keyUtils.dataURLImage,keyUtils.NULL);
            mMessage = (fb_Message) bundle.getSerializable(keyUtils.dataMessage);

            if(idChat.matches(keyUtils.NULL) || urlImage.matches(keyUtils.NULL) || mMessage == null)
                finish();

        }else
            finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        findViewById(R.id.imvBack)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        viewUtils.setColorStatusBar((AppCompatActivity) context,R.color.black);

        photoView = findViewById(R.id.imageView);
        rclvAllImageMessage = findViewById(R.id.rclvAllImageMessage);
        tvUsername = findViewById(R.id.tvUsername);
        tvTimeCreate = findViewById(R.id.tvTimeCreate);
        preChat = new pre_Chat(context,this);
        preChat.getAllImageFromMessage(idChat);

        detailImageList = new ArrayList<>();

    }


    @Override
    public void resultListChat(ArrayList<objChat> chatList) {

    }

    @Override
    public void resultOfAction(boolean isSuccess, String message, String type) {

    }

    @Override
    public void resultChatDetail(fb_Chat chats) {

    }

    @Override
    public void resultMessage(objMessage newMessage) {

    }

    @Override
    public void resultAllImage(final ArrayList<objDetailImage> detailImageList) {
        this.detailImageList = detailImageList;
        aRclvAllImageMsg adapter = new aRclvAllImageMsg(detailImageList,context);
        rclvAllImageMessage.setAdapter(adapter);
        rclvAllImageMessage.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        rclvAllImageMessage.setHasFixedSize(true);
        adapter.setOnClickListener(new aRclvAllImageMsg.actionClick() {
            @Override
            public void onClick(int position) {
                objDetailImage detailImage = detailImageList.get(position);
                setDataToView(detailImage.getUrlImage(),detailImage.getUsername(),detailImage.getTimeCreate());
            }
        });
    }


}
