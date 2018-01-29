package com.gangnam4bungate.nuviseoul.map;

/**
 * Created by choi on 2017-08-16.
 * 대한민국은 법으로 google에서 대중 교통만  길찾기를 지원함 &mode=transit
 */
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */

public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDbA3nymkyx7Hx7ilU_WZHoToZsPo06XN8";
    private DirectionFinderListener listener;
    private String origin;
    private String destination;
    private List<Route> mRoutes;
    private String unigueIdx;
    private String mStartTitle;
    private String mEndTitle;


    public DirectionFinder(DirectionFinderListener listener, List<Route> routes  ,String origin, String destination,String pEndTitle) {
        this.listener = listener;
        this.origin = origin.trim();
        this.destination = destination.trim();
      this.mRoutes = routes;

        if((pEndTitle==null)
                ||(pEndTitle.length()==0)){
            this.mEndTitle =  this.destination;
        }else{
            this.mEndTitle = pEndTitle.trim();
        }
        //------------------------------------------------------------
        int iSize=this.mRoutes.size();
        if(0<iSize)
        {
            //마지막  경로의 endTitle를 가져옴
            Route route =  this.mRoutes.get((iSize-1));
            this.mStartTitle = route.endTitle ;
        } else   {
            //초기치는 시작과 종료가 같음
            this.mStartTitle =this.mEndTitle;
        }
        this.unigueIdx=this.origin+"@"+this.destination;
    }

    public void execute() throws UnsupportedEncodingException {
        //중복데이터 확인 : 시작점과 끝점이 같으면 중복데이터로 제외함
        boolean bInsert=true;

        for(Route route : this.mRoutes )  {
            if(route.index ==this.unigueIdx){
                bInsert=false;
                break;
            }
        }

        if(bInsert){
            listener.onDirectionFinderStart();
            new DownloadRawData().execute(createUrl());
        }
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");

        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&mode=transit&key=" + GOOGLE_API_KEY+"&language=ko";

        //기본 영문
        //return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&mode=transit&key=" + GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;

        //List<Route> routes = new ArrayList<Route>();
        //this.mRoutes
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
       // for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(0/*i*/);
            Route route = new Route();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");
            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
             if(route.startAddress.compareToIgnoreCase(route.endAddress)!=0) {
                 route.points = decodePolyLine(overview_polylineJson.getString("points"));
             }
            //인덱스 : 시작과 출발 좌표로 함
            route.index = this.unigueIdx;

            //명칭
            route.startTitle = this.mStartTitle;
            route.endTitle = this.mEndTitle;

            this.mRoutes.add(route);
       //}
        listener.onDirectionFinderSuccess(this.mRoutes/*routes*/);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}