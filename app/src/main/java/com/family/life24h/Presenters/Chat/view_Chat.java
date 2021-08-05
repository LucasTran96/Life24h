package com.family.life24h.Presenters.Chat;

/*
 *  Date created: 12/09/2019
 *  Last updated: 12/09/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

import com.family.life24h.Models.objApplication.objChat;
import com.family.life24h.Models.objApplication.objDetailImage;
import com.family.life24h.Models.objApplication.objMessage;
import com.family.life24h.Models.objectFirebase.chat.fb_Chat;

import java.util.ArrayList;

public interface view_Chat {

    void resultListChat(ArrayList<objChat> chatList);

    void resultOfAction(boolean isSuccess, String message, String type);

    void resultChatDetail(fb_Chat chats);

    void resultMessage(objMessage newMessage);

    void resultAllImage(ArrayList<objDetailImage> detailImageList);

}
