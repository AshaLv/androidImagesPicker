package com.example.ashaphotospicker.camera;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
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
import com.example.ashaphotospicker.R;

import java.util.ArrayList;
import java.util.List;

import com.example.ashaphotospicker.camera.adapter.HorizontalImagesAdapter;

import org.w3c.dom.Text;

public class HorizontalImagesActivity extends AppCompatActivity {
    private static final String TAG = "HorizontalImages";

    private RecyclerView horizontalImagesScreenRecycleView;
    private HorizontalImagesAdapter horizontalImagesAdapter;
    private LinearLayoutManager layoutManager;

    private Dialog bottomBar = null;
    private LinearLayout root;
    private ArrayList<String> horizontalImages;

    private TextView productResult;
    private TextView brandResult;

    private float x;
    private float y;
    private ImageView currentHolderImageView;
    private String currentHolderImagePath;
    private RelativeLayout slidingImageRoot;
    RelativeLayout.LayoutParams params_;

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

    public void recordPoint(float x, float y, ImageView currentHolderImageView, String currentHolderImagePath, RelativeLayout slidingImageRoot) {
        Log.d(TAG,"recording the position information now");
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
                Log.d(TAG,"cancel text clicked");
                dismissBottomModal();
            }
        });
    }

    private void addClickListenerToSaveText(TextView saveText) {
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"save text clicked");
                setProductResultText("商品结果");
                setBrandResultText("品牌结果");
                putResultTextOntoCurrentImage();
            }
        });
    }

    private void addClickListenerToAddProductText(TextView addProductText) {
        addProductText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"addProduct text clicked");
            }
        });
    }

    private void addClickListenerToAddBrandText(TextView addBrandText) {
        addBrandText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"addBrand text clicked");
            }
        });
    }

    public void setProductResultText(String text) {
        productResult.setText(text);
    }

    public void setBrandResultText(String text) {
        brandResult.setText(text);
    }

    private void putResultTextOntoCurrentImage() {
        Log.d(TAG,"we are in position (" + String.valueOf(x) + "," + String.valueOf(y)  + ")"
            + "\nand the productResult is: " + productResult.getText() + "\nthe brandResult is: " + brandResult.getText()
            + "\nand current image view is: " + currentHolderImageView
            + "\nand current holder image path is: " + currentHolderImagePath);

//        Bitmap mutableBitmap = currentImageBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(mutableBitmap);
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(10);
//        canvas.drawText(productResult.getText().toString(),x,y,paint);


        //在当前的ImageView下创造一个"标签"TextView,然后将这个"标签"TextView写在指定的图片位置
        int randomInt = (int )(Math. random() * 1444450 + 1); // 足够大保证不重复
        if(brandResult.getText() != "") {
            final RelativeLayout relativeLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams params1 = new RelativeLayoutParamsFac().createDoubleContent();
            params1.leftMargin = (int) x;
            params1.topMargin = (int) y;
            relativeLayout.setLayoutParams(params1);

            int TAG_BRAND_ID = randomInt;
            params1 = new RelativeLayoutParamsFac().createDoubleContent();
            final TextView tagBrandText = new TextView(this);
            tagBrandText.setText(brandResult.getText());
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
                    Log.d(TAG,"here to delete brand name");
                    slidingImageRoot.removeView(deleteText1);
                    slidingImageRoot.removeView(tagBrandText);
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
                        Log.d(TAG,"onTouch");
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
                                        event.getX() > 1 &&
                                        (event.getY() + relativeLayout.getHeight())  < currentImageMaxY &&
                                        event.getY() > 1) {
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
                                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                                break;
                        }
                        return true;
                    }
                });
        }


        //点击保存后的最后一步是关掉这个对话窗口
        bottomBar.dismiss();

    }



}
