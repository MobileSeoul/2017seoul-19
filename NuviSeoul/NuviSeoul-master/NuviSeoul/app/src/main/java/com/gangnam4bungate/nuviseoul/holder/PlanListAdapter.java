package com.gangnam4bungate.nuviseoul.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.CODES;
import com.gangnam4bungate.nuviseoul.data.PlanData;
import com.gangnam4bungate.nuviseoul.data.PlanDetailData;
import com.gangnam4bungate.nuviseoul.ui.activity.PlanDetailActivity;
import com.gangnam4bungate.nuviseoul.ui.activity.PlanEditActivity;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wsseo on 2017. 8. 6..
 */

public class PlanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<PlanData> mPlanList = new ArrayList<PlanData>();
    private Context mContext;

    public PlanListAdapter(Context context){
        mContext = context;
    }

    public void bindData(ArrayList<PlanData> list){
        mPlanList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return Holder.getHolder(parent.getContext(), viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == Holder.TYPE_PLAN_LIST){
            Holder.PlanListViewHolder planListViewHolder = (Holder.PlanListViewHolder)holder;

            try {
                Date sDate = mPlanList.get(position).getStart_date();
                Date eDate = mPlanList.get(position).getEnd_date();

                SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd");
                String start_date = sdf.format(sDate);
                String end_date = sdf.format(eDate);

                if (start_date != null && end_date != null) {
                    if (start_date.equals(end_date)) {
                        planListViewHolder.setDate(start_date + " ");
                    } else {
                        planListViewHolder.setDate(start_date + " - " + end_date);
                    }
                }

                planListViewHolder.setName(mPlanList.get(position).getName());
                planListViewHolder.setPlaceNum(mPlanList.get(position).getPlacenum() + mContext.getString(R.string.plan_list_place_msg));
                planListViewHolder.itemView.setTag(mPlanList.get(position));
                planListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlanData data = (PlanData) v.getTag();
                        if (data != null) {
                            Intent intent = new Intent(mContext, PlanDetailActivity.class);
                            intent.putExtra("id", data.getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }catch(Exception e){

            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mPlanList.size();
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        return Holder.TYPE_PLAN_LIST;
    }
}
