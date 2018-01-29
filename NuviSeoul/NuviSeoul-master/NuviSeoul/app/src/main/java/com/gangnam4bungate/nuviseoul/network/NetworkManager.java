package com.gangnam4bungate.nuviseoul.network;

import android.content.Context;
import android.net.Uri;

import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.CODES;
import com.mystory.commonlibrary.network.HttpClientManager;

/**
 * Created by wsseo on 2017. 6. 27..
 */

public class NetworkManager {
    private static NetworkManager mNetworkManager;
    public static NetworkManager getInstance(){
        if(mNetworkManager == null)
            mNetworkManager = new NetworkManager();
        return mNetworkManager;
    }

    public void requestAreaBaseListInfo(Object object, String keyword){
        try {
            Uri builtUri = Uri.parse(CODES.DefaultDomain + CODES.URLCodes.URL_SEARCH_KEYWORD_LIST)
                    .buildUpon()
                    .appendQueryParameter(CODES.CommonCodes.KEYWORD, keyword)
                    .appendQueryParameter(CODES.CommonCodes.MOBILEOS, "AND")
                    .appendQueryParameter(CODES.CommonCodes.MOBILEAPP, ((Context) object).getString(R.string.app_name))
                    .appendQueryParameter(CODES.CommonCodes._TYPE, "json")
                    .build();

            String url = builtUri.toString() + "&" + CODES.CommonCodes.SERVICEKEY + "=" + CODES.Dev_ServiceKey;

            HttpClientManager.getInstance((Context) object).sendGet(object, url, CODES.RequestCode.REQUEST_AREABASELIST);
        }catch(Exception e){

        }
    }

    public void requestAreaBaseDetailListInfo(Object object, String contentid){
        try {
            Uri builtUri = Uri.parse((CODES.DefaultDomain + CODES.URLCodes.URL_DETAIL_COMMON) + "ServiceKey=" + CODES.Dev_ServiceKey + "&contentId="+ contentid +"&defaultYN=Y&MobileOS=AND&overviewYN=Y&MobileApp=" + ((Context) object).getString(R.string.app_name) + "&"+CODES.CommonCodes._TYPE + "=" +"json");

            String url = builtUri.toString();

            HttpClientManager.getInstance((Context) object).sendGet(object, url, CODES.RequestCode.REQUEST_AREABASEDETAILLIST);
        }catch (Exception e){

        }
    }

    public void requestPlanCourseListInfo(Object object, String pageNo) {

        try {
            Uri builtUri = Uri.parse(CODES.DefaultDomain + CODES.URLCodes.URL_AREA_BASED_LIST)
                    .buildUpon()
                    .appendQueryParameter(CODES.CommonCodes.NUMOFROWS, "30")
                    .appendQueryParameter(CODES.CommonCodes.PAGENO, pageNo)
                    .appendQueryParameter(CODES.CommonCodes.CONTENTTYPEID, CODES.API_CONTENTTYPE.TOUR_COURSE) //여행코스
                    .appendQueryParameter(CODES.CommonCodes.AREACODE, "1") //서울
                    .appendQueryParameter(CODES.CommonCodes.ARRANGE, "P") //조회순
                    .appendQueryParameter(CODES.CommonCodes.CAT1, "C01") //여행코스
                    .appendQueryParameter(CODES.CommonCodes.MOBILEOS, "AND")
                    .appendQueryParameter(CODES.CommonCodes.MOBILEAPP, ((Context) object).getString(R.string.app_name))
                    .appendQueryParameter(CODES.CommonCodes._TYPE, "json")
                    .build();

            String url = builtUri.toString() + "&" + CODES.CommonCodes.SERVICEKEY + "=" + CODES.Dev_ServiceKey;

            HttpClientManager.getInstance((Context) object).sendGet(object, url, CODES.RequestCode.REQUEST_PLAN_COURSE_LIST);
        }catch(Exception e){

        }
    }

    public void requestPlanCourseDetailListInfo(Object object, String contentid, String detailYN) {

        try {
            Uri builtUri = Uri.parse(CODES.DefaultDomain + CODES.URLCodes.URL_DETAIL_INFO)
                    .buildUpon()
                    .appendQueryParameter(CODES.CommonCodes.CONTENTTYPEID, CODES.API_CONTENTTYPE.TOUR_COURSE) //여행코스
                    .appendQueryParameter(CODES.CommonCodes.CONTENTID, contentid)
                    .appendQueryParameter(CODES.CommonCodes.DETAILYN, detailYN)
                    .appendQueryParameter(CODES.CommonCodes.MOBILEOS, "AND")
                    .appendQueryParameter(CODES.CommonCodes.MOBILEAPP, ((Context) object).getString(R.string.app_name))
                    .appendQueryParameter(CODES.CommonCodes._TYPE, "json")
                    .build();

            String url = builtUri.toString() + "&" + CODES.CommonCodes.SERVICEKEY + "=" + CODES.Dev_ServiceKey;

            HttpClientManager.getInstance((Context) object).sendGet(object, url, CODES.RequestCode.REQUEST_PLAN_COURSE_DETAIL_INFO);
        }catch(Exception e){

        }
    }

    public void requestPlanCourseDetailCommon(Object object, String contentid, String MAPINFOYN) {

        try {
            Uri builtUri = Uri.parse(CODES.DefaultDomain + CODES.URLCodes.URL_DETAIL_COMMON)
                    .buildUpon()
                    .appendQueryParameter(CODES.CommonCodes.CONTENTID, contentid)
                    .appendQueryParameter(CODES.CommonCodes.MAPINFOYN, MAPINFOYN)
                    .appendQueryParameter(CODES.CommonCodes.MOBILEOS, "AND")
                    .appendQueryParameter(CODES.CommonCodes.MOBILEAPP, ((Context) object).getString(R.string.app_name))
                    .appendQueryParameter(CODES.CommonCodes._TYPE, "json")
                    .build();

            String url = builtUri.toString() + "&" + CODES.CommonCodes.SERVICEKEY + "=" + CODES.Dev_ServiceKey;

            HttpClientManager.getInstance((Context) object).sendGet(object, url, CODES.RequestCode.REQUEST_PLAN_COURSE_DETAIL_COMMON);
        }catch(Exception e){

        }
    }

    public void requsetRecommendLocationInfo(Object object)
    {
        String key = "Wasn0OGY43ReOgSy5nz8ZNURGTw4Y5MRPt%2B1rJw0xZCEbQG07s6n5hjqHHqzzIRBCP8U1H64Q0RIgAZPRWSPlA%3D%3D";


        try {
            Uri builtUri = Uri.parse("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=" + key +
                    "&numOfRows=20&arrange=B&MobileOS=AND&MobileApp=NuviSeoul&contentTypeid=14&areaCode=1&cat1=A02&cat2=A0206&_type=json");

            String url = builtUri.toString();

            HttpClientManager.getInstance((Context) object).sendGet(object, url, CODES.RequestCode.REQUEST_RECOMMEND_LOCATION_LIST);

        }catch(Exception e){

        }
    }
}
