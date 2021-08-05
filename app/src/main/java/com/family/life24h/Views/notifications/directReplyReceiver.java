package com.family.life24h.Views.notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.RemoteInput;

import com.family.life24h.Presenters.Chat.pre_Chat;
import com.family.life24h.R;
import com.family.life24h.Utils.keyUtils;

import static com.family.life24h.Utils.keyUtils.KEY_TEXT_REPLY;
import static com.family.life24h.Utils.keyUtils.TAG_NOTIFICATION;


/*
 *  Date created: 12/27/2019
 *  Last updated: 12/27/2019
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class directReplyReceiver extends BroadcastReceiver {


    private String dsdssd = "dsđssđs";
    @Override
    public void onReceive(Context context, Intent intent) {
        processInlineReply(context,intent);
    }

    private void processInlineReply(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        Bundle bundle = intent.getBundleExtra(keyUtils.data);
        Log.d("ssds",dsdssd );
        if (remoteInput != null) {

            String idChat = bundle.getString(keyUtils.dataIDChat);

            String message = remoteInput.getCharSequence(KEY_TEXT_REPLY).toString();
            
            if(idChat!=null){

                pre_Chat.postMessageFromNotification(idChat, message, new pre_Chat.onResultPostMessageNotification() {
                    @Override
                    public void onResult(boolean isSuccess, String message) {
                        if(isSuccess){
                            NotificationManager mNotificationManager =
                                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                            int id = intent.getIntExtra(keyUtils.notificationId, -1);
                            // mNotificationManager.cancelAll();
                            mNotificationManager.cancel(TAG_NOTIFICATION,id);

                            Toast.makeText(context, R.string.Successful_message_reply, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, R.string.Reply_message_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            
        }
    }
}
