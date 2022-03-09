
package com.app.sealteamdelivery.model.driver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Driver {

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

    public Driver withIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
        return this;
    }

    public String getFaildReason() {
        return faildReason;
    }

    public void setFaildReason(String faildReason) {
        this.faildReason = faildReason;
    }

    public Driver withFaildReason(String faildReason) {
        this.faildReason = faildReason;
        return this;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Driver withData(Data data) {
        this.data = data;
        return this;
    }

}
