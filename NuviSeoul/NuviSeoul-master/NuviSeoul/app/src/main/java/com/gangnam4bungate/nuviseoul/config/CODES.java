package com.gangnam4bungate.nuviseoul.config;

/**
 * Created by wsseo on 2017. 7. 2..
 */

public class CODES {
    public static String TAG = "NuviSeoul";
    public static String DefaultDomain = "http://api.visitkorea.or.kr";
    public static String Dev_ServiceKey = "Wasn0OGY43ReOgSy5nz8ZNURGTw4Y5MRPt%2B1rJw0xZCEbQG07s6n5hjqHHqzzIRBCP8U1H64Q0RIgAZPRWSPlA%3D%3D";

    public static class URLCodes {
        public static String URL_SEARCH_KEYWORD_LIST = "/openapi/service/rest/KorService/searchKeyword?";
        public static String URL_AREA_BASED_LIST = "/openapi/service/rest/KorService/areaBasedList?";
        public static String URL_DETAIL_INFO = "/openapi/service/rest/KorService/detailInfo?";
        public static String URL_DETAIL_COMMON = "/openapi/service/rest/KorService/detailCommon?";
        public static String URL_SEARCH = "/v1/search/blog?query=";
    }

    public static class CommonCodes {
        public static String SERVICEKEY = "ServiceKey";
        public static String AREACODE = "areaCode";
        public static String CONTENTTYPEID = "contentTypeId";
        public static String KEYWORD = "keyword";
        public static String MOBILEOS = "MobileOS";
        public static String MOBILEAPP = "MobileApp";
        public static String CAT1 = "cat1";
        public static String _TYPE = "_type";
        public static String CONTENTID = "contentId";
        public static String DETAILYN = "detailYN";
        public static String MAPINFOYN = "mapinfoYN";
        public static String ARRANGE = "arrange";
        public static String NUMOFROWS = "numOfRows";
        public static String PAGENO = "pageNo";
    }

    public static class RequestCode {
        public static final int CODE_DEFAULT = 1000;
        public static final int REQUEST_AREABASELIST = CODE_DEFAULT + 1;
        public static final int REQUEST_SEARCH = CODE_DEFAULT + 2;
        public static final int REQUEST_AREABASEDETAILLIST = CODE_DEFAULT + 3;
        public static final int REQUEST_PLAN_COURSE_LIST = CODE_DEFAULT + 4;
        public static final int REQUEST_RECOMMEND_LOCATION_LIST = CODE_DEFAULT + 5;
        public static final int REQUEST_PLAN_COURSE_DETAIL_INFO = CODE_DEFAULT + 6;
        public static final int REQUEST_PLAN_COURSE_DETAIL_COMMON = CODE_DEFAULT + 7;
    }

    public static class API_CONTENTTYPE {
        public static String TOUR_SIGHTS = "12";//관광지
        public static String CULTURAL_FACILITIES = "14";//문화시설
        public static String FESTIVAL = "15";//축제,공연,행사
        public static String TOUR_COURSE = "25";//여행코스
        public static String REPORTS = "28";//레포츠
        public static String ACCOMMODATION = "32";//숙박
        public static String SHOPPING = "38";//쇼핑
        public static String FOOD = "39";//음식
    }

    public static class ActivityResult {
        public static final int LOCATIONS = 0;
        public static final int PLAN_EDIT = 1;
    }
}
