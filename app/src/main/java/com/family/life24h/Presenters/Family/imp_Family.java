package com.family.life24h.Presenters.Family;

/*
 *  Date created: 12/17/2019
 *  Last updated: 12/17/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

public interface imp_Family {

    /**
     *  Get all family ids by user id
     */
    void getAllIDFamilyByUserID();

    /**
     *
     * @param pathIDFamily path id of family
     */
    void getFamilyInformation(final String pathIDFamily);

    /**
     *
     * @param pathIDFamily path id of family
     */
    void getAllAccountInformationInFamily(final String pathIDFamily);

    void getAllFamilyByUid();
}
