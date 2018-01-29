package com.gangnam4bungate.nuviseoul.map;

/**
 * Created by choi on 2017-08-16.
 */

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {
    public String   index;
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String endTitle;
    public String startAddress;
    public LatLng startLocation;
    public String startTitle;
    public List<LatLng> points;
}