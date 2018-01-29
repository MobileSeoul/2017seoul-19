package com.gangnam4bungate.nuviseoul.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.CODES;
import com.gangnam4bungate.nuviseoul.data.PlanData;
import com.gangnam4bungate.nuviseoul.database.DBOpenHelper;
import com.gangnam4bungate.nuviseoul.database.DataBases;
import com.gangnam4bungate.nuviseoul.holder.PlanListAdapter;
import com.gangnam4bungate.nuviseoul.holder.TabPagerAdapter;
import com.gangnam4bungate.nuviseoul.model.TourCourseModel;
import com.gangnam4bungate.nuviseoul.network.DataManager;
import com.gangnam4bungate.nuviseoul.network.NetworkManager;
import com.gangnam4bungate.nuviseoul.ui.common.CommonActivity;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mystory.commonlibrary.network.MashupCallback;
import com.mystory.commonlibrary.utils.Util;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends CommonActivity{

    private TextView mTv_title;
    private RelativeLayout mRl_search;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    public final static int PERMISSION_ACCESS_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initPermission();
    }

    public void initPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    public void initLayout(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        mRl_search = (RelativeLayout) findViewById(R.id.rl_search);
        if(mRl_search != null){
            mRl_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                }
            });
        }
        setSupportActionBar(toolbar);

        // Initializing the TabLayout
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_plan_title)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_recommend_title)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFF00"));
        mTabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        mTabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFF00"));

        // Initializing ViewPager
        mViewPager = (ViewPager) findViewById(R.id.vp_pager);

        // Creating TabPagerAdapter adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        // Set TabSelectedListener
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();

        try{
            if(Util.getConnectivityStatus(getApplicationContext()) == Util.TYPE_NOT_CONNECTED){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);
                alertDialogBuilder
                        .setMessage(getString(R.string.network_error_msg))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }catch(Exception e){

        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);
            alertDialogBuilder
                    .setMessage(getString(R.string.finish_msg))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            })
                    .setNegativeButton(getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }catch(Exception e){
            finish();
        }
    }


    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CODES.ActivityResult.PLAN_EDIT:
            {
                initLayout();
            }
            break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
            {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getString(R.string.permission_location_fail_msg), Toast.LENGTH_LONG).show();
                }
            }
            return;
        }
    }
}
