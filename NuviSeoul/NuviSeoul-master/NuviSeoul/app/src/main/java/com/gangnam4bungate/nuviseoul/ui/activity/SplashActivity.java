package com.gangnam4bungate.nuviseoul.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.CODES;
import com.gangnam4bungate.nuviseoul.model.TourCourseModel;
import com.gangnam4bungate.nuviseoul.network.DataManager;
import com.gangnam4bungate.nuviseoul.network.NetworkManager;
import com.gangnam4bungate.nuviseoul.ui.common.CommonActivity;
import com.google.gson.Gson;
import com.mystory.commonlibrary.network.MashupCallback;

import org.json.JSONObject;

/**
 * Created by wsseo on 2017. 9. 10..
 */

public class SplashActivity extends CommonActivity implements MashupCallback{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NetworkManager.getInstance().requestPlanCourseListInfo(SplashActivity.this, "1");
    }

    @Override
    public void onMashupSuccess(final JSONObject object, final int requestCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (requestCode){
                    case CODES.RequestCode.REQUEST_PLAN_COURSE_LIST:
                    {
                        try {
                            TourCourseModel model = new Gson().fromJson(object.toString(), TourCourseModel.class);
                            if (model != null) {
                                DataManager.getInstance().setTourCourseModel(model);
                            }
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }catch(Exception e){

                        }
                    }
                    break;
                }
            }
        });
    }

    @Override
    public void onMashupFail(VolleyError error, int requestCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
