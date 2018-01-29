package com.gangnam4bungate.nuviseoul.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.CODES;
import com.gangnam4bungate.nuviseoul.data.PlanDetailData;
import com.gangnam4bungate.nuviseoul.data.PlanDetailList;
import com.gangnam4bungate.nuviseoul.database.DBOpenHelper;
import com.gangnam4bungate.nuviseoul.database.DataBases;
import com.gangnam4bungate.nuviseoul.holder.PlanDetailAdapter;
import com.gangnam4bungate.nuviseoul.ui.common.CommonGoogleMapActivity;
import com.gangnam4bungate.nuviseoul.ui.view.HorizontalSpaceItemDecoration;
import com.gangnam4bungate.nuviseoul.ui.view.VerticalSpaceItemDecoration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mystory.commonlibrary.network.MashupCallback;
import com.mystory.commonlibrary.utils.Util;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlanDetailActivity extends CommonGoogleMapActivity implements OnMapReadyCallback, MashupCallback,
                                                                GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener,
                                                                LocationListener{

    private ImageView mIv_Modify;
    private ImageView mIv_Delete;
    RelativeLayout mrl_back;
    private TextView mTv_title;
    private ImageView mIv_search;
    private RecyclerView mRvPlan;
    private PlanDetailAdapter mPlanAdapter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    public final static int PERMISSION_ACCESS_FINE_LOCATION = 0;
    DBOpenHelper mDBOpenHelper;
    public int mPlanid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if(intent != null){
            mPlanid = intent.getIntExtra("id", -1);
        }
        if(savedInstanceState != null){
            mCurrentLocation = savedInstanceState.getParcelable(LOCATION);
        }


        setContentView(R.layout.activity_plandetail);

        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mDBOpenHelper = DBOpenHelper.getInstance();
        if(mDBOpenHelper != null && mDBOpenHelper.isOpen() == false)
            mDBOpenHelper.open(getApplicationContext());
        initLayout();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
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
        if(mGoogleApiClient.isConnected()){
            getDeviceLocation();
        }
        super.onResume();
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
        if(mTv_title != null)
            mTv_title.setText(getString(R.string.main_title));
        setSupportActionBar(toolbar);


        mIv_search = (ImageView) findViewById(R.id.iv_search);
        if(mIv_search != null){
            mIv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                }
            });
        }

        mIv_Modify = (ImageView) findViewById(R.id.iv_modify);
        if(mIv_Modify != null){
            mIv_Modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PlanEditActivity.class);
                    intent.putExtra("edit", true);
                    intent.putExtra("planid", mPlanid);
                    startActivityForResult(intent, CODES.ActivityResult.PLAN_EDIT);
                }
            });
        }
        mIv_Delete = (ImageView) findViewById(R.id.iv_delete);
        if(mIv_Delete != null){
            mIv_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            PlanDetailActivity.this);
                    alertDialogBuilder
                            .setMessage(getString(R.string.delete_msg))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            mDBOpenHelper.plandetailDelete(mPlanid);
                                            mDBOpenHelper.planDelete(mPlanid);

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
                }
            });
        }

        ArrayList<PlanDetailList> allList = new ArrayList<PlanDetailList>();
        ArrayList<PlanDetailData> detailList = null;
        String sDate = "";
        String eDate = "";
        String name = "";
        if(mPlanid >= 0) {
            try {
                Cursor cursor = mDBOpenHelper.plan_getColumn(mPlanid);
                if (cursor != null && cursor.getCount() > 0) {
                    name = cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDB._NAME));
                    cursor.close();

                    Cursor c = mDBOpenHelper.plandetail_getColumn(mPlanid);
                    if (c != null) {
                        c.moveToFirst();
                        do {
                            PlanDetailData data = new PlanDetailData();
                            data.setPlanid(mPlanid);
                            try {
                                String tempsDate = c.getString(c.getColumnIndex(DataBases.CreatePlanDetailDB._STARTDATE));
                                String tempeDate = c.getString(c.getColumnIndex(DataBases.CreatePlanDetailDB._ENDDATE));
                                if (c.isFirst()) {
                                    detailList = new ArrayList<PlanDetailData>();
                                } else if ((sDate.equals(tempsDate) == false)
                                        || (eDate.equals(tempeDate) == false)) {
                                    PlanDetailList list = new PlanDetailList();
                                    list.setArrayList(detailList);
                                    allList.add(list);
                                    detailList = new ArrayList<PlanDetailData>();
                                }

                                sDate = c.getString(c.getColumnIndex(DataBases.CreatePlanDetailDB._STARTDATE));
                                eDate = c.getString(c.getColumnIndex(DataBases.CreatePlanDetailDB._ENDDATE));

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date start_date = sdf.parse(sDate);
                                Date end_date = sdf.parse(eDate);

                                data.setStartDate(start_date);
                                data.setEndDate(end_date);
                            } catch (Exception e) {

                            }
                            data.setPathseq(c.getInt(c.getColumnIndex(DataBases.CreatePlanDetailDB._PATH_SEQ)));
                            data.setPlacename(c.getString(c.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_NAME)));
                            data.setLatitude(c.getDouble(c.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_GPS_LATITUDE)));
                            data.setLongitude(c.getDouble(c.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_GPS_LONGITUDE)));
                            detailList.add(data);


                        } while (c.moveToNext());

                        PlanDetailList list = new PlanDetailList();
                        list.setArrayList(detailList);
                        allList.add(list);

                        c.close();
                    }
                }
            }catch(Exception e){

            }
        }

        if(allList != null && allList.size() > 0){
            mTv_title.setText(name + " " + getString(R.string.plandetail_subtitle));

            mPlanAdapter = new PlanDetailAdapter(this);
            mPlanAdapter.bindData(allList);
            mRvPlan = (RecyclerView) findViewById(R.id.rv_plan);
            mRvPlan.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRvPlan.setLayoutManager(layoutManager);
            mRvPlan.setAdapter(mPlanAdapter);

            HorizontalSpaceItemDecoration verticalSpaceItemDecoration = new HorizontalSpaceItemDecoration(50);
            mRvPlan.addItemDecoration(verticalSpaceItemDecoration);

            if(findViewById(R.id.iv_left) != null){
                findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
            }
            if(findViewById(R.id.iv_right) != null){
                findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
            }
            mPlanAdapter.notifyDataSetChanged();
        } else {
            mRvPlan = (RecyclerView) findViewById(R.id.rv_plan);
            mRvPlan.setVisibility(View.GONE);

            if(findViewById(R.id.iv_left) != null){
                findViewById(R.id.iv_left).setVisibility(View.GONE);
            }
            if(findViewById(R.id.iv_right) != null){
                findViewById(R.id.iv_right).setVisibility(View.GONE);
            }
        }

    }

    protected synchronized void buildGoogleApiClient(){
        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }

    private void getDeviceLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest,  this
            );
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getDeviceLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                updateLocationUI();
                return;
        }
    }


    private static final String CAMERA_POSITION = "camera_position";
    private static final String LOCATION = "location";

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(mMap != null) {
            outState.putParcelable(CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(LOCATION, mCurrentLocation);
            super.onSaveInstanceState(outState);
        }
    }

    private void updateLocationUI() {
        if (mMap == null) return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        getDeviceLocation();

        /*
        if(mCameraPosition != null){
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if(mCurrentLocation != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude()), 16));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.3, 34.3), 16));
        }*/

        Location location = Util.findGeoPoint(getApplicationContext(), "서울시 용산구 이촌동 동작대교");
        if(mMap != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11));

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();
    }

    @Override
    public void onMashupSuccess(JSONObject object, int requestCode) {

    }

    @Override
    public void onMashupFail(VolleyError error, int requestCode) {

    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}
