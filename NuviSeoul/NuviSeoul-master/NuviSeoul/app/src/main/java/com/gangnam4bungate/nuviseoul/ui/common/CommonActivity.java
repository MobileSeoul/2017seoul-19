package com.gangnam4bungate.nuviseoul.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by wsseo on 2017. 6. 28..
 */

public class CommonActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateView(savedInstanceState);
    }

    public void onCreateView(Bundle saveInstanceState){

    }
}
