package com.gangnam4bungate.nuviseoul.model.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class TourCourseDetailInfo {
    @SerializedName("contentid")
    String contentid;
    @SerializedName("contenttypeid")
    String contenttypeid;
    @SerializedName("subcontentid")
    String subcontentid;
    @SerializedName("subdetailalt")
    String subdetailalt;
    @SerializedName("subdetailimg")
    String subdetailimg;
    @SerializedName("subdetailoverview")
    String subdetailoverview;
    @SerializedName("subname")
    String subname;
    @SerializedName("subnum")
    String subnum;

    public String getContentid() {
        return contentid;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public String getSubcontentid() {
        return subcontentid;
    }

    public String getSubdetailalt() {
        return subdetailalt;
    }

    public String getSubdetailimg() {
        return subdetailimg;
    }

    public String getSubdetailoverview() {
        return subdetailoverview;
    }

    public String getSubname() {
        return subname;
    }

    public String getSubnum() {
        return subnum;
    }
}
