package com.family.life24h.Models.objApplication;

/*
 *  Date created: 02/03/2020
 *  Last updated: 02/03/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class objAreaCode {

    public String id;
    public String countriesName;

    public objAreaCode() {
    }

    public objAreaCode(String id, String countriesName) {
        this.id = id;
        this.countriesName = countriesName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountriesName() {
        return countriesName;
    }

    public void setCountriesName(String countriesName) {
        this.countriesName = countriesName;
    }
}