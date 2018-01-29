package com.gangnam4bungate.nuviseoul.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gangnam4bungate.nuviseoul.data.PlanData;
import com.gangnam4bungate.nuviseoul.model.TourCourseInfo;
import com.gangnam4bungate.nuviseoul.ui.activity.PlanDetailActivity;
import com.gangnam4bungate.nuviseoul.ui.activity.RecommendCourseDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wsseo on 2017. 8. 6..
 */

public class TourCourseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<TourCourseInfo> mTourCourseList = new ArrayList<TourCourseInfo>();
    private Context mContext;

    public TourCourseListAdapter(Context context){
        mContext = context;
    }

    public void bindData(List<TourCourseInfo> list){
        mTourCourseList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return Holder.getHolder(parent.getContext(), viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == Holder.TYPE_TOUR_COURSE_LIST){
            Holder.TourCourseListHolder tourCourseListHolder = (Holder.TourCourseListHolder)holder;

            try {
                tourCourseListHolder.setName(mTourCourseList.get(position).getTitle());
                tourCourseListHolder.setBackground(mContext, mTourCourseList.get(position).getFirstimage());


                tourCourseListHolder.itemView.setTag(mTourCourseList.get(position));
                tourCourseListHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TourCourseInfo info = (TourCourseInfo)v.getTag();
                        if(info != null){
                            Intent intent = new Intent(mContext, RecommendCourseDetailActivity.class);
                            intent.putExtra("contentid", info.getContentid());
                            intent.putExtra("title", info.getTitle());
                            intent.putExtra("image1", info.getFirstimage());
                            intent.putExtra("image2", info.getFirstimage2());
                            intent.putExtra("mapx", info.getMapx());
                            intent.putExtra("mapy", info.getMapy());
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
        return mTourCourseList.size();
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
        return Holder.TYPE_TOUR_COURSE_LIST;
    }
}
