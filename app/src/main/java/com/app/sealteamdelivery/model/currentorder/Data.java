
package com.app.sealteamdelivery.model.currentorder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("CurrentOrders")
    @Expose
    private List<CurrentOrder> currentOrders = null;
    @SerializedName("PendingOrders")
    @Expose
    private List<PendingOrder> pendingOrders = null;

    public List<CurrentOrder> getCurrentOrders() {
        return currentOrders;
    }

    public void setCurrentOrders(List<CurrentOrder> currentOrders) {
        this.currentOrders = currentOrders;
    }

    public List<PendingOrder> getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(List<PendingOrder> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

}
