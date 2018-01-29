package com.gangnam4bungate.nuviseoul.ui.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wsseo on 2017. 10. 28..
 */

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalSpaceHeight;

    public HorizontalSpaceItemDecoration(int verticalSpaceHeight) {
        this.horizontalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.right = horizontalSpaceHeight;
    }
}
