package com.gangnam4bungate.nuviseoul.model.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class DetailResponse {
    @SerializedName("header")
    DetailHeader header;
    @SerializedName("body")
    DetailBody body;

    public DetailHeader getHeader() {
        return header;
    }
    public DetailBody getBody() {
        return body;
    }
}
