package com.family.life24h.Presenters.Family;

/*
 *  Date created: 12/17/2019
 *  Last updated: 12/17/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

import com.family.life24h.Models.objApplication.objFamily;

import java.util.ArrayList;

public interface view_Family {
    void resultOfActionFamily(boolean isSuccess, String message, String type);

    void resultFamilyList(ArrayList<objFamily> families);
}
