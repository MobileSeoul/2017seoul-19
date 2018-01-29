package com.gangnam4bungate.nuviseoul.ui.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.CODES;
import com.gangnam4bungate.nuviseoul.data.PlanData;
import com.gangnam4bungate.nuviseoul.data.PlanDetailData;
import com.gangnam4bungate.nuviseoul.database.DBOpenHelper;
import com.gangnam4bungate.nuviseoul.database.DataBases;
import com.gangnam4bungate.nuviseoul.map.Route;
import com.gangnam4bungate.nuviseoul.ui.common.CommonActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PlanEditActivity extends CommonActivity {
    Button mSaveButton;
    LinearLayout mllAddLayout;
    LinearLayout mllAddDetailPlan;
    RelativeLayout mrl_back;

    DBOpenHelper mDBOpenHelper;
    EditText mPlanSubjectText;
    int year, month, date;
    PlanData mPlanData = new PlanData();
    ArrayList<PlanDetailData> mPlanDetailInsertList = new ArrayList<PlanDetailData>();
    ArrayList<PlanDetailData> mPlanDetailUpdateList = new ArrayList<PlanDetailData>();
    ArrayList<PlanDetailData> mPlanDetailDeleteList = new ArrayList<PlanDetailData>();

    ArrayList<Route> mLocationList = new ArrayList<Route>();
    boolean isPlanEdit = false;
    boolean isAddCourse = false;
    boolean isSearchAddCourse = false;
    int mEditPlanId = 0;

    public static PlanEditActivity mPlanEditActivity = null;
    public static PlanEditActivity getInstance(){
        return mPlanEditActivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_edit);
        mPlanEditActivity = this;

        Intent intent = getIntent();
        if(intent != null && intent.getBooleanExtra("edit", false)){
            isPlanEdit = true;
            mEditPlanId = intent.getIntExtra("planid", 0);
        } else if(intent != null && intent.getBooleanExtra("add_course", false)){
            isAddCourse = true;
        } else if(intent != null && intent.getBooleanExtra("search_add_course", false)){
            isSearchAddCourse = true;

        }
        mDBOpenHelper = DBOpenHelper.getInstance();
        if(mDBOpenHelper != null && mDBOpenHelper.isOpen() == false)
            mDBOpenHelper.open(getApplicationContext());
        initLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlanEditActivity = null;
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

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPlanSubjectText.getWindowToken(), 0);

    }

    public void setLocations(ArrayList<Route> locationList){
        mLocationList.clear();
        mLocationList.addAll(locationList);
    }

    public void initLayout(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        TextView tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        if(tv_title != null){
            if(isPlanEdit == false)
                tv_title.setText(getString(R.string.plan_make_title));
            else
                tv_title.setText(getString(R.string.edit_title));
        }
        mrl_back = (RelativeLayout) toolbar.findViewById(R.id.rl_back);
        if(mrl_back != null){
            mrl_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        setSupportActionBar(toolbar);

        Calendar cal = Calendar.getInstance();
        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH) + 1;
        date = cal.get(cal.DATE);

        mPlanSubjectText = (EditText) findViewById(R.id.et_subject);
        mllAddLayout = (LinearLayout) findViewById(R.id.ll_addLayout);

        if(isPlanEdit == false) {
            if (mllAddLayout != null) {
                addLayout(mllAddLayout, null);
            }
            View addLayout = getLayoutInflater().inflate(R.layout.layout_plan_adddetailplan, null);
            if (addLayout != null) {
                mllAddLayout.addView(addLayout);
                addLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addLayout(mllAddLayout, null);
                    }
                });
            }

            if(isAddCourse) {
                addCourseLayout(mllAddLayout);
            }

            if(isSearchAddCourse) {
                addSearchCourseLayout(mllAddLayout);
            }
        } else {
            Cursor c = mDBOpenHelper.plan_getColumn(mEditPlanId);
            if(c != null){
                if(c.getCount() > 0) {
                    try {
                        String name = c.getString(c.getColumnIndex(DataBases.CreatePlanDB._NAME));
                        if (name != null)
                            mPlanSubjectText.setText(name);
                    }catch(Exception e){

                    }
                }
                c.close();
            }

            Cursor cursor = mDBOpenHelper.plandetail_getColumn(mEditPlanId);
            if(cursor != null){
                do {
                    try {
                        PlanDetailData data = new PlanDetailData();

                        data.setId(cursor.getInt(cursor.getColumnIndex(DataBases.CreatePlanDetailDB._ID)));
                        data.setPlanid(mEditPlanId);
                        String sdate = cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDetailDB._STARTDATE));
                        String edate = cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDetailDB._ENDDATE));

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date start_date = sdf.parse(sdate);
                        Date end_date = sdf.parse(edate);

                        data.setStartDate(start_date);
                        data.setEndDate(end_date);

                        data.setPathseq(cursor.getInt(cursor.getColumnIndex(DataBases.CreatePlanDetailDB._PATH_SEQ)));
                        data.setPlacename(cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_NAME)));
                        data.setLatitude(cursor.getDouble(cursor.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_GPS_LATITUDE)));
                        data.setLongitude(cursor.getDouble(cursor.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_GPS_LONGITUDE)));
                        addLayout(mllAddLayout, data);
                    } catch (Exception e) {

                    }
                }while (cursor.moveToNext());
                cursor.close();
            }

            View addLayout = getLayoutInflater().inflate(R.layout.layout_plan_adddetailplan, null);
            if (addLayout != null) {
                mllAddLayout.addView(addLayout);
                addLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addLayout(mllAddLayout, null);
                    }
                });
            }
        }

        mllAddDetailPlan = (LinearLayout) findViewById(R.id.ll_adddetailplan);
        if(mllAddDetailPlan != null){
            mllAddDetailPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addLayout(mllAddLayout, null);
                }
            });
        }
        mSaveButton = (Button) findViewById(R.id.registerButton);
        if(mSaveButton != null){
            if(isPlanEdit == false){
                mSaveButton.setText(getString(R.string.save_title));
            } else {
                mSaveButton.setText(getString(R.string.edit_btn));
            }
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String subject = mPlanSubjectText.getText().toString();
                    if(subject == null || (subject != null && subject.equals(""))){
                        Toast.makeText(getApplicationContext(), getString(R.string.subject_error_toast_msg), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(isPlanEdit == false) {
                        mPlanData.setName(subject);
                        Date sDate = null;
                        Date eDate = null;

                        for (int i = 0; i < mPlanDetailInsertList.size(); i++) {
                            PlanDetailData data = mPlanDetailInsertList.get(i);
                            if (data != null) {
                                if(sDate == null) {
                                    sDate = data.getStartDate();
                                } else {
                                    if(data.getStartDate() == null)
                                        data.setStartDate(sDate);
                                    if(sDate.compareTo(data.getStartDate()) > 0){
                                        sDate = data.getStartDate();
                                    }
                                }
                                if(eDate == null) {
                                    eDate = data.getEndDate();
                                } else {
                                    if(data.getEndDate() == null)
                                        data.setEndDate(eDate);
                                    if(eDate.compareTo(data.getEndDate()) < 0){
                                        eDate = data.getEndDate();
                                    }
                                }
                            }
                        }


                        if(sDate == null){
                            Toast.makeText(getApplicationContext(), getString(R.string.sdate_error_toast_msg), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(eDate == null){
                            Toast.makeText(getApplicationContext(), getString(R.string.edate_error_toast_msg), Toast.LENGTH_LONG).show();
                            return;
                        }
                        boolean isPlaced = false;
                        for (int i = 0; i < mPlanDetailInsertList.size(); i++) {
                            PlanDetailData data = mPlanDetailInsertList.get(i);
                            if (data != null && data.getPlacename() != null &&
                                    data.getPlacename().equals("") == false &&
                                    data.getLatitude() > 0 &&
                                    data.getLongitude() > 0) {

                                isPlaced = true;
                                break;
                            }
                        }

                        if(isPlaced == false){
                            Toast.makeText(getApplicationContext(), getString(R.string.place_error_toast_msg), Toast.LENGTH_LONG).show();
                            return;
                        }


                        if(sDate != null)
                            mPlanData.setStart_date(sDate);
                        if(eDate != null)
                            mPlanData.setEnd_date(eDate);

                        mPlanData.setDetailDataList(mPlanDetailInsertList);

                        mDBOpenHelper.planInsert(mPlanData);
                        Cursor c = mDBOpenHelper.plan_getMatchName(mPlanData.getName());
                        int planid = 0;
                        if (c != null) {
                            if(c.moveToFirst())
                                planid = c.getInt(c.getColumnIndex(DataBases.CreatePlanDB._ID));
                            c.close();
                        }
                        for (int i = 0; i < mPlanDetailInsertList.size(); i++) {
                            PlanDetailData data = mPlanDetailInsertList.get(i);
                            if (data != null) {
                                data.setPlanid(planid);
                                mDBOpenHelper.plandetailInsert(data);
                            }
                        }
                    } else {
                        mPlanData.setName(subject);
                        Date sDate = null;
                        Date eDate = null;

                        for (int i = 0; i < mPlanDetailUpdateList.size(); i++) {
                            PlanDetailData data = mPlanDetailUpdateList.get(i);
                            if (data != null) {
                                if(sDate == null) {
                                    sDate = data.getStartDate();
                                } else {
                                    if(data.getStartDate() == null)
                                        data.setStartDate(sDate);
                                    if(sDate.compareTo(data.getStartDate()) > 0){
                                        sDate = data.getStartDate();
                                    }
                                }
                                if(eDate == null) {
                                    eDate = data.getEndDate();
                                } else {
                                    if(data.getEndDate() == null)
                                        data.setEndDate(eDate);
                                    if(eDate.compareTo(data.getEndDate()) < 0){
                                        eDate = data.getEndDate();
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < mPlanDetailInsertList.size(); i++) {
                            PlanDetailData data = mPlanDetailInsertList.get(i);
                            if (data != null) {
                                if(sDate == null) {
                                    sDate = data.getStartDate();
                                } else {
                                    if(data.getStartDate() == null)
                                        data.setStartDate(sDate);
                                    if(sDate.compareTo(data.getStartDate()) > 0){
                                        sDate = data.getStartDate();
                                    }
                                }
                                if(eDate == null) {
                                    eDate = data.getEndDate();
                                } else {
                                    if(data.getEndDate() == null)
                                        data.setEndDate(eDate);
                                    if(eDate.compareTo(data.getEndDate()) < 0){
                                        eDate = data.getEndDate();
                                    }
                                }
                            }
                        }

                        if(sDate == null){
                            Toast.makeText(getApplicationContext(), getString(R.string.sdate_error_toast_msg), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(eDate == null){
                            Toast.makeText(getApplicationContext(), getString(R.string.edate_error_toast_msg), Toast.LENGTH_LONG).show();
                            return;
                        }

                        boolean isPlaced = false;
                        for (int i = 0; i < mPlanDetailInsertList.size(); i++) {
                            PlanDetailData data = mPlanDetailInsertList.get(i);
                            if (data != null && data.getPlacename() != null &&
                                    data.getPlacename().equals("") == false &&
                                    data.getLatitude() > 0 &&
                                    data.getLongitude() > 0) {

                                isPlaced = true;
                                break;
                            }
                        }
                        for (int i = 0; i < mPlanDetailUpdateList.size(); i++) {
                            PlanDetailData data = mPlanDetailUpdateList.get(i);
                            if (data != null && data.getPlacename() != null &&
                                    data.getPlacename().equals("") == false &&
                                    data.getLatitude() > 0 &&
                                    data.getLongitude() > 0) {

                                isPlaced = true;
                                break;
                            }
                        }
                        if(isPlaced == false){
                            Toast.makeText(getApplicationContext(), getString(R.string.place_error_toast_msg), Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(sDate != null)
                            mPlanData.setStart_date(sDate);
                        if(eDate != null)
                            mPlanData.setEnd_date(eDate);

                        ArrayList<PlanDetailData> all_list = new ArrayList<PlanDetailData>();
                        if(mPlanDetailInsertList.size() > 0)
                             all_list.addAll(mPlanDetailInsertList);
                        if(mPlanDetailUpdateList.size() > 0)
                             all_list.addAll(mPlanDetailUpdateList);
                        mPlanData.setDetailDataList(all_list);
                        mPlanData.setId(mEditPlanId);

                        mDBOpenHelper.planUpdate(mPlanData);

                        for (int i = 0; i < mPlanDetailInsertList.size(); i++) {
                            PlanDetailData data = mPlanDetailInsertList.get(i);
                            if (data != null) {
                                data.setPlanid(mEditPlanId);
                                mDBOpenHelper.plandetailInsert(data);
                            }
                        }

                        for (int i = 0; i < mPlanDetailUpdateList.size(); i++) {
                            PlanDetailData data = mPlanDetailUpdateList.get(i);
                            if (data != null) {
                                data.setPlanid(mEditPlanId);
                                mDBOpenHelper.plandetailUpdate(data);
                            }
                        }

                        for (int i = 0; i < mPlanDetailDeleteList.size(); i++) {
                            PlanDetailData data = mPlanDetailDeleteList.get(i);
                            if (data != null) {
                                mDBOpenHelper.plandetailDelete(data.getId());
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.plan_add_finish_msg), Toast.LENGTH_LONG).show();
                    hideKeyboard();
                    finish();

                }
            });
        }
    }

    private void hideKeyboard(){
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){

        }

    }


    public void addLayout(final LinearLayout addView, PlanDetailData editdata){
        try {
            LinearLayout ll_adddetailplan = (LinearLayout) addView.findViewById(R.id.ll_adddetailplan);
            if (ll_adddetailplan != null) {
                addView.removeView(ll_adddetailplan);
            }

            final PlanDetailData data;
            if (editdata == null) {
                data = new PlanDetailData();
                mPlanDetailInsertList.add(data);
            } else {
                data = editdata;
                mPlanDetailUpdateList.add(data);
            }

            if(data != null) {
                if(data.getPathseq() == 0 || data.getPathseq() == -1){
                    final LinearLayout ll_detail = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_plan_edit_detail, null);
                    if (ll_detail != null) {
                        addView.addView(ll_detail);
                    }
                    if(ll_adddetailplan != null)
                        addView.addView(ll_adddetailplan);

                    TextView tvStartDate = (TextView) ll_detail.findViewById(R.id.tv_startDate);
                    if (tvStartDate != null) {
                        tvStartDate.setTag(data);
                        if (editdata != null) {
                            Date sdate = editdata.getStartDate();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(sdate);
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH) + 1;
                            int day = cal.get(Calendar.DAY_OF_MONTH);

                            tvStartDate.setText(String.format("%d/%d/%d", year, month, day));
                        }
                        tvStartDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                final DatePickerDialog dateDialog = new DatePickerDialog(PlanEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        try {
                                            TextView textView = (TextView) v;
                                            if (textView != null) {
                                                textView.setText(String.format("%d/%d/%d", year, month + 1, dayOfMonth));
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                                                Date date = dateFormat.parse(String.format("%d-%d-%d", year, month + 1, dayOfMonth));
                                                PlanDetailData data = (PlanDetailData) textView.getTag();
                                                data.setStartDate(date);
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                }, year, month - 1, date);
                                dateDialog.show();
                            }
                        });
                    }

                    TextView tvEndDate = (TextView) ll_detail.findViewById(R.id.tv_endDate);
                    if (tvEndDate != null) {
                        tvEndDate.setTag(data);
                        if (editdata != null) {
                            Date date = editdata.getEndDate();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH) + 1;
                            int day = cal.get(Calendar.DAY_OF_MONTH);

                            tvEndDate.setText(String.format("%d/%d/%d", year, month, day));
                        }
                        tvEndDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                DatePickerDialog dateDialog = new DatePickerDialog(PlanEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        try {
                                            TextView textView = (TextView) v;
                                            if (textView != null) {
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                                                Date edate = dateFormat.parse(String.format("%d-%d-%d", year, month + 1, dayOfMonth));
                                                PlanDetailData data = (PlanDetailData) textView.getTag();
                                                if(edate != null && edate.compareTo(data.getStartDate()) < 0){
                                                    Toast.makeText(getApplicationContext(), getString(R.string.date_error_toast_msg), Toast.LENGTH_LONG).show();
                                                } else {
                                                    textView.setText(String.format("%d/%d/%d", year, month + 1, dayOfMonth));
                                                    data.setEndDate(edate);
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                }, year, month - 1, date);
                                dateDialog.show();
                            }
                        });
                    }

                    final TextView tvAddPlace = (TextView) ll_detail.findViewById(R.id.tv_addplace);
                    if (tvAddPlace != null) {
                        tvAddPlace.setTag(data);
                        if (editdata != null) {
                            tvAddPlace.setText(editdata.getPlacename());
                        }
                        tvAddPlace.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (v != null) {
                                        TextView textView = (TextView) v;
                                        if (textView != null) {

                                            if(textView.getText() != null && textView.getText().toString().equals("") == false){
                                                Toast.makeText(getApplicationContext(), getString(R.string.place_add_fail_toast), Toast.LENGTH_LONG).show();
                                            } else if(textView.getText() == null || (textView.getText() != null &&
                                                                                        textView.getText().toString().equals(""))){
                                                if (ll_detail != null && textView != null && textView.getTag() != null)
                                                    ll_detail.setTag((PlanDetailData) textView.getTag());
                                                startActivityForResult(new Intent(getApplicationContext(), RecommendActivity.class),
                                                        CODES.ActivityResult.LOCATIONS);
                                            }
                                        }
                                    }
                                }catch(Exception e){

                                }
                            }
                        });
                    }

                    Button btClose = (Button) ll_detail.findViewById(R.id.bt_close);
                    if (btClose != null)
                        btClose.setTag(data);
                    btClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                PlanDetailData data = (PlanDetailData) v.getTag();
                                if(data != null){
                                    for (int index = 0; index < mllAddLayout.getChildCount(); index++) {
                                        View childview = mllAddLayout.getChildAt(index);
                                        if (childview != null) {
                                            TextView tv_dest = (TextView) childview.findViewById(R.id.tv_addplace);
                                            if (tv_dest != null) {
                                                PlanDetailData data_diff = (PlanDetailData) tv_dest.getTag();
                                                if (data != null && data.getId() == data_diff.getId()
                                                        && data.getPlacename().equals(data_diff.getPlacename())) {
                                                    mllAddLayout.removeView(childview);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (data != null) {
                                    mPlanDetailDeleteList.add(data);
                                    for (int index = 0; index < mPlanDetailInsertList.size(); index++) {
                                        PlanDetailData idata = mPlanDetailInsertList.get(index);
                                        if (idata != null && idata.getPlacename().equals(data.getPlacename())
                                                && idata.getStartDate().equals(data.getStartDate())
                                                && idata.getEndDate().equals(data.getEndDate())) {

                                            mPlanDetailInsertList.remove(data);
                                            break;
                                        }
                                    }

                                    for (int index = 0; index < mPlanDetailUpdateList.size(); index++) {
                                        PlanDetailData idata = mPlanDetailUpdateList.get(index);
                                        if (idata != null && idata.getPlacename().equals(data.getPlacename())
                                                && idata.getStartDate().equals(data.getStartDate())
                                                && idata.getEndDate().equals(data.getEndDate())) {

                                            mPlanDetailUpdateList.remove(data);
                                            break;
                                        }
                                    }
                                }
                            }catch(Exception e){

                            }
                        }
                    });
                } else if(data.getPathseq() >= 1){
                    updateLocationLayout(addView, data);
                }


            }
        }catch(Exception e){

        }
    }


    public void updateLocationLayout(LinearLayout parentView, PlanDetailData data){
        TextView tv_dest;
        View view = getLayoutInflater().inflate(R.layout.layout_plan_destination, null);
        if (view != null) {
            tv_dest = (TextView) view.findViewById(R.id.tv_addplace);
            if(tv_dest != null) {
                tv_dest.setText(data.getPlacename());
                tv_dest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (v != null) {
                                Toast.makeText(getApplicationContext(), getString(R.string.place_add_fail_toast), Toast.LENGTH_LONG).show();
                            }
                        }catch(Exception e){

                        }
                    }
                });
            }
        }
        parentView.addView(view);
    }


    public void addLocationLayout(LinearLayout parentView){
        try {
            boolean isEdit = false;
            LinearLayout addView = null;
            PlanDetailData data = null;
            for (int index = 0; index < parentView.getChildCount(); index++) {
                View childview = parentView.getChildAt(index);
                if (childview != null && childview.getTag() != null) {
                    data = (PlanDetailData)childview.getTag();
                    if(data != null){
                        addView = (LinearLayout)childview;
                        break;
                    }
                }
            }

            if(mLocationList.size() > 0) {
                TextView tv_dest = (TextView) addView.findViewById(R.id.tv_addplace);
                if (tv_dest != null) {
                    Route route = mLocationList.get(0);
                    tv_dest.setText(route.startTitle);
                    data.setLatitude(route.startLocation.latitude);
                    data.setLongitude(route.startLocation.longitude);
                    data.setPlacename(tv_dest.getText().toString());
                    data.setPathseq(0);

                    for (int index = 0; index < mPlanDetailUpdateList.size(); index++) {
                        PlanDetailData udata = mPlanDetailUpdateList.get(index);
                        if (udata != null && udata.getId() == data.getId()
                                && udata.getPlacename().equals(data.getPlacename())
                                && udata.getStartDate().equals(data.getStartDate())
                                && udata.getEndDate().equals(data.getEndDate())) {

                            isEdit = true;
                            break;
                        }
                    }
                }
                if (addView != null) {
                    for (int index = 1; index < mLocationList.size(); index++) {
                        Route route = mLocationList.get(index);
                        View view = getLayoutInflater().inflate(R.layout.layout_plan_destination, null);
                        if (view != null) {
                            tv_dest = (TextView) view.findViewById(R.id.tv_addplace);
                        }
                        if (tv_dest != null) {

                            PlanDetailData copy = new PlanDetailData();
                            copy.setId(data.getId());
                            copy.setPathseq(index);
                            copy.setEndDate(data.getEndDate());
                            copy.setStartDate(data.getStartDate());
                            copy.setPlanid(data.getPlanid());

                            tv_dest.setText(route.endTitle);
                            copy.setLatitude(route.endLocation.latitude);
                            copy.setLongitude(route.endLocation.longitude);
                            copy.setPlacename(tv_dest.getText().toString());

                            if(isEdit == false){
                                mPlanDetailInsertList.add(copy);
                            } else {
                                mPlanDetailUpdateList.add(copy);
                            }
                            tv_dest.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        if (v != null) {
                                            Toast.makeText(getApplicationContext(), getString(R.string.place_add_fail_toast), Toast.LENGTH_LONG).show();
                                        }
                                    }catch(Exception e){

                                    }
                                }
                            });
                            addView.addView(view);
                        }
                    }

                    addView.setTag(null);
                }
            }
        }catch (Exception e){

        }

    }


    public void addCourseLayout(LinearLayout parentView){
        try {
            boolean isEdit = false;
            LinearLayout addView = (LinearLayout) parentView.findViewById(R.id.ll_plan_deail);
            PlanDetailData data = null;

            List<PlanDetailData> list = RecommendCourseDetailActivity.getInstance().getPlanDetailDataList();
            if(list.size() > 0) {
                TextView tv_dest = (TextView) addView.findViewById(R.id.tv_addplace);
                if (tv_dest != null) {
                    data = (PlanDetailData) tv_dest.getTag();
                    if(data == null)
                        return;
                    PlanDetailData info = list.get(0);
                    tv_dest.setText(info.getPlacename());
                    data.setLatitude(info.getLatitude());
                    data.setLongitude(info.getLongitude());
                    data.setPlacename(info.getPlacename());
                    data.setPathseq(0);

                    for (int index = 0; index < mPlanDetailUpdateList.size(); index++) {
                        PlanDetailData udata = mPlanDetailUpdateList.get(index);
                        if (udata != null && udata.getId() == data.getId()
                                && udata.getPlacename().equals(data.getPlacename())
                                && udata.getStartDate().equals(data.getStartDate())
                                && udata.getEndDate().equals(data.getEndDate())) {

                            isEdit = true;
                            break;
                        }
                    }
                }
                if (addView != null) {
                    for (int index = 1; index < list.size(); index++) {
                        PlanDetailData info = list.get(index);
                        View view = getLayoutInflater().inflate(R.layout.layout_plan_destination, null);
                        if (view != null) {
                            tv_dest = (TextView) view.findViewById(R.id.tv_addplace);
                        }
                        if (tv_dest != null) {

                            PlanDetailData copy = new PlanDetailData();
                            copy.setId(data.getId());
                            copy.setPathseq(index);
                            copy.setEndDate(data.getEndDate());
                            copy.setStartDate(data.getStartDate());
                            copy.setPlanid(data.getPlanid());
                            tv_dest.setText(info.getPlacename());
                            copy.setLatitude(info.getLatitude());
                            copy.setLongitude(info.getLongitude());
                            copy.setPlacename(tv_dest.getText().toString());

                            if(isEdit == false){
                                mPlanDetailInsertList.add(copy);
                            } else {
                                mPlanDetailUpdateList.add(copy);
                            }
                            addView.addView(view);
                        }
                    }

                    addView.setTag(null);
                }
            }
        }catch (Exception e){

        }

    }


    public void addSearchCourseLayout(LinearLayout parentView){
        try {
            PlanDetailData data = null;
            if(parentView != null){
                LinearLayout linearLayout = (LinearLayout) parentView.findViewById(R.id.ll_plan_deail);
                if(linearLayout != null){
                    Intent intent = getIntent();
                    if(intent != null) {
                        TextView tv_dest = (TextView) linearLayout.findViewById(R.id.tv_addplace);
                        if (tv_dest != null) {
                            tv_dest.setText(intent.getStringExtra("placename"));
                            data = (PlanDetailData)tv_dest.getTag();
                            if(data != null) {
                                String mapx = intent.getStringExtra("mapx");
                                String mapy = intent.getStringExtra("mapy");
                                data.setLatitude(Double.valueOf(mapy));
                                data.setLongitude(Double.valueOf(mapx));
                                data.setPlacename(intent.getStringExtra("placename"));
                                data.setPathseq(0);
                            }
                        }
                    }

                }
            }

        }catch (Exception e){

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
            case CODES.ActivityResult.LOCATIONS:
            {
                addLocationLayout(mllAddLayout);
            }
            break;
        }
    }
}
