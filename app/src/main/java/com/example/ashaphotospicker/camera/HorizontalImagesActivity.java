package com.example.ashaphotospicker.camera;

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

    @Override
    protected void onResume() {
        super.onResume();
        if(multiImageSelectorActivityImagesPathCacheSharedPreferences.contains("imagesPath") && multiImageSelectorActivityImagesPathCacheSharedPreferences.getString("imagesPath","").length() > 0) {
            if(this.horizontalImages.size() > 0) {
                this.horizontalImages.clear();
            }
            ArrayList<String> horizontalImages_ = gson.fromJson(multiImageSelectorActivityImagesPathCacheSharedPreferences.getString("imagesPath",""), ArrayList.class);
            this.horizontalImages.addAll(horizontalImages_);
            this.horizontalImagesAdapter.notifyDataSetChanged();
            Log.d(TAG,"gson.fromJson(sharedPreferences.getString(\"imagesPath\",\"\"), ArrayList.class) " + horizontalImages);
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
            Log.d(TAG,"gson.fromJson(sharedPreferences.getString(\"imagesPath\",\"\"), ArrayList.class) " + horizontalImagesWithTags);
        }
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
        final SharedPreferences.Editor editor = multiImageSelectorActivityImagesWithTagsSharedPreferences.edit();
        Log.d(TAG,"updateImagesTagCacheData: " + horizontalImagesWithTags);
        editor.putString("ImagesWithTags", gsonBuilder.toJson(horizontalImagesWithTags));
        editor.commit();
    }

    private void deleteImagesTagCacheData(String imagePath, String text) {
        if(horizontalImagesWithTags.get(imagePath) != null) {
            String HorizontalImagesWithTagsToString = gson.toJson(horizontalImagesWithTags.get(imagePath));
            HorizontalImagesWithTags foo = gson.fromJson(HorizontalImagesWithTagsToString,HorizontalImagesWithTags.class);
            for(int i=0; i<foo.tagNames.size(); i++) {
                if(foo.tagNames.get(i).equals(text) ) {
                    Log.d(TAG,"delete brandNames: " + foo.tagNames);
                    Log.d(TAG,"delete brandNames: " + foo.tagPoints);
                    Log.d(TAG,"delete brandNames: " + String.valueOf(i));
                    Log.d(TAG,"delete brandNames: " + String.valueOf(i*2));
                    Log.d(TAG,"delete brandNames: " + String.valueOf(i*2 + 1));
                    foo.tagNames.remove(i);
                    foo.tagPoints.remove(i*2);
                    foo.tagPoints.remove(i*2); // 因为移除第一个后，第二个的位置就降下跟x坐标一样了，不这样写就会报溢出错误了
                    Log.d(TAG,"delete brandNames: " + foo.tagNames);
                    Log.d(TAG,"delete brandNames: " + foo.tagPoints);
                    break;
                }
            }
            horizontalImagesWithTags.put(imagePath,foo);
            final SharedPreferences.Editor editor = multiImageSelectorActivityImagesWithTagsSharedPreferences.edit();
            Log.d(TAG,"delete brandNames: " + horizontalImagesWithTags);
            editor.putString("ImagesWithTags", gsonBuilder.toJson(horizontalImagesWithTags));
            editor.commit();
        }
    }

    private void finalUpdateImagesTagCacheData() {
        final SharedPreferences.Editor editor = multiImageSelectorActivityImagesWithTagsSharedPreferences.edit();
        editor.putString("ImagesWithTags", gsonBuilder.toJson(horizontalImagesWithTags));
        Log.d(TAG,"gsonBuilder.toJson(ImagesWithTags) " + gsonBuilder.toJson(horizontalImagesWithTags));
        editor.commit();
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
        new PagerSnapHelper().attachToRecyclerView(horizontalImagesScreenRecycleView);


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
        linearLayout1.setBackgroundColor(Color.BLUE);
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
        cancelText.setBackgroundColor(Color.RED);
        TextView titleText = new TextView(this);
        titleText.setText("品牌标签");
        titleText.setBackgroundColor(Color.BLUE);
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.weight = 2;
        titleText.setGravity(Gravity.CENTER);
        titleText.setLayoutParams(params1);
        TextView saveText = new TextView(this);
        saveText.setText("保存");
        saveText.setBackgroundColor(Color.GRAY);
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.weight = 1;
        saveText.setLayoutParams(params1);
        saveText.setGravity(Gravity.CENTER);

        linearLayout1.addView(cancelText);
        linearLayout1.addView(titleText);
        linearLayout1.addView(saveText);
        linearLayout.addView(linearLayout1);


        //第二行linearLayout：添加商品、商品结果
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setBackgroundColor(Color.RED);
        params1 = new LinearLayoutParamsFac().createOnlyWidthMatchParent();
        params1.bottomMargin = 50;
        linearLayout2.setLayoutParams(params1);
        TextView addProductText = new TextView(this);
        addProductText.setText("添加商品");
        addProductText.setGravity(Gravity.LEFT);
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.leftMargin = 40;
        params1.weight = 1;
        addProductText.setLayoutParams(params1);
        productResult = new TextView(this);
        productResult.setGravity(Gravity.RIGHT);
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.rightMargin = 40;
        params1.weight = 1;
        productResult.setGravity(Gravity.RIGHT);
        productResult.setLayoutParams(params1);

        linearLayout2.addView(addProductText);
        linearLayout2.addView(productResult);
        linearLayout.addView(linearLayout2);

        //第三行linearLayout：添加提示信息——或为该商品打自己的标签
        LinearLayout linearLayout4 = new LinearLayout(this);
        linearLayout4.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout4.setBackgroundColor(Color.GRAY);
        params1 = new LinearLayoutParamsFac().createOnlyWidthMatchParent();
        params1.bottomMargin = 50;
        linearLayout4.setLayoutParams(params1);
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.weight = 1;
        TextView prompt = new TextView(this);
        prompt.setText("或为该商品打自己的标签");
        prompt.setGravity(Gravity.CENTER);
        prompt.setLayoutParams(params1);
        linearLayout4.addView(prompt);
        linearLayout.addView(linearLayout4);

        //第四行linearLayout：添加品牌、品牌结果
        LinearLayout linearLayout3 = new LinearLayout(this);
        linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout3.setBackgroundColor(Color.GREEN);
        params1 = new LinearLayoutParamsFac().createOnlyWidthMatchParent();
        params1.bottomMargin = 100;
        linearLayout3.setLayoutParams(params1);
        TextView addBrandText = new TextView(this);
        addBrandText.setText("添加品牌");
        addBrandText.setGravity(Gravity.LEFT);
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.leftMargin = 40;
        params1.weight = 1;
        addBrandText.setLayoutParams(params1);
        brandResult = new TextView(this);
        brandResult.setGravity(Gravity.RIGHT);
        params1 = new LinearLayoutParamsFac().createDoubleContent();
        params1.rightMargin = 40;
        params1.weight = 1;
        brandResult.setLayoutParams(params1);

        linearLayout3.addView(addBrandText);
        linearLayout3.addView(brandResult);
        linearLayout.addView(linearLayout3);


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

    private void addClickListenerToCancelText(TextView cancelText) {
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissBottomModal();
            }
        });
    }

    private void addClickListenerToSaveText(TextView saveText) {
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProductResultText("商品结果");
                setBrandResultText("品牌结果");
                String brandResultText = (String) brandResult.getText();
                putResultTextOntoCurrentImage(brandResultText);
                String url = "";
                updateImagesTagCacheData(currentHolderImagePath, brandResultText, x/currentHolderImageView.getWidth(), y/currentHolderImageView.getHeight(), url);
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

    public void setProductResultText(String text) {
        productResult.setText(text);
    }

    public void setBrandResultText(String text) {
        brandResult.setText(text);
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

            int TAG_BRAND_ID = randomInt;
            params1 = new RelativeLayoutParamsFac().createDoubleContent();
            final TextView tagBrandText = new TextView(this);
            tagBrandText.setText(brandResultText);
            tagBrandText.setId(TAG_BRAND_ID);
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
                    TAG_BRAND_ID
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
            slidingImageRoot.addView(relativeLayout);

            relativeLayout.setTag("被移动的标签名");

            //为"标签"TextView加拖动事件，仅允许在当前ImageView的四维界限活动

                //拿到当前图片的最大x坐标和最大y坐标
                final float currentImageMaxX = currentHolderImageView.getX() + currentHolderImageView.getWidth();
                final float currentImageMaxY = currentHolderImageView.getY() + currentHolderImageView.getHeight();
                relativeLayout.setBackgroundColor(Color.BLACK);
                //加拖动监听
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        if (e.getAction() == MotionEvent.ACTION_DOWN) {
                            ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
                            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                            ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
                            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
                            v.setVisibility(View.INVISIBLE);
                            v.startDrag(dragData,myShadow,v,0);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                slidingImageRoot.setOnDragListener(new View.OnDragListener() {
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
                                if((event.getX() + relativeLayout.getWidth()) < currentImageMaxX &&
                                        event.getX() >= 0 &&
                                        (event.getY() + relativeLayout.getHeight())  < currentImageMaxY &&
                                        event.getY() >= 0) {
                                    // Gets the item containing the dragged data
                                    ClipData.Item item = event.getClipData().getItemAt(0);
                                    // Gets the text data from the item.
                                    String dragData = item.getText().toString();



                                    //caste the view into LinearLayout as our drag acceptable layout is LinearLayout


                                    RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createDoubleContent();
                                    params.leftMargin = (int) event.getX();
                                    params.topMargin = (int) event.getY();
                                    vw.setLayoutParams(params);

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
