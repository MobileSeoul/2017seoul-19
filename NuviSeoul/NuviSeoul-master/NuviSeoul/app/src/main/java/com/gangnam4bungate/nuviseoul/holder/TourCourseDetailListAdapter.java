package com.gangnam4bungate.nuviseoul.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.model.detail.TourCourseDetailInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsseo on 2017. 8. 6..
 */

public class TourCourseDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<TourCourseDetailInfo> mTourCourseDetailList= new ArrayList<TourCourseDetailInfo>();
    private Context mContext;

    public TourCourseDetailListAdapter(Context context){
        mContext = context;
    }

    public void bindData(List<TourCourseDetailInfo> list){
        mTourCourseDetailList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return Holder.getHolder(parent.getContext(), viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == Holder.TYPE_TOUR_COURSE_DETAIL_LIST){
            Holder.TourCourseDetailListHolder tourCourseDetailListHolder = (Holder.TourCourseDetailListHolder)holder;
            try {
                tourCourseDetailListHolder.setTitle(mTourCourseDetailList.get(position).getSubname());
                tourCourseDetailListHolder.setMsg(mTourCourseDetailList.get(position).getSubdetailoverview());
                tourCourseDetailListHolder.setBackground(mContext, mTourCourseDetailList.get(position).getSubdetailimg());
                tourCourseDetailListHolder.setCourseNumber(Integer.toString(position + 1) + mContext.getString(R.string.course));
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
        return mTourCourseDetailList.size();
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
        return Holder.TYPE_TOUR_COURSE_DETAIL_LIST;
    }
}
