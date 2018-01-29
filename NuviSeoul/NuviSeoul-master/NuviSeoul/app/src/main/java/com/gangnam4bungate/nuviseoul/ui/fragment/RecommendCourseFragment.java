package com.gangnam4bungate.nuviseoul.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.holder.PlanDetailAdapter;
import com.gangnam4bungate.nuviseoul.holder.PlanListAdapter;
import com.gangnam4bungate.nuviseoul.holder.TourCourseListAdapter;
import com.gangnam4bungate.nuviseoul.model.TourCourseModel;
import com.gangnam4bungate.nuviseoul.network.DataManager;
import com.gangnam4bungate.nuviseoul.ui.view.VerticalSpaceItemDecoration;

/**
 * Created by wsseo on 2017. 10. 25..
 */

public class RecommendCourseFragment extends Fragment {

    private RecyclerView mRvTourCourse;
    private TourCourseListAdapter mTourCourseAdapter;
    private LinearLayoutManager mLinearLayoutManager;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        mRvTourCourse = (RecyclerView) view.findViewById(R.id.rv_courselist);
        mRvTourCourse.setVisibility(View.VISIBLE);

        mTourCourseAdapter = new TourCourseListAdapter(inflater.getContext());
        mRvTourCourse.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        mRvTourCourse.setAdapter(mTourCourseAdapter);

        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(30);
        mRvTourCourse.addItemDecoration(verticalSpaceItemDecoration);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            if (DataManager.getInstance() != null) {
                TourCourseModel model = DataManager.getInstance().getTourCourseModel();

                if (model != null) {
                    mTourCourseAdapter.bindData(model.getResponse().getBody().getItems().getItemList());
                }
            }
        }catch(Exception e){

        }
    }
}
