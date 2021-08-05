package com.family.life24h.Models.objApplication;

/*
 *  Date created: 12/31/2019
 *  Last updated: 12/31/2019
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class objInviteCode {
    private String inviteCode;
    private String idFamily;

    public objInviteCode(String inviteCode, String idFamily) {
        this.inviteCode = inviteCode;
        this.idFamily = idFamily;
    }

    public objInviteCode() {
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getIdFamily() {
        return idFamily;
    }

    public void setIdFamily(String idFamily) {
        this.idFamily = idFamily;
    }
}
