package com.family.life24h.Utils;

/*
 *  Date created: 03/03/2020
 *  Last updated: 03/03/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.family.life24h.Models.objApplication.objFamily;
import com.family.life24h.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class suggestionsDialog {

    public static void showSuggestions(Context context){

        objFamily family = objFamily.getMyFamily(context);
        if(family.getMembersList().size()<=1){
            String contentSuggestion = String.format(context.getResources().getString(R.string.suggestion_invite), "<b>" + family.getInviteCode() + "</b>");

            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_suggestion);
            dialog.setCancelable(false);

            TextView tvSuggestion = dialog.findViewById(R.id.tvSuggestion);

            CardView cvMainDialog = dialog.findViewById(R.id.cvMainDialog);

            tvSuggestion.setText(Html.fromHtml(contentSuggestion));

            cvMainDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    directionalController.goToUIInviteFriends(context);
                }
            });


            dialog.findViewById(R.id.imvClose)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

            dialog.getWindow().setLayout((int)(context.getResources().getDisplayMetrics().widthPixels*0.9), WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
        }
    }

}
