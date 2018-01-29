package com.gangnam4bungate.nuviseoul.map;

/**
 * Created by choi on 2017-08-16.
 * https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.559954,126.975312&radius=1&key=AIzaSyBV9mZADbZF38w9wwkdg1ROrFcVEYZpu5k
 */

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */

public class PlaceFinder {
    private static final String PLACE_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyBV9mZADbZF38w9wwkdg1ROrFcVEYZpu5k";

    private PlaceFinderListener listener;
    private LatLng mLatLng;
    private String mLocation="";

    public PlaceFinder (PlaceFinderListener listener, LatLng latLng) {
        this.listener = listener;
        this.mLatLng=latLng;
        mLocation=latLng.latitude+","+latLng.longitude;
    }

    public void execute() throws UnsupportedEncodingException {
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlLocation = URLEncoder.encode(mLocation, "utf-8");
        Log.d("TEST : ", PLACE_URL_API + "location=" + urlLocation + "&radius=10&key=" + GOOGLE_API_KEY+"&language=ko");
        return PLACE_URL_API + "location=" + urlLocation + "&radius=10&key=" + GOOGLE_API_KEY+"&language=ko";
        //return PLACE_URL_API + "location=" + urlLocation + "&radius=1&key=" + GOOGLE_API_KEY;

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

        String _Title="";
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonResults = jsonData.getJSONArray("results");
        //0번째 데이터는 대한민국을 나타냄
      //for (int i = 0; i < jsonResults.length(); i++) {
            JSONObject jsonPlace = jsonResults.getJSONObject(1/*i*/);
           JSONObject geometryJson = jsonPlace.getJSONObject("geometry");
           JSONObject locationJson =  geometryJson.getJSONObject("location");
           double lat=Double.parseDouble(locationJson.getString("lat"));
           double lng=Double.parseDouble(locationJson.getString("lng"));
          //if((lng==(this.mLatLng.longitude))
           //   &&(lat==(this.mLatLng.latitude))){
                  //JSONObject nameJson = jsonPlace.getJSONObject("name");
              _Title= jsonPlace.getString("name");
            //  break;
         //}
      // }
       listener.onPlaceFinderSuccess(this.mLatLng,_Title);
    }
}

