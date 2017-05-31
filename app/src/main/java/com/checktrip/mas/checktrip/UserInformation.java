package com.checktrip.mas.checktrip;

import java.util.ArrayList;

/**
 * Created by ling on 10/17/16.
 */

public class UserInformation {
    public String email, uid;
    public String name;
    public String address;
    public UserFlightInfo flightInfo;
    public UserFlightInfo pickupInfo;

    public UserInformation(String email, String uid, String name, String address, UserFlightInfo flightInfo, UserFlightInfo pickupInfo) {
        this.email = email;
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.flightInfo = flightInfo;
        this.pickupInfo = pickupInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserFlightInfo getFlightInfo() {
        return flightInfo;
    }

    public void setFlightInfo(UserFlightInfo flightInfo) {
        this.flightInfo = flightInfo;
    }

    public UserFlightInfo getPickupInfo() {
        return pickupInfo;
    }

    public void setPickupInfo(UserFlightInfo pickupInfo) {
        this.pickupInfo = pickupInfo;
    }
}
