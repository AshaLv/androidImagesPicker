package com.example.ashaphotospicker.camera;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.example.ashaphotospicker.R;

import java.util.ArrayList;
import java.util.List;

import com.example.ashaphotospicker.camera.adapter.HorizontalImagesAdapter;

public class HorizontalImagesActivity extends AppCompatActivity {
    private static final String TAG = "HorizontalImages";

    private RecyclerView horizontalImagesScreenRecycleView;
    private HorizontalImagesAdapter horizontalImagesAdapter;
    private LinearLayoutManager layoutManager;

    private Dialog bottomBar = null;
    private LinearLayout root;
    private ArrayList<String> horizontalImages;

    private void showNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizontal_images_screen);

        root = (LinearLayout) findViewById(R.id.horizontalImagesRoot);

        showNavigationBar();

        horizontalImages = getHorizontalImages();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        horizontalImagesScreenRecycleView = (RecyclerView) findViewById(R.id.horizontalSlidingImages);

        horizontalImagesScreenRecycleView.setLayoutManager(layoutManager);

        horizontalImagesAdapter = new HorizontalImagesAdapter(this, horizontalImages, this);

        horizontalImagesScreenRecycleView.setAdapter(horizontalImagesAdapter);


    }

    private ArrayList<String> getHorizontalImages() {
        Intent intentCarringImagesPath = getIntent();
        ArrayList<String> imagesPath = intentCarringImagesPath.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
        return imagesPath;

    }

    public void recordPoint() {

    }

    public void showBottomModal() {
        if(bottomBar == null) {
            createBottomBar();
            bottomBar.show();
        }else{
            if(bottomBar.isShowing()) {
                bottomBar.dismiss();
            }else{
                bottomBar.show();
            }

        }


    }

    private void createBottomBar() {
        //全局linearLayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout.setLayoutParams(params1);

        //第一行linearLayout：取消 、标题、 保存

        //第二行linearLayout：添加商品、商品结果

        //第三行linearLayout：添加品牌、品牌结果

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params2.leftMargin = 44;
        params2.rightMargin = 44;
        params2.topMargin = 100;

        TextView textview1 = new TextView(this);
        textview1.setBackgroundColor(Color.GRAY);
        textview1.setText("添加商品..");
        textview1.setHeight(200);
        textview1.setTextColor(Color.BLACK);
        textview1.setGravity(Gravity.CENTER);
        textview1.setLayoutParams(params2);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params3.leftMargin = 44;
        params3.rightMargin = 44;
        params3.topMargin = 100;
        params3.bottomMargin = 100;
        TextView textview2 = new TextView(this);
        textview2.setBackgroundColor(Color.GRAY);
        textview2.setText("添加品牌..");
        textview2.setTextColor(Color.BLACK);
        textview2.setHeight(200);
        textview2.setGravity(Gravity.CENTER);
        textview2.setLayoutParams(params3);

        linearLayout.addView(textview1,0);
        linearLayout.addView(textview2,1);

        Dialog dialog = new Dialog(HorizontalImagesActivity.this);
        dialog.setContentView(linearLayout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);


        bottomBar = dialog;


    }



}
