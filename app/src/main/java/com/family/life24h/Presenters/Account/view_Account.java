
package com.family.life24h.Presenters.Account;

/*
 *  Date created: 10/31/2019
 *  Last updated: 10/31/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

public interface view_Account {

    /**
     * Error of email or password
     * @param errorEmail error of email
     * @param errorPassword error of password
     */
    void errorEmailOrPassword(String errorEmail, String errorPassword);

    /**
     * Result of action
     * @param isSuccess success or fail
     * @param message message
     * @param type type of action (keyUtils)
     */
    void resultOfActionAccount(boolean isSuccess, String message, String type);

    /**
     * Error input of register
     * @param email email
     * @param password password
     * @param confirmPassword confirm password
     */
    void errorInputRegister(String email, String password, String confirmPassword);

    /**
     * Error input of setting profile
     * @param errorUsername error of username
     * @param errorPhoneNumber error of phone number
     */
    void errorInputSettingProfile(String errorUsername, String errorPhoneNumber);


    void errorInputEditProfile(String errorUsername, String errorPhoneNumber, String errorFamilyName);

    void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm);

    void resultCheckTimeTrial(boolean isExpired);
}
