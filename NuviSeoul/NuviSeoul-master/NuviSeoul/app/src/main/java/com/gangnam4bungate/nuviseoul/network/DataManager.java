package com.gangnam4bungate.nuviseoul.network;

import com.gangnam4bungate.nuviseoul.model.TourCourseModel;

/**
 * Created by wsseo on 2017. 10. 26..
 */

public class DataManager {
    private static DataManager mDataManager;
    public static DataManager getInstance(){
        if(mDataManager == null)
            mDataManager = new DataManager();
        return mDataManager;
    }

    private TourCourseModel mTourCourseModel;

    public TourCourseModel getTourCourseModel(){
        return mTourCourseModel;
    }

    public void setTourCourseModel(TourCourseModel model){
        this.mTourCourseModel = model;
    }
}
