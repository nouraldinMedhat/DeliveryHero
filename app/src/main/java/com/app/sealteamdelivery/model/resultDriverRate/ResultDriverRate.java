
package com.app.sealteamdelivery.model.resultDriverRate;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultDriverRate {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("FaildReason")
    @Expose
    private String faildReason;
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
