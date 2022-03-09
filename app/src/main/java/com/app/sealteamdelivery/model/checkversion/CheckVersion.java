
package com.app.sealteamdelivery.model.checkversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckVersion {

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

    public CheckVersion withIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
        return this;
    }

    public String getFaildReason() {
        return faildReason;
    }

    public void setFaildReason(String faildReason) {
        this.faildReason = faildReason;
    }

    public CheckVersion withFaildReason(String faildReason) {
        this.faildReason = faildReason;
        return this;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public CheckVersion withData(Data data) {
        this.data = data;
        return this;
    }

}
