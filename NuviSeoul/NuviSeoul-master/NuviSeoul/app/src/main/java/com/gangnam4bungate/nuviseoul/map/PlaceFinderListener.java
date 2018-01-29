package com.gangnam4bungate.nuviseoul.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by choi on 2017-08-16.
 */


public interface PlaceFinderListener {
    void onPlaceFinderSuccess(LatLng latLng,String pTitle);
}