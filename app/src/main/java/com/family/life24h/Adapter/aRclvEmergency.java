package com.family.life24h.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Models.objApplication.objEmergencyAssistance;
import com.family.life24h.R;
import com.family.life24h.SQLite.tb_Account;
import com.family.life24h.Utils.timeUtils;

import java.util.ArrayList;
import java.util.Objects;

/*
 *  Date created: 02/05/2020
 *  Last updated: 02/05/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class aRclvEmergency extends RecyclerView.Adapter<aRclvEmergency.ViewHolder> {
    private final Context context;

    private ArrayList<objEmergencyAssistance> items;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public aRclvEmergency(ArrayList<objEmergencyAssistance> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public objEmergencyAssistance getItemByPosition(int position){
        return items.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_assistance, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        objEmergencyAssistance item = items.get(position);


        objAccount account = tb_Account.getInstance(context).getAccountByID(item.getAuth());
        if(account != null){

            Glide.with(Objects.requireNonNull(context))
                    .load(account.getLocalAvatar().matches("") ? account.getAvatar() : account.getLocalAvatar())
                    .placeholder(R.color.colorLine)
                    .error(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar);

            holder.tvNeedsHelp.setText(item.getMessage());
        }

        holder.tvTime.setText(timeUtils.getTimeAgo(item.getTimeCreate()));

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick(position);
            }
        });

        if(item.getListSeen().contains(objAccount.getCurrentUser().getUid())){
            holder.imvHelp.setImageDrawable(context.getResources().getDrawable(R.drawable.stamp_help_grey));
        }else{
            holder.imvHelp.setImageDrawable(context.getResources().getDrawable(R.drawable.stamp_help));
            holder.imvHelp.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_warning));
        }

    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNeedsHelp;
        private TextView tvTime;
        private ImageView imvAvatar;
        private CardView cvMain;
        private ImageView imvHelp;

        private ViewHolder(View itemView) {
            super(itemView);

            tvNeedsHelp = itemView.findViewById(R.id.tvNeedsHelp);
            tvTime = itemView.findViewById(R.id.tvTime);
            imvAvatar = itemView.findViewById(R.id.imvAvatar);
            cvMain = itemView.findViewById(R.id.cvMain);
            imvHelp = itemView.findViewById(R.id.imvHelp);
        }

    }
}