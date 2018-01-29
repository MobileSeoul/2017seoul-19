package com.mystory.commonlibrary.network;

import android.content.Context;
import android.content.res.ObbInfo;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wsseo on 2017. 6. 30..
 */

public class HttpClientManager {
    private static HttpClientManager mHttpClientManager = null;
    private RequestQueue mRequestQueue;
    String token = "JOTWF9wQzd4ARDl8aT3A";//애플리케이션 클라이언트 아이디값";
    String header = "Bearer " + token; // Bearer 다음에 공백 추가

    public HttpClientManager(Context context){
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static HttpClientManager getInstance(Context context){
        if(mHttpClientManager == null) {
            mHttpClientManager = new HttpClientManager(context);
        }
        return mHttpClientManager;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public void sendPost(final Object object, String url, final int requestCode){
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MashupCallback callback = (MashupCallback)object;
                            if(callback != null)
                                callback.onMashupSuccess(response, requestCode);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            MashupCallback callback = (MashupCallback)object;
                            if(callback != null)
                                callback.onMashupFail(error, requestCode);
                        }
                    });
            mRequestQueue.add(request);
        }catch(Exception e){

        }
    }

    public void sendGet(final Object object, String url, final int requestCode){
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MashupCallback callback = (MashupCallback)object;
                            if(callback != null)
                                callback.onMashupSuccess(response, requestCode);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            MashupCallback callback = (MashupCallback)object;
                            if(callback != null)
                                callback.onMashupFail(error, requestCode);
                        }
                    });
            mRequestQueue.add(request);
        }catch(Exception e){

        }
    }

    public void sendGet_naver(final Object object, String url, final int requestCode, final String clientid, final String clientSecret){
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MashupCallback callback = (MashupCallback)object;
                            if(callback != null)
                                callback.onMashupSuccess(response, requestCode);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            MashupCallback callback = (MashupCallback)object;
                            if(callback != null)
                                callback.onMashupFail(error, requestCode);
                        }
                    })
            {
                /**
                 * Returns a list of extra HTTP headers to go along with this request. Can
                 * throw {@link AuthFailureError} as authentication may be required to
                 * provide these values.
                 *
                 * @throws AuthFailureError In the event of auth failure
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("X-Naver-Client-Id", clientid);
                    headers.put("X-Naver-Client-Secret", clientSecret);
                    return headers;
                }
            };
            mRequestQueue.add(request);
        }catch(Exception e){

        }
    }

}
