
package com.app.sealteamdelivery.model.currentorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrentOrder implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("deliveryCost")
    @Expose
    private Integer deliveryCost;
    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("OrderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("orderDest")
    @Expose
    private String orderDest;
    @SerializedName("orderCost")
    @Expose
    private Integer orderCost;
    @SerializedName("ResturantName")
    @Expose
    private String resturantName;
    @SerializedName("resturantslat")
    @Expose
    private String resturantslat;
    @SerializedName("resturantslng")
    @Expose
    private String resturantslng;
    @SerializedName("resturantslocation")
    @Expose
    private String resturantslocation;
    @SerializedName("resturantsTelephone")
    @Expose
    private String resturantsTelephone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Integer deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDest() {
        return orderDest;
    }

    public void setOrderDest(String orderDest) {
        this.orderDest = orderDest;
    }

    public Integer getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(Integer orderCost) {
        this.orderCost = orderCost;
    }

    public String getResturantName() {
        return resturantName;
    }

    public void setResturantName(String resturantName) {
        this.resturantName = resturantName;
    }

    public String getResturantslat() {
        return resturantslat;
    }

    public void setResturantslat(String resturantslat) {
        this.resturantslat = resturantslat;
    }

    public String getResturantslng() {
        return resturantslng;
    }

    public void setResturantslng(String resturantslng) {
        this.resturantslng = resturantslng;
    }

    public String getResturantslocation() {
        return resturantslocation;
    }

    public void setResturantslocation(String resturantslocation) {
        this.resturantslocation = resturantslocation;
    }

    public String getResturantsTelephone() {
        return resturantsTelephone;
    }

    public void setResturantsTelephone(String resturantsTelephone) {
        this.resturantsTelephone = resturantsTelephone;
    }

}
