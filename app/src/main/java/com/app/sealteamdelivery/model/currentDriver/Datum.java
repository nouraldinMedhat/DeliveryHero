
package com.app.sealteamdelivery.model.currentDriver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("rate")
    @Expose
    private double rate;
    @SerializedName("Status")
    @Expose
    private Long status;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("image")
    @Expose
    private String image;
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
    @SerializedName("Driver_Id")
    @Expose
    private Long driverId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Datum withName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Datum withEmail(String email) {
        this.email = email;
        return this;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public Datum withRate(Long rate) {
        this.rate = rate;
        return this;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Datum withStatus(Long status) {
        this.status = status;
        return this;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Datum withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Datum withImage(String image) {
        this.image = image;
        return this;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Datum withCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
        return this;
    }

    public String getCanReceiveOrder() {
        return canReceiveOrder;
    }

    public void setCanReceiveOrder(String canReceiveOrder) {
        this.canReceiveOrder = canReceiveOrder;
    }

    public Datum withCanReceiveOrder(String canReceiveOrder) {
        this.canReceiveOrder = canReceiveOrder;
        return this;
    }

    public Long getBusy() {
        return busy;
    }

    public void setBusy(Long busy) {
        this.busy = busy;
    }

    public Datum withBusy(Long busy) {
        this.busy = busy;
        return this;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Datum withAvailability(String availability) {
        this.availability = availability;
        return this;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Datum withDriverId(Long driverId) {
        this.driverId = driverId;
        return this;
    }

}
