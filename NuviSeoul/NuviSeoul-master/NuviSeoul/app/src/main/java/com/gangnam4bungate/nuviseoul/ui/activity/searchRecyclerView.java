package com.gangnam4bungate.nuviseoul.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gangnam4bungate.nuviseoul.R;
import com.gangnam4bungate.nuviseoul.config.searchDTO;
import com.gangnam4bungate.nuviseoul.config.searchDetailDTO;
import com.gangnam4bungate.nuviseoul.data.PlanDetailData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by shj89 on 2017-09-16.
 */

public class searchRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<searchDTO> searchDTOs = new ArrayList<>();
    private ArrayList<searchDetailDTO> searchDetailDTOs = new ArrayList<>();
    Map<String, String> map;
    Activity mActivity;

    public searchRecyclerView(ArrayList searchDTO, ArrayList searchDetailDTO, Map map, Activity activity) {
        this.searchDTOs = searchDTO;
        this.searchDetailDTOs = searchDetailDTO;
        mActivity = activity;
        this.map = map;
        Log.d("testtt" , searchDetailDTO.size() + " " +searchDetailDTO.size());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search,parent,false);

        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        String view = map.get(searchDTOs.get(position).contentId);
        final String homepage = view.substring(0, view.indexOf("|"));
        String overview = view.substring(view.indexOf("|") +1, view.length());

        ((RowCell)holder).searchTitle.setText(searchDTOs.get(position).title);
        ((RowCell) holder).searchDescription.setText(overview);
        ((RowCell) holder).searchHomepage.setText(homepage);

        if(searchDTOs.get(position).firstImage.toString().equals("no Parsing") ){
            Drawable drawable = mActivity.getResources().getDrawable(R.drawable.noimage);
            ((RowCell) holder).searchImage.setImageDrawable(drawable);
            ((RowCell) holder).searchImage.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        }else{
            Picasso.with(mActivity.getApplicationContext())
                    .load(searchDTOs.get(position).firstImage.toString())
                    .fit()
                    .into(((RowCell) holder).searchImage);
            ((RowCell) holder).searchImage.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        }

//        if(searchDetailDTOs.get(position).homePage.equals("주소가 없어요.")){
//            ((RowCell) holder).homePageButton.setVisibility(View.INVISIBLE);
//        }

        if(homepage.equals("주소가 없어요.")){
            ((RowCell) holder).homePageButton.setVisibility(View.INVISIBLE);
        } else {
            ((RowCell) holder).homePageButton.setVisibility(View.VISIBLE);
        }


        ((RowCell) holder).homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + homepage));
                mActivity.startActivity(intent);
            }
        });

      //  mSearchDataList.add(new PlanDetailData())

        ((RowCell) holder).addButton.setTag(searchDTOs.get(position));
        ((RowCell) holder).addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    searchDTO dto = (searchDTO) v.getTag();
                    if (dto != null) {
                        Intent intent = new Intent(mActivity.getApplicationContext(), PlanEditActivity.class);
                        intent.putExtra("search_add_course", true);
                        intent.putExtra("placename", dto.title);
                        intent.putExtra("mapx", dto.mapX);
                        intent.putExtra("mapy", dto.mapY);
                        mActivity.startActivity(intent);
                    }
                }catch(Exception e){

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchDTOs.size();
    }


    private class RowCell extends RecyclerView.ViewHolder {
        TextView searchTitle;
        TextView searchDescription;
        TextView searchHomepage;
        ImageView searchImage;
        Button homePageButton;
        Button addButton;

        public RowCell(View view) {
            super(view);
            searchTitle = (TextView) view.findViewById(R.id.searchTitle);
            searchDescription = (TextView) view.findViewById(R.id.searchDescription);
            searchHomepage = (TextView) view.findViewById(R.id.searchHomepage);
            searchImage = (ImageView) view.findViewById(R.id.searchImage);
            homePageButton = (Button) view.findViewById(R.id.homePageButton);
            addButton = (Button) view.findViewById(R.id.addButton);
        }
    }

    public class CircleTransform implements Transformation {
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
                bitmap = Bitmap.createBitmap(size , size , source.getConfig());
            } else {
                bitmap = Bitmap.createBitmap(size , size , Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            paint.setDither(true);
            paint.setAntiAlias(true);

            float radius = size / 2f;
            canvas.drawCircle(radius, radius, radius, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }


}


