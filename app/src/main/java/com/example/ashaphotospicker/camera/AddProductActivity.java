package com.example.ashaphotospicker.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ashaphotospicker.AshaFactory.RelativeLayoutParamsFac;
import com.example.ashaphotospicker.AshaHelper.BaseActivity;
import com.example.ashaphotospicker.AshaHelper.Random;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;

public class AddProductActivity extends BaseActivity {

    private LinearLayout root;
    private LinearLayout scrollViewRoot;
    private final String PRODUCT_NAME = "productName";
    private final String PRODUCT_ID = "productId";
    private final String PRODUCT_URL = "productUrl";
    private final String default_image = "https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createAddProductActivityScreen());
        super.setNavigationBar();

    }

    private RelativeLayout createOneProductInfoWrapper(String productName, String productId, String productUrl, String brandName, String brandId) {
        int randomInt = Random.getRandomInt();

        RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createOnlyWidthMatchParent();
        params.topMargin = 16;
        params.bottomMargin = 16;
        RelativeLayout productInfoWrapper = new RelativeLayout(this);
        productInfoWrapper.setLayoutParams(params);

        params = new RelativeLayoutParamsFac().createDoubleContent();
        params.leftMargin = 44;
        ImageView productImage = new ImageView(this);
        Picasso.with(this)
                .load(productUrl)
                .resize(300,300)
                .into(productImage);
        productImage.setLayoutParams(params);
        productImage.setId(randomInt);

        params = new RelativeLayoutParamsFac().createDoubleContent();
        params.addRule(
                RelativeLayout.RIGHT_OF,
                productImage.getId()
        );
        params.leftMargin = 16;
        TextView productNameView = new TextView(this);
        productNameView.setText(productName);
        productNameView.setTextSize(24);
        productNameView.setLayoutParams(params);

        productInfoWrapper.addView(productImage);
        productInfoWrapper.addView(productNameView);

        Hashtable object = new Hashtable<>();
        object.put("productName",productName);
        object.put("productUrl",productUrl);
        object.put("productId",productId);
        object.put("brandName",brandName);
        object.put("brandId",brandId);
        productInfoWrapper.setTag(object);
        return productInfoWrapper;
    }

    private LinearLayout createAddProductActivityScreen() {
        RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createOnlyWidthMatchParent();
        params.topMargin = 44;
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(params);

        scrollViewRoot = new LinearLayout(this);
        scrollViewRoot.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.canScrollVertically(ScrollView.SCROLL_AXIS_VERTICAL);

        String[] names = {"arafs","gdgs","hdhf"};
        for(String name:names) {
            final RelativeLayout productInfoWrapper = createOneProductInfoWrapper(name, name+"id", default_image, name+"brandName", name+"brandId");
            addClickListenerToProductInfoWrapper(productInfoWrapper);
            scrollViewRoot.addView(productInfoWrapper);
        }

        scrollView.addView(scrollViewRoot);
        root.addView(scrollView);
        return root;
    }

    private void backToHorizontalImagesActivity(RelativeLayout productInfoWrapper) {
        Hashtable object = (Hashtable) productInfoWrapper.getTag();
        String productName = (String) object.get("productName");
        String productId = (String) object.get("productId");
        String productUrl = (String) object.get("productUrl");
        String brandName = (String) object.get("brandName");
        String brandId = (String) object.get("brandId");
        Intent backIntent = new Intent();
        backIntent.putExtra("productName",productName);
        backIntent.putExtra("productId",productId);
        backIntent.putExtra("productUrl",productUrl);
        backIntent.putExtra("brandName",brandName);
        backIntent.putExtra("brandId",brandId);
        setResult(Activity.RESULT_OK,backIntent);
        finish();
    }

    private void addClickListenerToProductInfoWrapper(final RelativeLayout productInfoWrapper) {
        productInfoWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHorizontalImagesActivity((RelativeLayout) v);
            }
        });
    }

}
