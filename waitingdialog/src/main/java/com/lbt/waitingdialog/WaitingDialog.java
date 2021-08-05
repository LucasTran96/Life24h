package com.lbt.waitingdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

public class WaitingDialog {
    private static Dialog dialog;

    public static void showDialog(Context context, int colorID){

        if(!((Activity) context).isFinishing()){
            if(dialog == null){
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.waiting_dialog);

                SpinKitView waitingView = dialog.findViewById(R.id.SpinKitLoading);
                waitingView.setColor(context.getResources().getColor(colorID));

                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            if(!dialog.isShowing())
                dialog.show();
        }

    }

    public static void showDialog(final Context context){
        if(!((Activity) context).isFinishing()){
            if(dialog == null){
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.waiting_dialog);

                SpinKitView waitingView = dialog.findViewById(R.id.SpinKitLoading);
                waitingView.setColor(context.getResources().getColor(R.color.colorPurple));
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            if(!dialog.isShowing())
                dialog.show();
        }
    }

    public static void showDialog(final Context context, final String title){
        if(!((Activity) context).isFinishing()){
            if(dialog == null){
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.waiting_dialog_title);

                SpinKitView waitingView = dialog.findViewById(R.id.SpinKitLoading);
                TextView tvTitle = dialog.findViewById(R.id.tvTitle);

                waitingView.setColor(context.getResources().getColor(R.color.colorPurple));
                tvTitle.setText(title);

                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            if(!dialog.isShowing())
                dialog.show();
        }
    }

    public static void dismissDialog(){
        if(dialog != null){
            if(dialog.isShowing()){
                dialog.dismiss();
                dialog = null;
            }
        }
    }


}
