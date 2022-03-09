
package com.app.sealteamdelivery.model.driver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentOrder {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("rate")
    @Expose
    private Double rate;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("CurrentBalance")
    @Expose
    private String currentBalance;
    @SerializedName("canReceiveOrder")
    @Expose
    private String canReceiveOrder;
    @SerializedName("busy")
    @Expose
    private Long busy;
    @SerializedName("availability")
    @Expose
    private String availability;
    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("Driver_Id")
    @Expose
    private Long driverId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CurrentOrder withName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CurrentOrder withEmail(String email) {
        this.email = email;
        return this;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public CurrentOrder withRate(Double rate) {
        this.rate = rate;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CurrentOrder withImage(String image) {
        this.image = image;
        return this;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public CurrentOrder withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public CurrentOrder withCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
        return this;
    }

    public String getCanReceiveOrder() {
        return canReceiveOrder;
    }

    public void setCanReceiveOrder(String canReceiveOrder) {
        this.canReceiveOrder = canReceiveOrder;
    }

    public CurrentOrder withCanReceiveOrder(String canReceiveOrder) {
        this.canReceiveOrder = canReceiveOrder;
        return this;
    }

    public Long getBusy() {
        return busy;
    }

    public void setBusy(Long busy) {
        this.busy = busy;
    }

    public CurrentOrder withBusy(Long busy) {
        this.busy = busy;
        return this;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public CurrentOrder withAvailability(String availability) {
        this.availability = availability;
        return this;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public CurrentOrder withDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
        return this;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public CurrentOrder withDriverId(Long driverId) {
        this.driverId = driverId;
        return this;
    }

}
