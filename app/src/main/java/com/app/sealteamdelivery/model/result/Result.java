package com.app.sealteamdelivery.model.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("FaildReason")
    @Expose
    private String faildReason;
    @SerializedName("Data")
    @Expose
    private Integer data;

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

    public Integer getData() {
    return data;
    }

    public void setData(Integer data) {
this.data = data;
}

}
