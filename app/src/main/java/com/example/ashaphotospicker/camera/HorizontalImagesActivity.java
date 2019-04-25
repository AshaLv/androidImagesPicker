package com.example.ashaphotospicker.camera;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ashaphotospicker.AshaFactory.LinearLayoutParamsFac;
import com.example.ashaphotospicker.AshaFactory.RelativeLayoutParamsFac;
import com.example.ashaphotospicker.AshaFactory.ToolbarLayoutParamsFac;
import com.example.ashaphotospicker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;

import java.util.Hashtable;
import java.util.List;

import com.example.ashaphotospicker.camera.adapter.HorizontalImagesAdapter;
import com.example.ashaphotospicker.camera.bean.HorizontalImagesWithTags;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;


public class HorizontalImagesActivity extends AppCompatActivity {
    private static final String TAG = "HorizontalImages";

    private RecyclerView horizontalImagesScreenRecycleView;
    private HorizontalImagesAdapter horizontalImagesAdapter;
    private LinearLayoutManager layoutManager;

    private Dialog bottomBar = null;
    private LinearLayout root;
    private ArrayList<String> horizontalImages = new ArrayList<String>();
    private Dictionary<String, HorizontalImagesWithTags> horizontalImagesWithTags = new Hashtable<String, HorizontalImagesWithTags>();

    private TextView productResult;
    private TextView brandResult;

    private float x;
    private float y;
    private ImageView currentHolderImageView;
    private String currentHolderImagePath;
    private RelativeLayout slidingImageRoot;
    RelativeLayout.LayoutParams params_;

    private SharedPreferences multiImageSelectorActivityImagesWithTagsSharedPreferences;
    private SharedPreferences multiImageSelectorActivityImagesPathCacheSharedPreferences;

    private final int nextStepButtonId = 42423;

    private final Gson gson = new Gson();
    private final Gson gsonBuilder = new GsonBuilder().create();

    private final int usedWidthNumber = 145235425;
    private final int usedHeightNumber = 145435425;
    private final int ADD_BRAND_NAME_REQUEST_CODE = 314;
    private final int ADD_PRODUCT_REQUEST_CODE = 4398;
    private final int PRODUCT_HIDDEN_TEXT = 4959;
    private final int BRAND_HIDDEN_TEXT = 4912;
    private boolean OLD_TAG_BE_CLICKED = false;
    private final int NEED_BRAND_NAME_BACK_FLAG = 324;
    private final int NEED_PRODUCT_NAME_BACK_FLAG = 111;

    private SharedPreferences.Editor editor = null;

    private TextView currentBeingModifiedExistedTag;
    private String oldBrandResultText;

    private void initImages() {
        if(multiImageSelectorActivityImagesPathCacheSharedPreferences.contains("imagesPath") && multiImageSelectorActivityImagesPathCacheSharedPreferences.getString("imagesPath","").length() > 0) {
            if(this.horizontalImages.size() > 0) {
                this.horizontalImages.clear();
            }
            ArrayList<String> horizontalImages_ = gson.fromJson(multiImageSelectorActivityImagesPathCacheSharedPreferences.getString("imagesPath",""), ArrayList.class);
            this.horizontalImages.addAll(horizontalImages_);
            this.horizontalImagesAdapter.notifyDataSetChanged();
        }
    }

    public void displayCachedTagsInsideImage(ImageView currentHolderImageView, String currentHolderImagePath, RelativeLayout slidingImageRoot) {
        if(multiImageSelectorActivityImagesWithTagsSharedPreferences.contains("ImagesWithTags") && multiImageSelectorActivityImagesWithTagsSharedPreferences.getString("ImagesWithTags","").length() > 0) {
            horizontalImagesWithTags = gson.fromJson(multiImageSelectorActivityImagesWithTagsSharedPreferences.getString("ImagesWithTags",""), Hashtable.class);
            if(horizontalImagesWithTags.get(currentHolderImagePath) != null) {
                String HorizontalImagesWithTagsToString = gson.toJson(horizontalImagesWithTags.get(currentHolderImagePath));
                HorizontalImagesWithTags foo = gson.fromJson(HorizontalImagesWithTagsToString,HorizontalImagesWithTags.class);
                ArrayList<String> tagNames = foo.tagNames;
                ArrayList<Float> tagPoints = foo.tagPoints;
                this.currentHolderImageView = currentHolderImageView;
                this.currentHolderImagePath = currentHolderImagePath;
                this.slidingImageRoot = slidingImageRoot;
                for(int j=0; j<tagNames.size(); j++) {
                    x = tagPoints.get(j*2) * currentHolderImageView.getWidth();
                    y = tagPoints.get(j*2+1) * currentHolderImageView.getHeight();
                    putResultTextOntoCurrentImage(tagNames.get(j));
                }

            }
        }
    }

    private HorizontalImagesWithTags getCurrentImagePathCacheData(String path) {
        horizontalImagesWithTags = gson.fromJson(multiImageSelectorActivityImagesWithTagsSharedPreferences.getString("ImagesWithTags",""), Hashtable.class);
        String HorizontalImagesWithTagsToString = gson.toJson(horizontalImagesWithTags.get(path));
        HorizontalImagesWithTags foo = gson.fromJson(HorizontalImagesWithTagsToString,HorizontalImagesWithTags.class);
        return foo;
    }

    private SharedPreferences.Editor singletonGetEditor() {
        if(editor == null) {
            editor = multiImageSelectorActivityImagesWithTagsSharedPreferences.edit();
            return editor;
        } else {
            return editor;
        }
    }

    private void updateCurrentImagePathCacheData(String path, HorizontalImagesWithTags newData) {
        horizontalImagesWithTags.put(path,newData);
        singletonGetEditor().putString("ImagesWithTags", gsonBuilder.toJson(horizontalImagesWithTags));
        singletonGetEditor().commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, nextStepButtonId, Menu.NONE, "下一步").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }


    private void showNavigationBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setSupportActionBar(toolbar);

        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");

        }


    }

    private void updateImagesTagCacheData(String imagePath, String text, float x, float y, String url) {
        if(horizontalImagesWithTags.get(imagePath) != null) {
            String HorizontalImagesWithTagsToString = gson.toJson(horizontalImagesWithTags.get(imagePath));
            HorizontalImagesWithTags foo = gson.fromJson(HorizontalImagesWithTagsToString,HorizontalImagesWithTags.class);
            foo.tagNames.add(text);
            foo.tagPoints.add(x);
            foo.tagPoints.add(y);
            horizontalImagesWithTags.put(imagePath,foo);
        }else {
            ArrayList<String> tagNames = new ArrayList<>();
            ArrayList<Float> tagPoints = new ArrayList<>();
            tagNames.add(text);
            tagPoints.add(x);
            tagPoints.add(y);
            HorizontalImagesWithTags foo = new HorizontalImagesWithTags(
                    "",
                    tagNames,
                    tagPoints
            );
            horizontalImagesWithTags.put(imagePath,foo);
        }
        singletonGetEditor().putString("ImagesWithTags", gsonBuilder.toJson(horizontalImagesWithTags));
        singletonGetEditor().commit();
    }

    private void updateImagesTagCacheDataAfterDrag(String imagePath, float x, float y, String text) {
        if(horizontalImagesWithTags.get(imagePath) != null) {
            String HorizontalImagesWithTagsToString = gson.toJson(horizontalImagesWithTags.get(imagePath));
            HorizontalImagesWithTags foo = gson.fromJson(HorizontalImagesWithTagsToString,HorizontalImagesWithTags.class);
            for(int i=0; i<foo.tagNames.size(); i++) {
                if(foo.tagNames.get(i).equals(text) ) {
                    foo.tagPoints.set(i*2,x);
                    foo.tagPoints.set(i*2+1,y);
                    break;
                }
            }
            horizontalImagesWithTags.put(imagePath,foo);
            singletonGetEditor().putString("ImagesWithTags", gsonBuilder.toJson(horizontalImagesWithTags));
            singletonGetEditor().commit();
        }
    }

    private void deleteImagesTagCacheData(String imagePath, String text) {
        if(horizontalImagesWithTags.get(imagePath) != null) {
            String HorizontalImagesWithTagsToString = gson.toJson(horizontalImagesWithTags.get(imagePath));
            HorizontalImagesWithTags foo = gson.fromJson(HorizontalImagesWithTagsToString,HorizontalImagesWithTags.class);
            for(int i=0; i<foo.tagNames.size(); i++) {
                if(foo.tagNames.get(i).equals(text) ) {
                    foo.tagNames.remove(i);
                    foo.tagPoints.remove(i*2);
                    foo.tagPoints.remove(i*2); // 因为移除第一个后，第二个的位置就降下跟x坐标一样了，不这样写就会报溢出错误了
                    break;
                }
            }
            horizontalImagesWithTags.put(imagePath,foo);
            singletonGetEditor().putString("ImagesWithTags", gsonBuilder.toJson(horizontalImagesWithTags));
            singletonGetEditor().commit();
        }
    }

    private void finalUpdateImagesTagCacheData() {
        singletonGetEditor().putString("ImagesWithTags", gsonBuilder.toJson(horizontalImagesWithTags));
        singletonGetEditor().commit();
    }

    private void goToStoryOfPostUgc() {
        Intent intent = new Intent();
    }

    private void compressImageAndGetUrl() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            case nextStepButtonId:
                finalUpdateImagesTagCacheData();
                compressImageAndGetUrl();
                goToStoryOfPostUgc();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        multiImageSelectorActivityImagesWithTagsSharedPreferences = getSharedPreferences("multiImageSelectorActivityImagesWithTags", MODE_PRIVATE);
        multiImageSelectorActivityImagesPathCacheSharedPreferences = getSharedPreferences("multiImageSelectorActivityImagesPathCache", MODE_PRIVATE);
        final SharedPreferences.Editor editor = multiImageSelectorActivityImagesWithTagsSharedPreferences.edit();

        setContentView(R.layout.horizontal_images_screen);

        root = (LinearLayout) findViewById(R.id.horizontalImagesRoot);

        showNavigationBar();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        horizontalImagesScreenRecycleView = (RecyclerView) findViewById(R.id.horizontalSlidingImages);

        horizontalImagesScreenRecycleView.setHasFixedSize(true);

        horizontalImagesScreenRecycleView.setLayoutManager(layoutManager);

        horizontalImagesAdapter = new HorizontalImagesAdapter(this, horizontalImages, this);

        horizontalImagesScreenRecycleView.setAdapter(horizontalImagesAdapter);

        initImages();

        new PagerSnapHelper().attachToRecyclerView(horizontalImagesScreenRecycleView);

        TextView p = (TextView)findViewById(R.id.promptOfAddingTagAndProductName);
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,gson.toJson(getCurrentImagePathCacheData(currentHolderImagePath)));
            }
        });

        p.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG,currentHolderImagePath);
                HorizontalImagesWithTags h = new HorizontalImagesWithTags("",new ArrayList<String>(),new ArrayList<Float>());
                updateCurrentImagePathCacheData(currentHolderImagePath,h);
                return true;
            }
        });


    }

    public void recordPoint(float x, float y, ImageView currentHolderImageView, String currentHolderImagePath, RelativeLayout slidingImageRoot) {
        this.x = x;
        this.y = y;
        this.currentHolderImageView = currentHolderImageView;
        this.currentHolderImagePath = currentHolderImagePath;
        this.slidingImageRoot = slidingImageRoot;
    }

    public void showBottomModal() {
        if(bottomBar == null) {
            createBottomBar();
            bottomBar.show();
        }else{
            if(OLD_TAG_BE_CLICKED == false) {
                //说明这是点击一个新的位置而弹出来的
                brandResult.setText("");
                productResult.setText("");
            } else {
                //说明这是点击了一个存在的标签然后对话框弹出来

            }
            bottomBar.show();
        }
    }

    public void dismissBottomModal() {
        if(bottomBar != null) {
            bottomBar.dismiss();
        }
    }

    private void createBottomBar() {
        //全局linearLayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params1 = new LinearLayoutParamsFac().createOnlyWidthMatchParent();
        linearLayout.setLayoutParams(params1);

        //第一行linearLayout：取消 、标题、 保存
        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        params1 = new LinearLayoutParamsFac().createOnlyWidthMatchParent();
        params1.topMargin = 40;
        params1.bottomMargin = 100;
        linearLayout1.setLayoutParams(params1);
        TextView cancelText = new TextView(this);
        cancelText.setText("取消");
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.weight = 1;
        cancelText.setGravity(Gravity.CENTER);
        cancelText.setLayoutParams(params1);
        TextView titleText = new TextView(this);
        titleText.setText("品牌标签");
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.weight = 2;
        titleText.setGravity(Gravity.CENTER);
        titleText.setLayoutParams(params1);
        TextView saveText = new TextView(this);
        saveText.setText("保存");
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.weight = 1;
        saveText.setLayoutParams(params1);
        saveText.setGravity(Gravity.CENTER);

        linearLayout1.addView(cancelText);
        linearLayout1.addView(titleText);
        linearLayout1.addView(saveText);
        linearLayout.addView(linearLayout1);


        //第二行linearLayout：添加商品、商品结果
        RelativeLayout relativeLayout2 = new RelativeLayout(this);
        relativeLayout2.setClickable(true);
        relativeLayout2.setBackgroundColor(Color.LTGRAY);
        RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createOnlyWidthMatchParent();
        params.bottomMargin = 50;
        relativeLayout2.setLayoutParams(params);
        TextView addProductText = new TextView(this);
        addProductText.setClickable(false);
        addProductText.setText("添加商品");
        params = new RelativeLayoutParamsFac().createDoubleContent();
        params.leftMargin = 80;
        addProductText.setLayoutParams(params);
        productResult = new TextView(this);
        productResult.setClickable(false);
        params = new RelativeLayoutParamsFac().createDoubleContent();
        params.addRule(
                RelativeLayout.ALIGN_PARENT_RIGHT
        );
        params.rightMargin = 80;
        productResult.setLayoutParams(params);

        relativeLayout2.addView(addProductText);
        relativeLayout2.addView(productResult);
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HorizontalImagesActivity.this.gotoAddProductActivity();
            }
        });
        linearLayout.addView(relativeLayout2);

        //第三行linearLayout：添加提示信息——或为该商品打自己的标签
        LinearLayout linearLayout4 = new LinearLayout(this);
        linearLayout4.setOrientation(LinearLayout.HORIZONTAL);
        params1 = new LinearLayoutParamsFac().createOnlyWidthMatchParent();
        params1.bottomMargin = 50;
        linearLayout4.setLayoutParams(params1);
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.weight = 1;
        TextView prompt = new TextView(this);
        prompt.setText("或为该图片打自己的标签");
        prompt.setGravity(Gravity.CENTER);
        prompt.setLayoutParams(params1);
        linearLayout4.addView(prompt);
        linearLayout.addView(linearLayout4);

        //第四行linearLayout：添加品牌、品牌结果
        RelativeLayout relativeLayout3 = new RelativeLayout(this);
        relativeLayout3.setBackgroundColor(Color.LTGRAY);
        relativeLayout3.setClickable(true);
        params = new RelativeLayoutParamsFac().createOnlyWidthMatchParent();
        params.bottomMargin = 100;
        relativeLayout3.setLayoutParams(params);
        TextView addBrandText = new TextView(this);
        addBrandText.setClickable(false);
        addBrandText.setText("添加品牌");
        addBrandText.setGravity(Gravity.LEFT);
        params = new RelativeLayoutParamsFac().createDoubleContent();
        params.leftMargin = 80;
        addBrandText.setLayoutParams(params);
        brandResult = new TextView(this);
        brandResult.setClickable(false);
        brandResult.setGravity(Gravity.RIGHT);
        params = new RelativeLayoutParamsFac().createDoubleContent();
        params.addRule(
                RelativeLayout.ALIGN_PARENT_RIGHT
        );
        params.rightMargin = 80;
        brandResult.setLayoutParams(params);

        relativeLayout3.addView(addBrandText);
        relativeLayout3.addView(brandResult);
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HorizontalImagesActivity.this.gotoAddBrandNameActicvity();
            }
        });
        linearLayout.addView(relativeLayout3);


        //添加到一个对话框
        Dialog dialog = new Dialog(HorizontalImagesActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(linearLayout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.horizontalMargin = 0;
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);


        bottomBar = dialog;

        addClickListenerToCancelText(cancelText);
        addClickListenerToSaveText(saveText);
        addClickListenerToAddProductText(addProductText);
        addClickListenerToAddBrandText(addBrandText);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_BRAND_NAME_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    String brandName = data.getStringExtra("brandName");
                    String brandId = data.getStringExtra("brandId");
                    setBrandResultText(brandName,brandId);
                }
                break;
            case ADD_PRODUCT_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    String productName = data.getStringExtra("productName");
                    String productUrl = data.getStringExtra("productUrl");
                    String productId = data.getStringExtra("productId");
                    String brandName = data.getStringExtra("brandName");
                    String brandId = data.getStringExtra("brandId");
                    setProductResultText(productName,productUrl,productId);
                    setBrandResultText(brandName,brandId);
                }
                break;
            default: break;
        }
    }

    private void gotoAddBrandNameActicvity() {
        Intent intent = new Intent(HorizontalImagesActivity.this,AddBrandNameActivity.class);
        intent.putExtra("flag","brandNameBack");
        startActivityForResult(intent,ADD_BRAND_NAME_REQUEST_CODE);
    }

    private void gotoAddProductActivity() {
        Intent intent = new Intent(HorizontalImagesActivity.this,AddProductActivity.class);
        intent.putExtra("flag","productNameBack");
        startActivityForResult(intent,ADD_PRODUCT_REQUEST_CODE);
    }

    private void addClickListenerToCancelText(TextView cancelText) {
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissBottomModal();
            }
        });
    }

    private ArrayList replaceTagNamesInCacheData(ArrayList tagNames,String newValue) {
        for(int i=0; i<tagNames.size(); i++) {
            if (tagNames.get(i).equals(oldBrandResultText)) {
                tagNames.set(i,newValue);
                break;
            }
        }
        return tagNames;
    }

    private void addClickListenerToSaveText(TextView saveText) {
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brandWrittenText = (String) brandResult.getText();
                String brandResultText = (String) brandResult.getTag();

                if(OLD_TAG_BE_CLICKED && !oldBrandResultText.equals(brandResultText)) { //说明是修改现存的标签
                    //如果brandResultText更原来的不一样，那么数据就要进行替换了
                    HorizontalImagesWithTags horizontalImagesWithTags = getCurrentImagePathCacheData(currentHolderImagePath);
                    horizontalImagesWithTags.tagNames = replaceTagNamesInCacheData(horizontalImagesWithTags.tagNames,brandResultText);
                    updateCurrentImagePathCacheData(currentHolderImagePath,horizontalImagesWithTags);
                    currentBeingModifiedExistedTag.setText(brandWrittenText);
                }
                if(!OLD_TAG_BE_CLICKED && brandWrittenText != "" && brandResultText != "") {
                    // 其实主要是保证实际brandResult有没有填了字
                    putResultTextOntoCurrentImage(brandResultText);
                    String url = "";
                    updateImagesTagCacheData(currentHolderImagePath, brandResultText, x/currentHolderImageView.getWidth(), y/currentHolderImageView.getHeight(), url);
                }
                bottomBar.dismiss();
            }
        });
    }

    private void addClickListenerToAddProductText(TextView addProductText) {
        addProductText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void addClickListenerToAddBrandText(TextView addBrandText) {
        addBrandText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void cutTheImage(int realImageWidth, int realImageHeight, File imageFile, final ImageView imageview, final int position, final RelativeLayout slidingImagesRoot) {

        final float recycleViewWidthToHeightRatio = horizontalImagesScreenRecycleView.getWidth() / horizontalImagesScreenRecycleView.getHeight();
        final float realImageWidthToHeightRatio = realImageWidth / realImageHeight;

        float usedWidth;
        float usedHeight;

        if(realImageWidthToHeightRatio < recycleViewWidthToHeightRatio) {
            // 如果实际图片宽高比小于屏幕提供显示图片的宽高比，则说明图片高度过高，直接将其截成1：1
            usedWidth = horizontalImagesScreenRecycleView.getWidth();
            usedHeight = usedWidth;

        } else {
            // 这种情况就是屏幕提供的内容区可以将该图片显示完整
            usedWidth = horizontalImagesScreenRecycleView.getWidth();
            usedHeight = usedWidth / realImageWidthToHeightRatio;
        }
        final HorizontalImagesActivity this_ = this;

        imageview.setTag(usedWidthNumber,usedWidth);
        imageview.setTag(usedHeightNumber,usedWidth);

        Picasso.with(this)
                .load(imageFile)
                .placeholder(R.mipmap.ic_launcher) // optional
                .resize((int)usedWidth, (int)usedHeight)
                .error(R.mipmap.ic_launcher) //if error
                .into(imageview);

        imageview.setOnTouchListener(new View.OnTouchListener() {
            float x2 = 0;
            float y2 = 0;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();
                        this_.recordPoint(x2,y2,imageview,horizontalImages.get(position),slidingImagesRoot);
                        OLD_TAG_BE_CLICKED = false;
                        this_.showBottomModal();
                        break;
                    default: break;
                }
                return true;
            }
        });

    }

    public void setProductResultText(String productName, String productUrl, String productId) {
        if(!productName.equals("noProductName")) productResult.setText(productName);
        productResult.setTag(";ashaSeperator;" + productName + ":" + productUrl + ":" + productId);
    }

    public void setBrandResultText(String brandName, String brandId) {
        brandResult.setText(brandName);
        if(brandResult.getText() != "") {
            if(productResult.getText() != "") {
                brandResult.setTag(brandName+productResult.getTag()+":"+brandId);
            }else{
                //当商品没有选的时候，数据就是长这样的
                brandResult.setTag(brandName+";ashaSeperator;noProductName:noProductUrl:noProductId:"+brandId);
            }

        }
    }

    private void setThisTimeCurrentEnvironment(String currentHolderImagePath,ImageView currentHolderImageView) {
        this.currentHolderImagePath = currentHolderImagePath;
        this.currentHolderImageView = currentHolderImageView;
    }

    private void putResultTextOntoCurrentImage(final String brandResultText) {
        //在当前的ImageView下创造一个"标签"TextView,然后将这个"标签"TextView写在指定的图片位置
        int randomInt = (int )(Math. random() * 1444450 + 1); // 足够大保证不重复
        if(brandResultText != "") {
            final RelativeLayout relativeLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams params1 = new RelativeLayoutParamsFac().createDoubleContent();
            if(currentHolderImageView.getWidth() - x < 300) {
                x = x - 300;
            }
            if(currentHolderImageView.getHeight() - y < 300) {
                y = y - 300;
            }
            params1.leftMargin = (int) x;
            params1.topMargin = (int) y;
            relativeLayout.setLayoutParams(params1);
            params1 = new RelativeLayoutParamsFac().createDoubleContent();
            final TextView tagBrandText = new TextView(this);
            String textForDisplay = brandResultText.split(";ashaSeperator;")[0];
            tagBrandText.setText(textForDisplay);
            tagBrandText.setTag(brandResultText);
            tagBrandText.setId(randomInt);
            tagBrandText.setTextSize(14);
            tagBrandText.setLayoutParams(params1);

            //为这个"标签"TextView加"删除"TextView
            params1 = new RelativeLayoutParamsFac().createDoubleContent();
            final TextView deleteText1 = new TextView(this);
            deleteText1.setText("X");
            deleteText1.setTextColor(Color.BLUE);
            deleteText1.setTextSize(14);
            params1.addRule(
                    RelativeLayout.RIGHT_OF,
                    tagBrandText.getId()
            );
            params1.leftMargin = 4;
            deleteText1.setLayoutParams(params1);

            //为"删除"TextView加点击事件，删除"标签"和"删除"这两个TextView
            deleteText1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //加监听的时候不能直接用上面的数据,不知道为什么,就先调取parent吧
                    RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
                    RelativeLayout slidingImageRoot = (RelativeLayout) relativeLayout.getParent();
                    deleteImagesTagCacheData(currentHolderImagePath,brandResultText);
                    slidingImageRoot.removeView(relativeLayout);
                }
            });

            relativeLayout.addView(deleteText1);
            relativeLayout.addView(tagBrandText);
            relativeLayout.setTag("被拖动的标签");
            slidingImageRoot.addView(relativeLayout);

            Log.d("11","11111111");

            //为"标签"TextView加拖动事件，仅允许在当前ImageView的四维界限活动

                //拿到当前图片的最大x坐标和最大y坐标

                final float currentImageMinX = currentHolderImageView.getX();
                final float currentImageMinY = (slidingImageRoot.getHeight() - (float)currentHolderImageView.getTag(usedHeightNumber)) / 2;
                final float currentImageMaxX = currentImageMinX + currentHolderImageView.getWidth();
                final float currentImageMaxY = currentImageMinY + (float)currentHolderImageView.getTag(usedHeightNumber);

                //对标签做点击监听
                tagBrandText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomModal();
                        // 抽出之前在tagBrandText写进去的旧数据
                        oldBrandResultText = (String)tagBrandText.getTag();
                        OLD_TAG_BE_CLICKED = true;
                        currentBeingModifiedExistedTag = (TextView) v;
                        setThisTimeCurrentEnvironment(currentHolderImagePath,currentHolderImageView);
                        String[] brandInfoWrappers = oldBrandResultText.split(";ashaSeperator;");
                        String[] productInfoWrappers = brandInfoWrappers[1].split(":");
                        String brandName = brandInfoWrappers[0];
                        String productName = productInfoWrappers[0];
                        String productUrl = productInfoWrappers[1];
                        String productId = productInfoWrappers[2];
                        String brandId = productInfoWrappers[3];
                        setProductResultText(productName,productUrl,productId);
                        setBrandResultText(brandName,brandId);
                    }
                });

                //加拖动监听
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        switch(e.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
                                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                                ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
                                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
                                v.setVisibility(View.INVISIBLE);
                                v.startDrag(dragData,myShadow,v,0);
                                break;
                            default: break;
                        }
                        return true;
                    }
                } );

                currentHolderImageView.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        int action = event.getAction();

                        switch (action) {

                            case DragEvent.ACTION_DRAG_STARTED:
                                // Determines if this View can accept the dragged data
                                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                                    // if you want to apply color when drag started to your view you can uncomment below lines
                                    // to give any color tint to the View to indicate that it can accept data.
                                    // v.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                                    // Invalidate the view to force a redraw in the new tint
                                    //  v.invalidate();
                                    // returns true to indicate that the View can accept the dragged data.
                                    return true;
                                }
                                // Returns false. During the current drag and drop operation, this View will
                                // not receive events again until ACTION_DRAG_ENDED is sent.
                                return false;

                            case DragEvent.ACTION_DRAG_ENTERED:
                                return true;

                            case DragEvent.ACTION_DRAG_LOCATION:
                                // Ignore the event

                                return true;

                            case DragEvent.ACTION_DRAG_EXITED:
                                relativeLayout.setVisibility(View.VISIBLE);
                                return true;

                            case DragEvent.ACTION_DROP:

                                View vw = (View) event.getLocalState();

                                if((event.getX() + vw.getWidth()) < currentImageMaxX &&
                                        event.getX() >= currentImageMinX &&
                                        (event.getY() + vw.getHeight())  < currentImageMaxY &&
                                        event.getY() >= currentImageMinY) {
                                    // Gets the item containing the dragged data
                                    ClipData.Item item = event.getClipData().getItemAt(0);
                                    // Gets the text data from the item.
                                    String dragData = item.getText().toString();

                                    //cast the view into LinearLayout as our drag acceptable layout is LinearLayout

                                    RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createDoubleContent();
                                    params.leftMargin = (int) event.getX();
                                    params.topMargin = (int) event.getY();
                                    vw.setLayoutParams(params);
                                    updateImagesTagCacheDataAfterDrag(currentHolderImagePath,event.getX()/currentHolderImageView.getWidth(),event.getY()/currentHolderImageView.getHeight(), brandResultText);
                                }
                                vw.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                                // Returns true. DragEvent.getResult() will return true.
                                return true;
                            case DragEvent.ACTION_DRAG_ENDED:
                                return true;
                            // An unknown action type was received.
                            default:
                                break;
                        }
                        return true;
                    }
                });
        }
        //点击保存后的最后一步是关掉这个对话窗口
        if(bottomBar != null)  bottomBar.dismiss();
    }

}
