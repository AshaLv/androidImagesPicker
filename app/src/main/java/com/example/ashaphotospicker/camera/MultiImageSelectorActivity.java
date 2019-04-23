package com.example.ashaphotospicker.camera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import com.example.ashaphotospicker.R;
import com.example.ashaphotospicker.camera.HorizontalImagesActivity;
import com.example.ashaphotospicker.camera.adapter.ImageGridAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MultiImageSelectorActivity extends AppCompatActivity implements MultiImageSelectorFragment.Callback {
    private static final String TAG = "MultiImageSelector";
    // Single choice
    public static final int MODE_SINGLE = 0;
    // Multi choice
    public static final int MODE_MULTI = 1;

    /** Max image size，int，{@link #DEFAULT_IMAGE_SIZE} by default */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** Select mode，{@link #MODE_MULTI} by default */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** Whether show camera，true by default */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /** Result data set，ArrayList&lt;String&gt;*/
    public static final String EXTRA_RESULT = "select_result";
    /** Original data set */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";
    // Default image size
    private static final int DEFAULT_IMAGE_SIZE = 9;

    private ArrayList<String> resultList = new ArrayList<>();
    private Button mSubmitButton;
    private int mDefaultCount = DEFAULT_IMAGE_SIZE;

    private SharedPreferences sharedPreferences;
    private FrameLayout root;

    public ArrayList<String> getImagesPathFromCache() {
        if(sharedPreferences.contains("imagesPath") && sharedPreferences.getString("imagesPath","").length() > 0) {
            resultList.clear();
            Gson gson = new Gson();
            ArrayList resultList_ = gson.fromJson(sharedPreferences.getString("imagesPath",""), ArrayList.class);
            resultList.addAll(resultList_);
            Log.d(TAG,"gson.fromJson(sharedPreferences.getString(\"imagesPath\",\"\"), ArrayList.class) " + resultList);
            updateDoneText(resultList);
        } else {
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("multiImageSelectorActivityImagesPathCache", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        setTheme(R.style.MIS_NO_ACTIONBAR);
        setContentView(R.layout.multi_photo_screen);

        root = (FrameLayout) findViewById(R.id.image_grid);

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
            actionBar.setTitle("");
        }

        final Intent intent = getIntent();
        mDefaultCount = 9;
        final int mode = MultiImageSelectorActivity.MODE_MULTI;
        final boolean isShow = false;

        mSubmitButton = (Button) findViewById(R.id.commit);
        if(mode == MODE_MULTI){
            updateDoneText(resultList);
            mSubmitButton.setVisibility(View.VISIBLE);
            mSubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(resultList != null && resultList.size() >0){
                        // Notify success
                        Intent intent = new Intent(MultiImageSelectorActivity.this, HorizontalImagesActivity.class);
                        Gson gsonBuilder = new GsonBuilder().create();
                        editor.putString("imagesPath", gsonBuilder.toJson(resultList));
                        editor.commit();
                        startActivity(intent);
                        return;
                    }else{
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                }
            });
        }else{
            mSubmitButton.setVisibility(View.GONE);
        }

        if(savedInstanceState == null){
            Bundle bundle = new Bundle();
            bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
            bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
            bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
            bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                    .commit();
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

    private void updateDoneText(ArrayList<String> resultList) {
        int size = 0;
        if(resultList == null || resultList.size()<=0){
            mSubmitButton.setText(R.string.mis_action_done);
            mSubmitButton.setEnabled(false);
        }else{
            size = resultList.size();
            mSubmitButton.setEnabled(true);
        }
        mSubmitButton.setText(getString(R.string.mis_action_button_string,
                getString(R.string.mis_action_done), size, mDefaultCount));
    }

    @Override
    public void onImageSelected(String path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        updateDoneText(resultList);
    }

    @Override
    public void onImageUnselected(String path) {
        if(resultList.contains(path)){
            resultList.remove(path);
        }
        updateDoneText(resultList);
    }


}
