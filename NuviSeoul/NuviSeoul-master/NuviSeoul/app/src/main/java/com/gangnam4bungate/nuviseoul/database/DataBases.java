package com.gangnam4bungate.nuviseoul.database;

import android.provider.BaseColumns;

/**
 * Created by wsseo on 2017. 8. 19..
 */

public class DataBases {
    public static final class CreatePlanDB implements BaseColumns {
        public static final String _TABLENAME = "plan";

        public static final String _NAME = "name";
        public static final String _STARTDATE = "startdate";
        public static final String _ENDDATE = "enddate";

        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        + _ID  + " integer primary key autoincrement, "
                        + _NAME + " text not null, "
                        + _STARTDATE + " date not null, "
                        + _ENDDATE + " date not null);";
    }

    public static final class CreatePlanDetailDB implements BaseColumns {

        public static final String _TABLENAME = "plandetail";
        public static final String _PLANID = "planid";
        public static final String _STARTDATE = "startdate";
        public static final String _ENDDATE = "enddate";
        public static final String _PLACE_NAME = "placename";
        public static final String _PATH_SEQ = "pathseq";
        public static final String _PLACE_GPS_LATITUDE = "latitude";
        public static final String _PLACE_GPS_LONGITUDE = "longitude";

        public static final String _CREATE =
                "create table "+ _TABLENAME +"("
                        + _ID  + " integer primary key autoincrement, "
                        + _PLANID  + " integer, "
                        + _STARTDATE + " date not null, "
                        + _ENDDATE + " date not null, "
                        + _PLACE_NAME + " text not null, "
                        + _PATH_SEQ  + " integer, "
                        + _PLACE_GPS_LATITUDE + " double, "
                        + _PLACE_GPS_LONGITUDE + " double)";

    }
}
