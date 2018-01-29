package com.gangnam4bungate.nuviseoul.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wsseo on 2017. 9. 23..
 */

public class PlanData {
    int id;
    String name = "";
    Date start_date;
    Date end_date;
    int placenum = 0;
    ArrayList<PlanDetailData> mDetailDataList;

    public int getId() {
        return id;
    }

    public void setId(int id){ this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public ArrayList<PlanDetailData> getDetailDataList() {
        return mDetailDataList;
    }

    public void setDetailDataList(ArrayList<PlanDetailData> detailList) {
        this.mDetailDataList = detailList;
    }

    public int getPlacenum() {
        return placenum;
    }

    public void setPlacenum(int placenum) {
        this.placenum = placenum;
    }
}
