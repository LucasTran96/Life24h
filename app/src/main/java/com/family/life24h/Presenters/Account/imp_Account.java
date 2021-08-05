package com.family.life24h.Presenters.Account;

/*
 *  Date created: 12/04/2019
 *  Last updated: 11/04/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.family.life24h.Models.objectFirebase.account.fb_Account;

public interface imp_Account {

    /**
     * Logout;
     */
    void signOut();

    /**
     * Login
     * @param email email
     * @param pwd password
     */
    void actionSignIn(String email, String pwd);

    /**
     * Auto login
     */
    void autoSignIn();

    /**
     * Register
     * @param email email
     * @param password password
     * @param confirmPassword confirm password
     */
    void actionSignUp(String email, String password, String confirmPassword);

    /**
     *
     * @param uriAvatar uri avatar
     * @param mAccount object account FireBase
     */
    void profileSetting(Uri uriAvatar, fb_Account mAccount);


    /**
     * Sign in with google
     * @param mUser Current user FireBase
     */
    void signInWithGoogle(FirebaseUser mUser);

    /**
     * Reset password
     * @param email email
     */
    void forgetPassword(String email);

    /**
     * update password
     * @param currentPassword current password
     * @param newPassword new password
     * @param passwordConfirm password confirm
     */
    void updatePassword(String currentPassword, String newPassword, String passwordConfirm);

    /**
     * Check the trial period
     */
    void checkTimeTrial();

    /**
     * Update bought status
     */
    void updateBought();
}
