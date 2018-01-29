package com.gangnam4bungate.nuviseoul.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.CODES;
import com.gangnam4bungate.nuviseoul.data.PlanDetailData;
import com.gangnam4bungate.nuviseoul.holder.TourCourseDetailListAdapter;
import com.gangnam4bungate.nuviseoul.model.TourCourseInfo;
import com.gangnam4bungate.nuviseoul.model.detail.TourCourseDetailInfo;
import com.gangnam4bungate.nuviseoul.model.detail.TourCourseDetailModel;
import com.gangnam4bungate.nuviseoul.model.map.MapInfo;
import com.gangnam4bungate.nuviseoul.model.map.MapModel;
import com.gangnam4bungate.nuviseoul.network.NetworkManager;
import com.gangnam4bungate.nuviseoul.ui.common.CommonActivity;
import com.gangnam4bungate.nuviseoul.ui.view.VerticalSpaceItemDecoration;
import com.google.gson.Gson;
import com.mystory.commonlibrary.network.MashupCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsseo on 2017. 10. 26..
 */

public class RecommendCourseDetailActivity extends CommonActivity implements MashupCallback{
    TextView mTv_title;
    RelativeLayout mrl_back;
    private ImageView mIv_add;
    TourCourseInfo info = new TourCourseInfo();
    private RecyclerView mRvTourCourseDetail;
    private TourCourseDetailListAdapter mTourCourseDetailListAdapter;
    private ArrayList<PlanDetailData> mPlanDetailDataList = new ArrayList<PlanDetailData>();
    public static RecommendCourseDetailActivity mRecommendCourseDetailActivity = null;
    public static RecommendCourseDetailActivity getInstance(){
        return mRecommendCourseDetailActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendcourse_detail);
        mRecommendCourseDetailActivity = this;

        Intent intent = getIntent();
        if(intent != null){
            info.setTitle(intent.getStringExtra("title"));
            info.setMapx(intent.getStringExtra("mapx"));
            info.setMapy(intent.getStringExtra("mapy"));
            info.setFirstimage(intent.getStringExtra("image1"));
            info.setFirstimage2(intent.getStringExtra("image2"));
            info.setContentid(intent.getStringExtra("contentid"));

            if(info.getContentid() != null)
                NetworkManager.getInstance().requestPlanCourseDetailListInfo(RecommendCourseDetailActivity.this,
                    info.getContentid(), "Y");
        }
        initLayout();
    }


    public void initLayout(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        mrl_back = (RelativeLayout) toolbar.findViewById(R.id.rl_back);
        if(mrl_back != null){
            mrl_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        mTv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        if(mTv_title != null && info.getTitle() != null)
            mTv_title.setText(info.getTitle());
        setSupportActionBar(toolbar);
        mIv_add = (ImageView) findViewById(R.id.iv_add);
        if(mIv_add != null){
            mIv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PlanEditActivity.class);
                    intent.putExtra("add_course", true);
                    startActivity(intent);
                }
            });
        }

        mRvTourCourseDetail = (RecyclerView) findViewById(R.id.rv_detail);

        mTourCourseDetailListAdapter = new TourCourseDetailListAdapter(getApplicationContext());
        mRvTourCourseDetail.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRvTourCourseDetail.setAdapter(mTourCourseDetailListAdapter);

        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(50);
        mRvTourCourseDetail.addItemDecoration(verticalSpaceItemDecoration);
    }

    public ArrayList<PlanDetailData> getPlanDetailDataList(){
        return mPlanDetailDataList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecommendCourseDetailActivity = null;
    }

    @Override
    public void onMashupSuccess(final JSONObject object, final int requestCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (requestCode){
                    case CODES.RequestCode.REQUEST_PLAN_COURSE_DETAIL_INFO:
                    {
                        try {
                            TourCourseDetailModel detailModel = new Gson().fromJson(object.toString(), TourCourseDetailModel.class);
                            if (detailModel != null) {
                                List<TourCourseDetailInfo> list = detailModel.getResponse().getBody().getItems().getItemList();
                                if(list != null) {
                                    mTourCourseDetailListAdapter.bindData(list);

                                    mPlanDetailDataList.clear();

                                    for(TourCourseDetailInfo info : list) {
                                        PlanDetailData data = new PlanDetailData();
                                        data.setPlacename(info.getSubname());
                                        data.setSubcontentid(info.getSubcontentid());
                                        mPlanDetailDataList.add(data);
                                    }

                                    for(TourCourseDetailInfo info : list) {
                                        NetworkManager.getInstance().requestPlanCourseDetailCommon(RecommendCourseDetailActivity.this,
                                                info.getSubcontentid(), "Y");
                                    }
                                }
                            }
                        }catch(Exception e){

                        }
                    }
                    break;
                    case CODES.RequestCode.REQUEST_PLAN_COURSE_DETAIL_COMMON:
                    {
                        try {
                            MapModel model = new Gson().fromJson(object.toString(), MapModel.class);
                            if (model != null) {
                                MapInfo info = model.getResponse().getBody().getItems().getItem();
                                for (PlanDetailData data : mPlanDetailDataList) {
                                    if (data != null && info != null && data.getSubcontentid() != null
                                            && data.getSubcontentid().equals(info.getContentid())) {
                                        data.setLatitude(Double.valueOf(info.getMapy()));
                                        data.setLongitude(Double.valueOf(info.getMapx()));
                                    }
                                }
                            }
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

            }
        });
    }
}
