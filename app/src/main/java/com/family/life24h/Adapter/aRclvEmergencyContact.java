package com.family.life24h.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.R;
import com.family.life24h.Utils.keyUtils;

import java.util.ArrayList;

/*
 *  Date created: 12/25/2019
 *  Last updated: 12/25/2019
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class aRclvEmergencyContact extends RecyclerView.Adapter<aRclvEmergencyContact.ViewHolder> {
    private final Context context;

    private ArrayList<objAccount> items;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public aRclvEmergencyContact(ArrayList<objAccount> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public String getPhoneNumber(int position){
        return items.get(position).getPhone();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        objAccount item = items.get(position);

        if(item.getLocalAvatar().matches("")){
            Glide.with(context).load(item.getAvatar())
                    .placeholder(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar);
        }else{
            Glide.with(context).load(item.getLocalAvatar())
                    .placeholder(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar);
        }

        holder.tvPhoneNumber.setText(item.getPhone().matches(keyUtils.NULL) ? context.getResources().getString(R.string.Unknown) : item.getPhone());
        holder.tvUsername.setText(item.getName());

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imvAvatar;
        private TextView tvUsername;
        private TextView tvPhoneNumber;
        private CardView cvMain;

        private ViewHolder(View itemView) {
            super(itemView);

            imvAvatar = itemView.findViewById(R.id.imvAvatar);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            cvMain = itemView.findViewById(R.id.cvMain);
        }

    }
}