package com.gangnam4bungate.nuviseoul.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.CODES;
import com.gangnam4bungate.nuviseoul.config.searchDTO;
import com.gangnam4bungate.nuviseoul.config.searchDetailDTO;
import com.gangnam4bungate.nuviseoul.data.PlanDetailData;
import com.gangnam4bungate.nuviseoul.network.NetworkManager;
import com.gangnam4bungate.nuviseoul.ui.common.CommonActivity;
import com.mystory.commonlibrary.network.MashupCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.gangnam4bungate.nuviseoul.config.CODES.RequestCode.REQUEST_AREABASEDETAILLIST;
import static com.gangnam4bungate.nuviseoul.config.CODES.RequestCode.REQUEST_AREABASELIST;

/**
 * Created by wsseo on 2017. 7. 9..
 */

public class SearchActivity extends CommonActivity implements MashupCallback {
    private ArrayList<searchDTO> searchDTOs = new ArrayList<>();
    private ArrayList<searchDetailDTO> searchDetailDTOs = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();
    private ProgressBar mProgressBar;
    int Count = 0;
    private RelativeLayout mRL_Search;
    private EditText mEt_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        mRL_Search = (RelativeLayout) toolbar.findViewById(R.id.rl_search);
        mEt_title = (EditText)toolbar.findViewById(R.id.et_title);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        if(mRL_Search != null)
            mRL_Search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        mEt_title.setHint("");
                        String mEt_title_value = mEt_title.getText().toString();
                        if (mProgressBar != null)
                            mProgressBar.setVisibility(View.VISIBLE);
                        NetworkManager.getInstance().requestAreaBaseListInfo(SearchActivity.this, mEt_title_value);
                        if (searchDTOs != null) {
                            searchDTOs.clear();
                        }
                        if (searchDetailDTOs != null) {
                            searchDetailDTOs.clear();
                        }
                        hideKeyboard();
                    }catch (Exception e){

                    }
                }
            });

    }

    private void hideKeyboard(){
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(searchDTOs != null){
            searchDTOs.clear();
        }

        if(searchDetailDTOs != null){
            searchDetailDTOs.clear();
        }
    }

    @Override
    public void onMashupSuccess(JSONObject object, int requestCode) {
        try {
            if (requestCode == REQUEST_AREABASEDETAILLIST) {
                Log.d(CODES.TAG, "Search " + object.toString());
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    JSONObject response = jsonObject.getJSONObject("response");
                    JSONObject body = response.getJSONObject("body");
                    JSONObject locations = body.getJSONObject("items");
                    JSONObject item = locations.getJSONObject("item");
                    String contentId = item.getString("contentid");

                    String homePage = "";
                    String overView = "";

                    overView = item.getString("overview")
                            .replaceAll("</br>", "")
                            .replaceAll("<br>", "")
                            .replaceAll("<br />", "")
                            .replaceAll("<strong>", "")
                            .replaceAll("</strong>", "")
                            .replaceAll("&nbsp;", "")
                            .replaceAll("&lt;", "")
                            .replaceAll("&gt;", "");

                    if (overView.length() > 120) {
                        overView = overView.substring(0, 120) + "...";
                    }

                    homePage = item.optString("homepage", "주소가 없어요.");
                    if (homePage.contains("href")) {
                        int first_index = homePage.indexOf("\"") + 1;
                        int last_index = homePage.indexOf("\"", first_index);
                        homePage = homePage.substring(first_index, last_index).replace("http://", "");
                        if (homePage.length() > 25 && homePage.contains("/")) {
                            homePage = homePage.substring(0, homePage.indexOf("/"));
                        }
                    }
                    Log.d("homePage", homePage);
                    searchDetailDTOs.add(new searchDetailDTO(overView, homePage));
                    map.put(contentId, homePage + "|" + overView);

                    if (searchDetailDTOs.size() == Count) {
                        RecyclerView view = (RecyclerView) findViewById(R.id.main_recyclerview);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                        Log.d("searchDetailSize", String.valueOf(searchDetailDTOs.size()));
                        Log.d("searchSize", String.valueOf(searchDTOs.size()));
                        searchRecyclerView searchRecyclerView = new searchRecyclerView(searchDTOs, searchDetailDTOs, map, this);
                        view.setLayoutManager(layoutManager);
                        view.setAdapter(searchRecyclerView);
                        searchRecyclerView.notifyDataSetChanged();
                        if (mProgressBar != null)
                            mProgressBar.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_AREABASELIST) {
                Log.d(CODES.TAG, "Search " + object.toString());
                //        String jsonInfo = object.toString();
                //        JsonParser jsonParser = new JsonParser();

                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    JSONObject response = jsonObject.getJSONObject("response");
                    JSONObject body = response.getJSONObject("body");
                    JSONObject locations = body.getJSONObject("items");
                    JSONArray item = locations.getJSONArray("item");
                    //
                    Count = item.length();

                    String contentId = "";
                    String title = "";
                    String firstImage = "";
                    String mapX = "";
                    String mapY = "";
                    for (int i = 0; i < item.length(); i++) {
                        contentId = item.getJSONObject(i).getString("contentid");
                        title = item.getJSONObject(i).getString("title");
                        if (title.length() > 17) {
                            title = title.substring(0, 17) + "...";
                        }
                        mapX = item.getJSONObject(i).getString("mapx");
                        mapY = item.getJSONObject(i).getString("mapy");
                        firstImage = item.getJSONObject(i).optString("firstimage", "no Parsing");

                        searchDTOs.add(new searchDTO(contentId, title, firstImage, mapX, mapY));
                        NetworkManager.getInstance().requestAreaBaseDetailListInfo(this, contentId);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }catch(Exception e){

        }

    }

    @Override
    public void onMashupFail(VolleyError error, int requestCode) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
