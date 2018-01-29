package com.gangnam4bungate.nuviseoul.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.CODES;
import com.gangnam4bungate.nuviseoul.data.PlanData;
import com.gangnam4bungate.nuviseoul.database.DBOpenHelper;
import com.gangnam4bungate.nuviseoul.database.DataBases;
import com.gangnam4bungate.nuviseoul.holder.PlanListAdapter;
import com.gangnam4bungate.nuviseoul.ui.activity.MainActivity;
import com.gangnam4bungate.nuviseoul.ui.activity.PlanEditActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wsseo on 2017. 10. 25..
 */

public class PlanFragment extends Fragment {
    private RelativeLayout mRL_Empty;
    private TextView mTv_Empty;
    private ImageView mIv_add;
    private RecyclerView mRvPlanList;
    private PlanListAdapter mPlanListAdapter;
    DBOpenHelper mDBOpenHelper;

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        mDBOpenHelper = DBOpenHelper.getInstance();
        if(mDBOpenHelper != null && mDBOpenHelper.isOpen() == false)
            mDBOpenHelper.open(inflater.getContext());

        mRL_Empty = (RelativeLayout) view.findViewById(R.id.rl_empty);
        mTv_Empty = (TextView) view.findViewById(R.id.tv_empty);
        mIv_add = (ImageView) view.findViewById(R.id.iv_add);
        if(mIv_add != null){
            mIv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(inflater.getContext(), PlanEditActivity.class), CODES.ActivityResult.PLAN_EDIT);
                }
            });
        }

        Cursor cursor = mDBOpenHelper.planAllColumns();
        ArrayList<PlanData> planList = new ArrayList<PlanData>();
        if(cursor != null){
            try {
                if(cursor.getCount() > 0){
                    mRvPlanList.setVisibility(View.VISIBLE);
                    mRL_Empty.setVisibility(View.GONE);
                    mTv_Empty.setVisibility(View.GONE);
                } else {
                    mRvPlanList.setVisibility(View.GONE);
                    mRL_Empty.setVisibility(View.VISIBLE);
                    mTv_Empty.setVisibility(View.VISIBLE);
                }

                do {
                    PlanData data = new PlanData();

                    data.setId(cursor.getInt(cursor.getColumnIndex(DataBases.CreatePlanDB._ID)));

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date start_date = sdf.parse(cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDB._STARTDATE)));
                    Date end_date = sdf.parse(cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDB._ENDDATE)));

                    data.setStart_date(start_date);
                    data.setEnd_date(end_date);
                    data.setName(cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDB._NAME)));

                    Cursor c = mDBOpenHelper.plandetail_getColumn(data.getId());
                    if(c != null){
                        data.setPlacenum(c.getCount());
                        c.close();
                    }

                    planList.add(data);
                } while (cursor.moveToNext());

                cursor.close();

            }catch(Exception e){

            }
        }

        mRvPlanList = (RecyclerView) view.findViewById(R.id.rv_planlist);
        mRvPlanList.setVisibility(View.VISIBLE);

        mPlanListAdapter = new PlanListAdapter(inflater.getContext());
        mRvPlanList.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        mRvPlanList.setAdapter(mPlanListAdapter);

        return view;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();

        Cursor cursor = mDBOpenHelper.planAllColumns();
        ArrayList<PlanData> planList = new ArrayList<PlanData>();
        if(cursor != null){
            try {

                if(cursor.getCount() > 0){
                    mRvPlanList.setVisibility(View.VISIBLE);
                    mRL_Empty.setVisibility(View.GONE);
                    mTv_Empty.setVisibility(View.GONE);
                } else {
                    mRvPlanList.setVisibility(View.GONE);
                    mRL_Empty.setVisibility(View.VISIBLE);
                    mTv_Empty.setVisibility(View.VISIBLE);
                }

                do {
                    PlanData data = new PlanData();

                    data.setId(cursor.getInt(cursor.getColumnIndex(DataBases.CreatePlanDB._ID)));

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date start_date = sdf.parse(cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDB._STARTDATE)));
                    Date end_date = sdf.parse(cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDB._ENDDATE)));

                    data.setStart_date(start_date);
                    data.setEnd_date(end_date);
                    data.setName(cursor.getString(cursor.getColumnIndex(DataBases.CreatePlanDB._NAME)));
                    Cursor c = mDBOpenHelper.plandetail_getColumn(data.getId());
                    if(c != null){
                        data.setPlacenum(c.getCount());
                        c.close();
                    }
                    planList.add(data);
                } while (cursor.moveToNext());

                cursor.close();

            }catch(Exception e){

            }
        }

        if(mPlanListAdapter != null)
            mPlanListAdapter.bindData(planList);

    }
}
