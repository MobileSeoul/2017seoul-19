package com.gangnam4bungate.nuviseoul.data;

import java.util.Date;

/**
 * Created by wsseo on 2017. 9. 23..
 */

public class PlanDetailData {
    int id;
    int planid = -1;
    Date startdate;
    Date enddate;
    String placename;
    int pathseq = -1;
    double latitude = 0.0d;
    double longitude = 0.0d;
    String subcontentid;

    public int getId() {
        return id;
    }

    public void setId(int id){ this.id = id;}

    public int getPlanid() {
        return planid;
    }

    public Date getStartDate() {
        return startdate;
    }

    public Date getEndDate() { return enddate; }

    public String getPlacename() {
        return placename;
    }

    public int getPathseq() {
        return pathseq;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setPlanid(int planid) {
        this.planid = planid;
    }

    public void setStartDate(Date datetime) {
        this.startdate = datetime;
    }

    public void setEndDate(Date datetime) { this.enddate = datetime;}

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public void setPathseq(int pathseq) {
        this.pathseq = pathseq;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSubcontentid() {
        return subcontentid;
    }

    public void setSubcontentid(String subcontentid) {
        this.subcontentid = subcontentid;
    }
}
