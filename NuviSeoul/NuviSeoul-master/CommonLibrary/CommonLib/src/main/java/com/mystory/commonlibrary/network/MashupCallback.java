package com.mystory.commonlibrary.network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by wsseo on 2017. 7. 1..
 */

public interface MashupCallback {
    void onMashupSuccess(JSONObject object, int requestCode);
    void onMashupFail(VolleyError error, int requestCode);
}
