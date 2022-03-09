
package com.app.sealteamdelivery.model.currentorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentOrders {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("FaildReason")
    @Expose
    private String faildReason;
    @SerializedName("Data")
    @Expose
    private Data data;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getFaildReason() {
        return faildReason;
    }

    public void setFaildReason(String faildReason) {
        this.faildReason = faildReason;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
