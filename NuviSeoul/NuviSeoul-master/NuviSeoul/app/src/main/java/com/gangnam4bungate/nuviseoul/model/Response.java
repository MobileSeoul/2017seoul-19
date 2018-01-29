package com.gangnam4bungate.nuviseoul.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class Response {
    @SerializedName("header")
    Header header;
    @SerializedName("body")
    Body body;

    public Header getHeader() {
        return header;
    }
    public Body getBody() {
        return body;
    }
}
