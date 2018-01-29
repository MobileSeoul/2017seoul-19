package com.gangnam4bungate.nuviseoul.model.detail;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class DetailItem {
    @SerializedName("item")
    List<TourCourseDetailInfo> mitem;

    public List<TourCourseDetailInfo> getItemList() {
        return mitem;
    }
}
