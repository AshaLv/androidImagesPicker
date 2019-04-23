package com.example.ashaphotospicker.camera.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Target;
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
        RelativeLayout slidingImageRoot = (RelativeLayout) view;
        return new ViewHolder(view,slidingImageRoot);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        File f = new File(images.get(position));

        Picasso.with(mContext)
                .load(f)
                .placeholder(R.mipmap.ic_launcher) // optional
                .fit()
                .centerCrop()
                .error(R.mipmap.ic_launcher) //if error
                .into(holder.image);

        holder.image.setOnTouchListener(new View.OnTouchListener() {
            float x2 = 0;
            float y2 = 0;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();
                        activity.recordPoint(x2,y2,holder.image,images.get(position),holder.slidingImagesRoot);
                        activity.showBottomModal();
                        break;
                    default: break;
                }
                return true;
            }
        });

        holder.image.post(new Runnable() {
            @Override
            public void run() {
                activity.displayCachedTagsInsideImage(holder.image,images.get(position),holder.slidingImagesRoot);
            }
        });

    }

    @Override
    public int getItemCount() {
        int size = images.size();
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private RelativeLayout slidingImagesRoot;

        public ViewHolder(View itemView, RelativeLayout slidingImagesRoot) {
            super(itemView);
            this.image = itemView.findViewById(R.id.slidingImage);
            this.slidingImagesRoot = slidingImagesRoot;
        }
    }

}
