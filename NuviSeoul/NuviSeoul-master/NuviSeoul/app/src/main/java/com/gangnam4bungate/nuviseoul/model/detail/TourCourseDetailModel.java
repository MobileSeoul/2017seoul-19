package com.gangnam4bungate.nuviseoul.model.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 2..
 */

public class TourCourseDetailModel {
    @SerializedName("response")
    DetailResponse response;

    public DetailResponse getResponse() {
        return response;
    }
}
