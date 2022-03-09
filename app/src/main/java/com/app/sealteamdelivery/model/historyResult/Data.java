
package com.app.sealteamdelivery.model.historyResult;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("OrdersCount")
    @Expose
    private Integer ordersCount;
    @SerializedName("CurrentBalance")
    @Expose
    private Integer currentBalance;
    @SerializedName("OrderHistory")
    @Expose
    private List<OrderHistory> orderHistory = null;

    public Integer getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(Integer ordersCount) {
        this.ordersCount = ordersCount;
    }

    public Integer getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Integer currentBalance) {
        this.currentBalance = currentBalance;
    }

    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }

}
