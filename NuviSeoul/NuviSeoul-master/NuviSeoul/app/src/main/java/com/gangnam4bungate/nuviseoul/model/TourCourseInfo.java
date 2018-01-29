package com.gangnam4bungate.nuviseoul.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class TourCourseInfo {
    @SerializedName("areacode")
    String areacode;
    @SerializedName("cat1")
    String cat1;
    @SerializedName("cat2")
    String cat2;
    @SerializedName("cat3")
    String cat3;
    @SerializedName("contentid")
    String contentid;
    @SerializedName("contenttypeid")
    String contenttypeid;
    @SerializedName("createdtime")
    String createdtime;
    @SerializedName("firstimage")
    String firstimage;
    @SerializedName("firstimage2")
    String firstimage2;
    @SerializedName("mapx")
    String mapx;
    @SerializedName("mapy")
    String mapy;
    @SerializedName("mlevel")
    String mlevel;
    @SerializedName("modifiedtime")
    String modifiedtime;
    @SerializedName("readcount")
    String readcount;
    @SerializedName("sigungucode")
    String sigungucode;
    @SerializedName("title")
    String title;

    public String getAreacode() {
        return areacode;
    }

    public String getCat1() {
        return cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public String getContentid() {
        return contentid;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public String getMapx() {
        return mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public String getMlevel() {
        return mlevel;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public String getReadcount() {
        return readcount;
    }

    public String getSigungucode() {
        return sigungucode;
    }

    public String getTitle() {
        return title;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public void setContenttypeid(String contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    public void setMlevel(String mlevel) {
        this.mlevel = mlevel;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public void setReadcount(String readcount) {
        this.readcount = readcount;
    }

    public void setSigungucode(String sigungucode) {
        this.sigungucode = sigungucode;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
