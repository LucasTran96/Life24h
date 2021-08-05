package com.family.life24h.Views.UIChat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.family.life24h.Adapter.aMultiPictureSelect;
import com.family.life24h.R;

import java.util.ArrayList;

/*
 *  Date created: 12/18/2019
 *  Last updated: 12/16/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIChooseImageFromGallery extends AppCompatActivity {
    public static final String TAG = UIChooseImageFromGallery.class.getSimpleName();

    private final Context context = this;

    private GridView gvImageFromGallery;
    private aMultiPictureSelect adapterMultiSelect;

    private TextView tvNumberSelected;
    private Button btnDone;

    private ArrayList<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image_from_gallery);

        initView();

        setupGridView();

        setActionToView();
    }

    private void setActionToView() {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIChat.previousActivity = TAG;
                UIChat.imageList = adapterMultiSelect.getImageCheckedList();
                finish();
            }
        });
    }


    private void setupGridView() {
        this.imageUrls = new ArrayList<>();
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };


        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        //Set up cursor
        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        //Get all image and add to image list
        for (int i = 0; i < imageCursor.getCount(); i++) {
            imageCursor.moveToPosition(i);
            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imageCursor.getString(dataColumnIndex));
            Log.d(TAG, imageCursor.getString(dataColumnIndex));
        }

        adapterMultiSelect = new aMultiPictureSelect(context,imageUrls);
        gvImageFromGallery.setAdapter(adapterMultiSelect);

        adapterMultiSelect.setOnClickListener(new aMultiPictureSelect.onAction() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedImage(int selectedTotal) {
                tvNumberSelected.setText(selectedTotal + " " +getResources().getString(R.string.selected));
            }
        });


    }

    private void initView() {
        gvImageFromGallery = findViewById(R.id.gridViewImage);
        tvNumberSelected = findViewById(R.id.tvNumberSelected);
        btnDone = findViewById(R.id.btnDone);
    }
}
