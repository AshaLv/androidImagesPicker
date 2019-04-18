package com.example.ashaphotospicker.camera.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import com.example.ashaphotospicker.R;
import com.example.ashaphotospicker.camera.GlideApp;
import com.example.ashaphotospicker.camera.HorizontalImagesActivity;
import com.example.ashaphotospicker.camera.MyGlideApp;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


public class HorizontalImagesAdapter extends RecyclerView.Adapter<HorizontalImagesAdapter.ViewHolder> {

    private static final String TAG = "HorizontalImagesAdapter";

    //vars
    private ArrayList<String> images = new ArrayList<>();
    private Context mContext;
    private HorizontalImagesActivity activity;
    private float x;
    private float y;

    public HorizontalImagesAdapter(Context context, ArrayList<String> images, HorizontalImagesActivity activity) {
        this.images = images;
        // 存储了图片的路径
        this.mContext = context;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        Log.d(TAG, String.valueOf(position));
        Log.d(TAG, images.get(position));

        File f = new File(images.get(position));
        Picasso.with(mContext)
                .load(f)
                .placeholder(R.mipmap.ic_launcher) // optional
                .fit()
                .centerCrop()
                .error(R.mipmap.ic_launcher) //if error
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.recordPoint();
                activity.showBottomModal();
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = images.size();
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.slidingImage);
        }
    }

}
