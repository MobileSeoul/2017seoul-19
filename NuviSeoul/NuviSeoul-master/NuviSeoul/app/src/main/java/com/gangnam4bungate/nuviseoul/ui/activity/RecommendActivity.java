package com.gangnam4bungate.nuviseoul.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.network.NetworkManager;
import com.gangnam4bungate.nuviseoul.ui.common.CommonGoogleMapActivity;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mystory.commonlibrary.network.MashupCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wsseo on 2017. 7. 9..
 */

//public class RecommendActivity extends CommonActivity {
//public class RecommendActivity extends CommonGoogleMapActivity implements CommonActivity {
public class RecommendActivity extends CommonGoogleMapActivity implements MashupCallback {

    RelativeLayout mrl_back;
    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;
    private List<RecommendData> data;

    ImageView closehandle;
    SlidingDrawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        this.mType=1;//수정모드

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        TextView tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        if(tv_title != null){
            tv_title.setText(getString(R.string.plan_make_title));
        }
        setSupportActionBar(toolbar);

        mrl_back = (RelativeLayout) toolbar.findViewById(R.id.rl_back);
        if(mrl_back != null){
            mrl_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        horizontal_recycler_view= (RecyclerView) findViewById(R.id.horizontal_recycler_view);

        drawer = (SlidingDrawer)findViewById(R.id.slide);
        drawer.animateOpen();

        closehandle = (ImageView) findViewById(R.id.close);

        closehandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.animateClose();
            }
        });
/*
        data = fill_with_data();

        horizontalAdapter=new HorizontalAdapter(data, getApplication());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(RecommendActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
*/
        Paint paint = new Paint();

        paint.setAlpha(50);
        ((LinearLayout)findViewById(R.id.content)).setBackgroundColor(paint.getColor());

        NetworkManager.getInstance().requsetRecommendLocationInfo(this);

        ImageButton saveBtn = (ImageButton) findViewById(R.id.locationSave);
        ImageButton resetBtn = (ImageButton) findViewById(R.id.locationReset);
        ImageButton cancelBtn = (ImageButton) findViewById(R.id.locationCancel);

        saveBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 저장 버튼 눌렀을시 이벤트
                if(PlanEditActivity.getInstance() != null)
                    PlanEditActivity.getInstance().setLocations(getRoutes());
                finish();


               /* Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("location", getRoutes());
                bundle.putArrayList("location", getRoutes());
                Intent intent  = new Intent();
                intent.putExtra("locations", new Bundle());
                setResult(0, intent);*/
                //Toast.makeText(RecommendActivity.this, "저장!!", Toast.LENGTH_SHORT).show();
            }
        });

        resetBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 초기화 버튼 눌렀을시 이벤트
                MapClear();
                //Toast.makeText(RecommendActivity.this, "초기화!!", Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 취소 버튼 눌렀을시 이벤트
                //Toast.makeText(RecommendActivity.this, "취소!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

/*
    public List<RecommendData> fill_with_data() {

        List<RecommendData> data = new ArrayList<>();

        data.add(new RecommendData( R.mipmap.ic_launcher, "서울타워",37.5511694,126.98822659999996));
        data.add(new RecommendData( R.mipmap.ic_launcher, "경복궁",37.579617,126.97704099999999));
        data.add(new RecommendData( R.mipmap.ic_launcher, "63빌딩",37.5193776,126.94021029999999));


//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("location", );
//        Intent intent  = new Intent();
//        intent.putExtra("locations", new Bundle());
//        setResult(0, intent);

        return data;
    }
*/
    @Override
    public void onMashupSuccess(JSONObject object, int requestCode) {

        //String addr1 = null;
        String title = null;
        String mapx = null;
        String mapy = null;
        String image = null;


        List<RecommendData> data = new ArrayList<>();


        try
        {
            JSONObject jsonObject = new JSONObject(object.toString());
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject locations = body.getJSONObject("items");
            JSONArray location = locations.getJSONArray("item");


            for (int i = 0; i < location.length(); i++) {
                JSONObject jsonlocation = location.getJSONObject(i);

                title = jsonlocation.getString("title");
                //addr1 = jsonlocation.getString("addr1");
                mapx = jsonlocation.getString("mapx");
                mapy = jsonlocation.getString("mapy");
                image = jsonlocation.optString("firstimage2", "no image");

                //data.add(new RecommendData( image, title, addr1, Double.parseDouble(mapy), Double.parseDouble(mapx)));
                data.add(new RecommendData( image, title, Double.parseDouble(mapy), Double.parseDouble(mapx)));
            }
            horizontalAdapter=new HorizontalAdapter(data, getApplication());

            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(RecommendActivity.this, LinearLayoutManager.HORIZONTAL, false);
            horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
            horizontal_recycler_view.setAdapter(horizontalAdapter);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onMashupFail(VolleyError error, int requestCode) {

    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


        List<RecommendData> horizontalList = Collections.emptyList();
        Context context;


        public HorizontalAdapter(List<RecommendData> horizontalList, Context context) {
            this.horizontalList = horizontalList;
            this.context = context;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;

            public MyViewHolder(View view) {
                super(view);
                imageView=(ImageView) view.findViewById(R.id.imageview);

            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_location, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            //holder.imageView.setImageResource(horizontalList.get(position).imageId);
            //holder.imageView.setImageBitmap(horizontalList.get(position).image);

            Picasso.with(context)
                    .load(horizontalList.get(position).image.toString())
                    .transform(new RecommendCircleTransform())
                    .centerCrop()
                    .resize(250,250)
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String title = horizontalList.get(position).title.toString();
                    //String addres = horizontalList.get(position).addres.toString();
                    double lati = horizontalList.get(position).latitude;
                    double longi = horizontalList.get(position).longitude;

                    LatLng location = new LatLng(lati, longi);
                    /*

                    MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.recommend_location));

                    marker .position(new LatLng(lati, longi))
                            .title(list)
                            .snippet("Seoul");

                    MapMarkerDisplay(marker);
                    MapMarkerZoom(location);
                    MapLineDrawing(location);
                    MapPreviousLocation(location);
                    */
                    //맵에 마크 추가하기
                    MapMarkerDisplay(location,title);
                    Toast.makeText(RecommendActivity.this, title, Toast.LENGTH_SHORT).show();
                }

            });
        }

        @Override
        public int getItemCount()
        {
            return horizontalList.size();
        }
    }

    public class RecommendCircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if(squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = null;
            //int[] colors = new int[0];

            if(source.getConfig() != null) {
                bitmap = Bitmap.createBitmap(size + 10, size + 10, source.getConfig());
            } else {
                bitmap = Bitmap.createBitmap(size + 10, size + 10, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            paint.setDither(true);
            paint.setAntiAlias(true);

            float radius = size / 2f;
            canvas.drawCircle(radius + 5, radius + 5, radius, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}

