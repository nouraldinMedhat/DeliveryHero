
package com.app.sealteamdelivery.model.driver;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("CurrentOrders")
    @Expose
    private List<CurrentOrder> currentOrders = null;

    public List<CurrentOrder> getCurrentOrders() {
        return currentOrders;
    }

    public void setCurrentOrders(List<CurrentOrder> currentOrders) {
        this.currentOrders = currentOrders;
    }

    public Data withCurrentOrders(List<CurrentOrder> currentOrders) {
        this.currentOrders = currentOrders;
        return this;
    }

}
