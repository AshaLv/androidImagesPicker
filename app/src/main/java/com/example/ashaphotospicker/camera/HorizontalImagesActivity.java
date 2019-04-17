package com.example.ashaphotospicker.camera;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ashaphotospicker.R;

import java.util.List;

public class HorizontalImagesActivity extends AppCompatActivity {
    private static final String TAG = "HorizontalImages";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizontal_images_screen);

        Intent intentCarringImagesPath = getIntent();
        List<String> imagesPath = intentCarringImagesPath.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
        Log.d(TAG, imagesPath.toString());








    }

}
