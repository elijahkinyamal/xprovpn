package com.webihostapp.xprofreevpnapp.dialog;


import unified.vpn.sdk.Country;

public class CountryData {

    private boolean pro = false;
    private Country countryvalue;

    public boolean isPro() {
        return pro;
    }

    public void setPro(boolean pro) {
        this.pro = pro;
    }

    public Country getCountryvalue() {
        return countryvalue;
    }

    public void setCountryvalue(Country countryvalue) {
        this.countryvalue = countryvalue;
    }

}
