package com.gangnam4bungate.nuviseoul.model.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class DetailHeader {
    @SerializedName("resultCode")
    String resultCode;
    @SerializedName("resultMsg")
    String resultMsg;

    public String getResultCode(){
        return this.resultCode;
    }
    public String getResultMsg(){
        return this.resultMsg;
    }
}
