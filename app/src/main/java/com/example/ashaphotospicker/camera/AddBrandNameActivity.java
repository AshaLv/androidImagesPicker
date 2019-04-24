package com.example.ashaphotospicker.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ashaphotospicker.AshaFactory.LinearLayoutParamsFac;
import com.example.ashaphotospicker.AshaHelper.BaseActivity;

import org.w3c.dom.Text;

public class AddBrandNameActivity extends BaseActivity {

    private LinearLayout root;
    private TextView t;
    private TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = createAddBrandNameActivityScreen();
        setContentView(root);
        super.setNavigationBar();
        addClickListenerToBrandName(t);
        addClickListenerToBrandName(t2);
    }

    private LinearLayout createAddBrandNameActivityScreen() {
        LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createDoubleParent();
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(params);

        params = new LinearLayoutParamsFac().createDoubleContent();
        t = new TextView(this);
        t.bringToFront();
        t.setText("我的品牌名1");
        t.setTag("656364353453454");
        t.setTextSize(50);
        t.setLayoutParams(params);

        t2 = new TextView(this);
        t2.setTag("535353643422523452");
        t2.setText("我的品牌名2");
        t2.setTextSize(50);
        t2.setLayoutParams(params);

        root.addView(t);
        root.addView(t2);

        return root;
    }

    private void backToHorizontalImagesActivity(TextView brandNameText) {
        String brandName = (String) brandNameText.getText();
        String brandId = (String)brandNameText.getTag();
        Intent backIntent = new Intent();
        backIntent.putExtra("brandName",brandName);
        backIntent.putExtra("brandId",brandId);
        setResult(Activity.RESULT_OK,backIntent);
        finish();
    }

    private void addClickListenerToBrandName(final TextView t) {
        if(getIntent().getExtras().get("flag").equals("brandNameBack")) {
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToHorizontalImagesActivity((TextView)v);
                }
            });
        }
    }

}
