package com.family.life24h.Presenters.Safety;

import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Models.objApplication.objDrivingDetail;
import com.family.life24h.Models.objApplication.objEmergencyAssistance;

import java.util.ArrayList;

/*
 *  Date created: 01/09/2020
 *  Last updated: 12/26/2019
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public interface view_Safety {

    void resultOfAction(boolean isSuccess, String message, String type);

    void emergencyContacts(ArrayList<objAccount> accounts);

    void drivingDetails(objDrivingDetail drivingDetail);

    void allDrivingDetailOfUser(ArrayList<objDrivingDetail> drivingDetailList);

    void emergencyAssistance(objAccount account, objEmergencyAssistance EmergencyAssistance, boolean isNewEmergency);

    void allEmergencyAssistance(ArrayList<objEmergencyAssistance> newEmergencyAssistanceList, ArrayList<objEmergencyAssistance> oldEmergencyAssistanceList);


}
