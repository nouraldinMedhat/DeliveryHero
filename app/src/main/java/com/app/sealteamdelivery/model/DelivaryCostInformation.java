package com.app.sealteamdelivery.model;

public class DelivaryCostInformation {
    int driver_id;
    int responseStatus;
    int order_id;
    int delivrycost;

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setDelivrycost(int delivrycost) {
        this.delivrycost = delivrycost;
    }
}
