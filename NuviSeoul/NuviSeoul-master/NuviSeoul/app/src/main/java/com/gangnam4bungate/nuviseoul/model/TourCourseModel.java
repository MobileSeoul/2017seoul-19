package com.gangnam4bungate.nuviseoul.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 2..
 */

public class TourCourseModel {
    @SerializedName("response")
    Response response;

    public Response getResponse() {
        return response;
    }
}
