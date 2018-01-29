package com.gangnam4bungate.nuviseoul.ui.common;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.data.PlanData;
import com.gangnam4bungate.nuviseoul.database.DBOpenHelper;
import com.gangnam4bungate.nuviseoul.database.DataBases;
import com.gangnam4bungate.nuviseoul.map.DirectionFinder;
import com.gangnam4bungate.nuviseoul.map.DirectionFinderListener;
import com.gangnam4bungate.nuviseoul.map.PlaceFinder;
import com.gangnam4bungate.nuviseoul.map.PlaceFinderListener;
import com.gangnam4bungate.nuviseoul.map.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hschoi on 2017. 8. 06..
 */

public class CommonGoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback,DirectionFinderListener,PlaceFinderListener,GoogleMap.OnInfoWindowClickListener {
    protected GoogleMap mMap=null;
    private LatLng mLastedMarkLatLng=null;
    public ArrayList<Route> mRoutes = null;//new ArrayList<Route>();
    public int mType=0;
    public int mZoom=13;

    DBOpenHelper mDBOpenHelper;
    PlanData mPlanData = new PlanData();
    @Override
    /*protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
   */
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //onCreateView(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        if(this.mMap!=null)
            this.mMap.clear();

        if(this.mRoutes!=null)  {
            this.mRoutes.clear();
        }else{
            this.mRoutes=new ArrayList<Route>();
        }

        //DB
        mDBOpenHelper = DBOpenHelper.getInstance();
        if(mDBOpenHelper != null)
            mDBOpenHelper.open(getApplicationContext());
    }
    @Override
    public void onDirectionFinderStart() {

    }


    @Override
    public void onPlaceFinderSuccess(LatLng latLng, String pTitle) {
        MapMarkerDisplay(latLng,pTitle);
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        if(mMap==null)
        {
            return ;
        }
        mMap.clear();
        boolean bFirst=true;
        Marker makerInfo = null;
        for( Route route : routes) {

            if(bFirst==true) {
                makerInfo =mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.recommend_location))
                        .position(route.startLocation)
                        .title(route.startAddress));
                makerInfo.showInfoWindow();
                bFirst=false;
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.endLocation, mZoom));
            makerInfo = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.recommend_location))
                    .position(route.endLocation)
                    .title(route.endAddress));
            makerInfo.showInfoWindow();

            //시작과 종료위치가 다른 경우만 경로 데이터를 보여줌
            if(route.startAddress.compareToIgnoreCase(route.endAddress)!=0){

                PolylineOptions polylineOptions = new PolylineOptions()
                        .geodesic(true)
                        .color(Color.BLUE)
                        .width(10);

                polylineOptions.add(route.startLocation);

                for (int i = 0; i < route.points.size(); i++)
                    polylineOptions.add(route.points.get(i));

                polylineOptions.add(route.endLocation);

                mMap.addPolyline(polylineOptions);
            }
        }
    /* Iterator<Route> iterator=routes.iterator();
        while(iterator.hasNext()) {
            Route  item=(Route)iterator.next();

            mMap.addPolyline(new PolylineOptions()
                    .color(Color.BLUE)
                    .width(5)
                    .geodesic(true)
                    .add(item.startLocation)
                    .add(item.endLocation)
            );

            Toast.makeText(getApplicationContext(),item.startLocation.toString(), Toast.LENGTH_LONG)
                    .show();
        }*/
       /* String sOrigin="";
        sOrigin.format("<<<%f,%f >>",(float)mLastedMarkLatLng.latitude,(float)mLastedMarkLatLng.longitude);
        String sDestination=latLng.toString();
        Toast.makeText(getApplicationContext(),sOrigin, Toast.LENGTH_LONG)
                .show();*/
    }


    public void showLocationData(int planid){
        //Cursor c = mDBOpenHelper.planAllColumns();
        //int planid = 0;
        //예외처리 필요
        if(this.mMap==null)
            return;

        this.MapClear();

        Cursor c = mDBOpenHelper.plandetail_getColumn(planid);
        if(c != null) {
            do
            {
                //LatLng latLng=new LatLng(route.endLocation.latitude,route.endLocation.longitude);
                //MapMarkerDisplay(latLng,route.endTitle);
                double dLat=c.getDouble(c.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_GPS_LATITUDE));
                double dLng=c.getDouble(c.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_GPS_LONGITUDE));
                LatLng latLng=new LatLng(dLat,dLng );
                GetPlaceInfo(latLng);
            }while (c.moveToNext());
                c.close();
        }
    }


    private  void sendRequestWithDirection(LatLng ltOrigin,LatLng ltDestination,String pTitle)
    {
        //String strOrigin="37.509590,127.013767";
        //String strDestination="37.493909,127.014278";
        String strOrigin=ltOrigin.latitude+","+ltOrigin.longitude;
        String strDestination=ltDestination.latitude+","+ltDestination.longitude;
        if(strOrigin.isEmpty())
        {
            Toast.makeText(this,"Please, input origin address.",Toast.LENGTH_SHORT).show();
            return ;
        }

        if(strDestination.isEmpty())
        {
            Toast.makeText(this,"Please, input destination address.",Toast.LENGTH_SHORT).show();
            return ;
        }

        try
        {
            boolean bInsert=true;

            for(Route route : this.mRoutes ) {
                if((pTitle==route.startTitle)
                      ||(pTitle==route.endTitle) ){
                    bInsert=false;
                    break;
                }
            }

            if(bInsert) {
                new DirectionFinder(this, this.mRoutes, strOrigin, strDestination, pTitle).execute();
            }
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        LatLng seoul = new LatLng(37.52, 127.0);
        //mLastedMarkLatLng = seoul;
        //mMap.addMarker(new MarkerOptions().position(seoul)
        //              .title("Marker in Seoul")
        //                               /*.icon(BitmapDescriptorFactory.fromResource(com.google.android.gms.R.drawable.push_in))*/
        //);

        //
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        //mMap.setLocationSource( );


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,mZoom));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(-18.142, 178.431), 2));
        //moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.86997, 151.2089), 18));
    /*-------------------------------------------------------------------------------------------------*/
        // Other supported types include: MAP_TYPE_NORMAL,
        // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
    /*-------------------------------------------------------------------------------------------------*/

        // Customise the styling of the base map using a JSON object defined
        // in a raw resource file.
       /* MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_json);
        mMap.setMapStyle(style);*/
/*-------------------------------------------------------------------------------------------------*/

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        /*mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.house_flag))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(41.889, -87.622)));*/
/*-------------------------------------------------------------------------------------------------*/

        if(this.mType==0){
            //planid
            //plandetail
            //Cursor c = mDBOpenHelper.planAllColumns();
            //int planid = 0;
           /* Cursor c = mDBOpenHelper.plandetail_getColumn(this.mPlanId);
            if(c != null) {
                while (c.moveToNext()) {
                    //LatLng latLng=new LatLng(route.endLocation.latitude,route.endLocation.longitude);
                    //MapMarkerDisplay(latLng,route.endTitle);
                    LatLng latLng=new LatLng(c.getDouble(c.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_GPS_LATITUDE)), c.getDouble(c.getColumnIndex(DataBases.CreatePlanDetailDB._PLACE_GPS_LONGITUDE)));
                    GetPlaceInfo(latLng);
                }
                c.close();
            }*/
        }
/*-------------------------------------------------------------------------------------------------*/
   /*-------------------------------------------------------------------------------------------------*/
        else{//if(this.mType==1){

            //1안. 수정 상태 - 기존 데이터 저장
            /*
            if(PlanEditActivity.getInstance() != null) {
                //PlanEditActivity.getInstance().mLocationList;
                //1. 방식
                if(PlanEditActivity.getInstance().mLocationList.size()>0) {
                    ArrayList<Route> Routes = PlanEditActivity.getInstance().mLocationList;
                    for (Route route : mRoutes) {
                        LatLng latLng = new LatLng(route.endLocation.latitude, route.endLocation.longitude);
                        MapMarkerDisplay(latLng, route.endTitle);
                    }
                }
                //2번 방식
                this.showLocationData(PlanEditActivity.getInstance().mEditPlanId);
            }
            */
            //2안 : 추가 개념이므로 항상 신규맵 상태 유지
            //MapMarkerDisplay(new LatLng(37.5388,127.00155));
            //MapMarkerDisplay(new LatLng(37.6388,127.00455));

            //1 Map Click
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng arg0) {
                    /*String textTitle = "[Map Click] latitude ="
                            + arg0.latitude + ", longitude ="
                            + arg0.longitude;*/
          /*  Toast.makeText(getApplicationContext(), textTitle, Toast.LENGTH_LONG)
                    .show();*/
                    //Marker
                    LatLng latLng=new LatLng(arg0.latitude,arg0.longitude);
                    //mMap.addMarker(new MarkerOptions().position(latLng).title(textTitle));


                    // Polylines are useful for marking paths and routes on the map.
               /* mMap.addPolyline(new PolylineOptions().geodesic(true)
                        .add(new LatLng(-33.866, 151.195))  // Sydney
                        .add(new LatLng(-18.142, 178.431))  // Fiji
                        .add(new LatLng(21.291, -157.821))  // Hawaii
                        .add(new LatLng(37.423, -122.091))  // Mountain View
                );*/
               /*
               PolylineOptions polyline_options = new PolylineOptions()
                        .addAll(arraylist_lat_lon).color(Color.GREEN).width(2);
                polyline = googleMap.addPolyline(polyline_options);
               */

                /*mMap.addPolyline(new PolylineOptions()
                        .color(Color.BLUE)
                        .width(5)
                        .geodesic(true)
                        .add(mLastedMarkLatLng)
                        .add(latLng)
                        //.add(new LatLng(arg0.latitude,arg0.longitude))
                );

                String sOrigin="";
                sOrigin.format("<<<%f,%f >>",(float)mLastedMarkLatLng.latitude,(float)mLastedMarkLatLng.longitude);
                String sDestination=latLng.toString();
                Toast.makeText(getApplicationContext(),sOrigin, Toast.LENGTH_LONG)
                        .show();
                */
                    //좌표를 명칭으로 바꾸는 함수 호출
                    //String _Title="";
                    //MapMarkerDisplay(latLng,_Title);
                    GetPlaceInfo(latLng);
                }
            });
        }//if(this.mType==1)


        /*mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            public void onMapLongClick(LatLng point) {
                String text = "[Map LongClick] latitude ="
                        + point.latitude + ", longitude ="
                        + point.longitude;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                        .show();
            }
        });*/
    /*-------------------------------------------------------------------------------------------------*/
        //2.Map marker Click
       /* mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                for ( Route route : mRoutes) {
                    //System.out.println(el);
                    if(route.index == marker.getTitle()) {
                        //String text = "[Map Marker Click] latitude ="
                          //      + marker.getPosition().latitude + ", longitude ="
                          //      + marker.getPosition().longitude;
                        String text =route.index;
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                                .show();
                    }
                }
                return false;
            }
        });*/
    /*-------------------------------------------------------------------------------------------------*/
    }

    public void GetPlaceInfo(LatLng latLng)
    {
        try
        {
            new PlaceFinder(this,latLng).execute();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }


    public void MapMarkerDisplay(LatLng latLng,String pTitle)
    {

        if(mLastedMarkLatLng!=null) {
            sendRequestWithDirection(mLastedMarkLatLng, latLng,pTitle);
        }
        else
        {
                        /*String textTitle = "[Map Click] latitude ="
                                + arg0.latitude + ", longitude ="
                                + arg0.longitude;*/
           /* Marker makerInfo =  mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.recommend_location))
                    .position(latLng).title("Start"));

            makerInfo.showInfoWindow();*/
            sendRequestWithDirection(latLng, latLng,pTitle);
        }
        mLastedMarkLatLng = latLng;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }
/*
    public void MapMarkerDisplay(MarkerOptions _marker)
    {
        mMap.addMarker(_marker).showInfoWindow();
    }

    public void MapMarkerZoom(LatLng _location)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(_location,mZoom));
    }

    public void MapLineDrawing(LatLng _location)
    {
        mMap.addPolyline(new PolylineOptions()
                .color(Color.BLUE)
                .width(5)
                .geodesic(true)
                .add(mLastedMarkLatLng)
                .add(_location)
        );
    }

    public void MapPreviousLocation(LatLng _plocation)
    {
        mLastedMarkLatLng = _plocation;
    }
    */

    public void MapClear()
    {
        this.mMap.clear();
        this.mRoutes.clear();
        this.mLastedMarkLatLng=null;
    }

    public ArrayList<Route> getRoutes()
    {
        return this.mRoutes;
    }
}