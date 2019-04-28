package com.example.ashaphotospicker.camera;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ashaphotospicker.AshaConstant.ColorConstant;
import com.example.ashaphotospicker.AshaConstant.SizeConstant;
import com.example.ashaphotospicker.AshaFactory.LinearLayoutParamsFac;
import com.example.ashaphotospicker.AshaFactory.RelativeLayoutParamsFac;
import com.example.ashaphotospicker.AshaHelper.PositionHelper;
import com.example.ashaphotospicker.AshaHelper.SharedPreferenceHelper;
import com.example.ashaphotospicker.AshaInteractor.CircleInteractor;
import com.example.ashaphotospicker.AshaModel.Circle;
import com.example.ashaphotospicker.AshaModel.Product;
import com.example.ashaphotospicker.R;
import com.example.ashaphotospicker.camera.bean.Image;
import com.example.ashaphotospicker.camera.bean.ImageTagsCache;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;

public class PostUGCActivity extends SharedPreferenceHelper implements CircleInteractor.CircleActivity {

    private final int IMAGES_PATH_TYPE = 3;
    private final int CIRCLES_NAME_TYPE = 33;
    private final int PRODUCTS_TYPE = 4323;

    private final int IMAGE_RECYCLE_VIEW_CHILD_ID = 325;
    private final int CIRCLE_TEXT_RECYCLE_VIEW_CHILD_ID =56;
    private final int PRODUCT_RECYCLE_IMAGE_ID = 63453;
    private final int PRODUCT_RECYCLE_NAME_ID = 636;
    private final int PRODUCT_RECYCLE_PRICE_ID = 5336;

    private ScrollView scrollView;
    private LinearLayout scrollViewChild;
    private LinearLayout rootView;
    private RelativeLayout navigationBar;
    private RecyclerView imagesRecycleView;
    private PostUgcAdapter imagesRecycleViewAdapter;
    private EditText editText;
    private RecyclerView circlesRecycleView;
    private PostUgcAdapter circlesRecycleViewAdapter;
    private RecyclerView productsRecycleView;
    private PostUgcAdapter productsRecycleViewAdapter;

    private ArrayList<String> imagesPath;
    private ArrayList<String> circlesName;
    private ArrayList<Product> products;
    private Dictionary<String, ImageTagsCache> allData;
    private ArrayList<String> allPath;

    private ViewBuilder viewBuilder;

    private CircleInteractor circleInteractor;

    @Override
    public void setNavigationBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SizeConstant.getSingletonMetrics(PostUGCActivity.this);
        viewBuilder = new ViewBuilder();
        circleInteractor = new CircleInteractor();
        allData = this.getCachedImagesDataWithTags();
        allPath = this.getCachedImagesPath();

        setNavigationBar();

        rootView = viewBuilder.makeRootView();
        setContentView(rootView);
        navigationBar = viewBuilder.makeNavigationBar();

        imagesRecycleView = viewBuilder.makeImagesRecycleView();
        circlesRecycleView = viewBuilder.makeCirclesRecycleView();
        editText = viewBuilder.makeEditText();
        productsRecycleView = viewBuilder.makeProductsRecycleView();

        imagesRecycleViewAdapter = getImagesRecycleViewAdapter();
        circleInteractor.setActivity(this);
        productsRecycleViewAdapter = getProductsRecycleViewAdapter();

        imagesRecycleView.setAdapter(imagesRecycleViewAdapter);
        productsRecycleView.setAdapter(productsRecycleViewAdapter);

        scrollView = viewBuilder.makeScrollView();
        scrollViewChild = viewBuilder.makeScrollViewChild();

        rootView.addView(navigationBar);
        rootView.addView(scrollView);
        scrollView.addView(scrollViewChild);
        scrollViewChild.addView(imagesRecycleView);
        scrollViewChild.addView(editText);
        scrollViewChild.addView(productsRecycleView);
        circleInteractor.getUserRelatedCircles("userId");
    }

    private PostUgcAdapter getImagesRecycleViewAdapter() {
        PostUgcAdapter postUgcAdapter;
        if(allPath != null && allPath.size()>0) {
            postUgcAdapter = new PostUgcAdapter(allPath,IMAGES_PATH_TYPE);
            return postUgcAdapter;
        }else {
            postUgcAdapter = new PostUgcAdapter(new ArrayList(),IMAGES_PATH_TYPE);
            return postUgcAdapter;
        }
    }

    private void getCirclesRecycleViewAdapter(ArrayList<Circle> circles) {
        if(circles == null) circles = new ArrayList<>();
        PostUgcAdapter postUgcAdapter;
        postUgcAdapter = new PostUgcAdapter(circles,CIRCLES_NAME_TYPE);
        circlesRecycleViewAdapter = postUgcAdapter;
        circlesRecycleView.setAdapter(circlesRecycleViewAdapter);
        scrollViewChild.addView(circlesRecycleView,2);
    }

    private PostUgcAdapter getProductsRecycleViewAdapter() {
        PostUgcAdapter postUgcAdapter;
        ArrayList<Product> productsDataWrapper = new ArrayList<>();
        if(allData != null && allData.size()>0) {
            for(int i=0; i<allPath.size(); i++) {
                ImageTagsCache imageTagsCache = this.getCachedImageTags(allPath.get(i));
                ArrayList<String> productsInfo = imageTagsCache.tagNames;
                // 用;ashaSeparator;做品牌名称的分割线，例子"tagName;ashaSperator;ProductName:ProductUrl:ProductId:BrandId:productPrice
                //如果没有产品相关联,则ProductName叫做noProductName
                for(int j=0; j<productsInfo.size(); j++) {
                    String productStringInfo = productsInfo.get(j).split(";ashaSeperator;")[1];
                    String[] productInfo = productStringInfo.split(":");
                    String productName = productInfo[0];
                    if(!productName.equals("noProductName")) {
                        String ProductUrl = productInfo[1];
                        String productPrice = productInfo[4];
                        Product product = new Product(ProductUrl,productName,productPrice);
                        productsDataWrapper.add(product);
                    }
                }
            }
        }
        postUgcAdapter = new PostUgcAdapter(productsDataWrapper,PRODUCTS_TYPE);
        return postUgcAdapter;
    }

    @Override
    public void didGetTheCircles(ArrayList<Circle> circles) {
        getCirclesRecycleViewAdapter(circles);
    }

    class ViewBuilder {

        public ViewBuilder() {

        }

        public ScrollView makeScrollView() {
            ScrollView scrollView = new ScrollView(PostUGCActivity.this);
            scrollView.canScrollVertically(ScrollView.SCROLL_AXIS_VERTICAL);
            return scrollView;
        }

        public LinearLayout makeScrollViewChild() {
            LinearLayout.LayoutParams params = LinearLayoutParamsFac.getSingleton().createDoubleParent();

            LinearLayout scrollViewChild = new LinearLayout(PostUGCActivity.this);
            scrollViewChild.setOrientation(LinearLayout.VERTICAL);

            scrollViewChild.setLayoutParams(params);

            return scrollViewChild;
        }

        public LinearLayout makeRootView() {
            LinearLayout.LayoutParams params = LinearLayoutParamsFac.getSingleton().createDoubleParent();

            LinearLayout rootView = new LinearLayout(PostUGCActivity.this);
            rootView.setOrientation(LinearLayout.VERTICAL);

            rootView.setLayoutParams(params);

            return rootView;
        }

        public RelativeLayout makeNavigationBar() {
            LinearLayout.LayoutParams params1 = LinearLayoutParamsFac.getSingleton().createOnlyWidthMatchParent();
            params1.topMargin = SizeConstant.getPixel(20);
            params1.bottomMargin = SizeConstant.getPixel(30);
            params1.leftMargin = SizeConstant.getPixel(30);
            params1.rightMargin = SizeConstant.getPixel(30);
            RelativeLayout relativeLayout = new RelativeLayout(PostUGCActivity.this);
            relativeLayout.setMinimumHeight(SizeConstant.getPixel(100));
            relativeLayout.setLayoutParams(params1);

            final int CANCEL_ID = 324;
            final int TITLE_ID = 523;
            final int POST_ID = 532;

            //取消发布
            RelativeLayout.LayoutParams params = RelativeLayoutParamsFac.getSingleton().createDoubleContent();
            TextView cancelTextView = new TextView(PostUGCActivity.this);
            cancelTextView.setText("X");
            cancelTextView.setId(CANCEL_ID);
            cancelTextView.setTextColor(ColorConstant.blackColor);
            cancelTextView.setTextSize(SizeConstant.getSpPixel(30));
            cancelTextView.setLayoutParams(params);
            relativeLayout.addView(cancelTextView);
            //标题
            params = RelativeLayoutParamsFac.getSingleton().createDoubleContent();
            PositionHelper.right(params,CANCEL_ID,SizeConstant.getPixel(237));
            TextView titleTextView = new TextView(PostUGCActivity.this);
            titleTextView.setText("发布动态");
            titleTextView.setId(TITLE_ID);
            titleTextView.setTextColor(ColorConstant.blackColor);
            titleTextView.setTextSize(SizeConstant.getSpPixel(30));
            titleTextView.setLayoutParams(params);
            relativeLayout.addView(titleTextView);
            //发送
            params = RelativeLayoutParamsFac.getSingleton().createDoubleContent();
            PositionHelper.right(params,TITLE_ID,SizeConstant.getPixel(237));
            TextView postTextView = new TextView(PostUGCActivity.this);
            postTextView.setText("发送");
            postTextView.setId(POST_ID);
            postTextView.setTextColor(ColorConstant.annotationColor);
            postTextView.setTextSize(SizeConstant.getSpPixel(30));
            postTextView.setLayoutParams(params);
            relativeLayout.addView(postTextView);

            return relativeLayout;
        }

        public RecyclerView makeImagesRecycleView() {
            LinearLayout.LayoutParams params = LinearLayoutParamsFac.getSingleton().createOnlyWidthMatchParent();
            params.leftMargin = SizeConstant.getPixel(30);
            params.rightMargin = SizeConstant.getPixel(30);
            RecyclerView imagesRecycleView = new RecyclerView(PostUGCActivity.this);
            imagesRecycleView.setLayoutManager(new GridLayoutManager(PostUGCActivity.this,4));
            imagesRecycleView.setHasFixedSize(true);

            imagesRecycleView.setLayoutParams(params);

            return imagesRecycleView;
        }

        public EditText makeEditText() {
            LinearLayout.LayoutParams params = LinearLayoutParamsFac.getSingleton().createOnlyWidthMatchParent();
            params.width = SizeConstant.getPixel(690);
            params.leftMargin = SizeConstant.getPixel(30);
            params.topMargin = SizeConstant.getPixel(24);
            params.bottomMargin = SizeConstant.getPixel(160);
            EditText editText = new EditText(PostUGCActivity.this);
            editText.setHint("我想说......");
            editText.setTextSize(SizeConstant.getSpPixel(28));
            editText.setSingleLine(false);
            editText.setBackgroundResource(android.R.color.transparent);

            editText.setLayoutParams(params);

            return editText;
        }

        public RecyclerView makeCirclesRecycleView() {
            LinearLayout.LayoutParams params = LinearLayoutParamsFac.getSingleton().createOnlyWidthMatchParent();
            params.leftMargin = SizeConstant.getPixel(35);
            RecyclerView circlesRecycleView = new RecyclerView(PostUGCActivity.this);
            circlesRecycleView.setLayoutManager(new GridLayoutManager(PostUGCActivity.this, 5));
            circlesRecycleView.setHasFixedSize(true);

            circlesRecycleView.setLayoutParams(params);

            return circlesRecycleView;
        }

        public RecyclerView makeProductsRecycleView() {
            LinearLayout.LayoutParams params = LinearLayoutParamsFac.getSingleton().createOnlyWidthMatchParent();
            params.leftMargin = SizeConstant.getPixel(24);
            RecyclerView productsRecycleView = new RecyclerView(PostUGCActivity.this);
            productsRecycleView.setLayoutManager(new LinearLayoutManager(PostUGCActivity.this, LinearLayout.HORIZONTAL,false));
            productsRecycleView.setHasFixedSize(true);

            productsRecycleView.setLayoutParams(params);

            return productsRecycleView;
        }

        public RelativeLayout makeImagesAdapterView(Context context) {

            RelativeLayout.LayoutParams params1 = RelativeLayoutParamsFac.getSingleton().createDoubleContent();
            params1.leftMargin = SizeConstant.getPixel(10);

            RelativeLayout relativeLayout = new RelativeLayout(context);

            ImageView imageView = new ImageView(context);
            imageView.setId(IMAGE_RECYCLE_VIEW_CHILD_ID);
            relativeLayout.addView(imageView);

            imageView.setLayoutParams(params1);

            return relativeLayout;
        }

        public RelativeLayout makeCirclesAdapterView(Context context) {

            RelativeLayout.LayoutParams params1 = RelativeLayoutParamsFac.getSingleton().createDoubleContent();
            params1.leftMargin = SizeConstant.getPixel(20);
            params1.bottomMargin = SizeConstant.getPixel(200);

            RelativeLayout relativeLayout = new RelativeLayout(context);

            TextView textView = new TextView(context);
            textView.setId(CIRCLE_TEXT_RECYCLE_VIEW_CHILD_ID);
            relativeLayout.addView(textView);

            textView.setLayoutParams(params1);

            return relativeLayout;
        }

        public RelativeLayout makeProductsAdapterView(Context context) {

            RelativeLayout.LayoutParams params1 = RelativeLayoutParamsFac.getSingleton().createDoubleContent();
            RelativeLayout.LayoutParams params2 = RelativeLayoutParamsFac.getSingleton().createDoubleContent();
            RelativeLayout.LayoutParams params3 = RelativeLayoutParamsFac.getSingleton().createDoubleContent();

            RelativeLayout relativeLayout = new RelativeLayout(context);

            ImageView productImage = new ImageView(context);
            productImage.setId(PRODUCT_RECYCLE_IMAGE_ID);

            TextView productName = new TextView(context);
            productName.setId(PRODUCT_RECYCLE_NAME_ID);

            TextView productPrice = new TextView(context);
            productPrice.setId(PRODUCT_RECYCLE_PRICE_ID);

            relativeLayout.addView(productImage);
            relativeLayout.addView(productName);
            relativeLayout.addView(productPrice);

            productImage.setLayoutParams(params1);
            productName.setLayoutParams(params2);
            productPrice.setLayoutParams(params3);

            return relativeLayout;
        }

    }

    class PostUgcAdapter extends RecyclerView.Adapter<PostUgcAdapter.ViewHolder> {

        private ArrayList<String> imagesPath;
        private ArrayList<Circle> circles;
        private ArrayList<Product> products;
        private int type;

        public PostUgcAdapter(ArrayList data, int type) {
            this.type = type;
            switch(type) {
                case(IMAGES_PATH_TYPE):
                    this.imagesPath = data;
                    break;
                case(CIRCLES_NAME_TYPE):
                    this.circles = data;
                    break;
                case(PRODUCTS_TYPE):
                    this.products = data;
                    break;
                default: break;
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            //如果是图片
            private ImageView imageView;
            //如果是圈子
            private TextView textView;
            //如果是商品
            private ImageView productImage;
            private TextView productName;
            private TextView productPrice;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                switch(type) {
                    case(IMAGES_PATH_TYPE):
                        this.imageView = itemView.findViewById(IMAGE_RECYCLE_VIEW_CHILD_ID);
                        break;
                    case(CIRCLES_NAME_TYPE):
                        this.textView = itemView.findViewById(CIRCLE_TEXT_RECYCLE_VIEW_CHILD_ID);
                        break;
                    case(PRODUCTS_TYPE):
                        this.productImage = itemView.findViewById(PRODUCT_RECYCLE_IMAGE_ID);
                        this.productName = itemView.findViewById(PRODUCT_RECYCLE_NAME_ID);
                        this.productPrice = itemView.findViewById(PRODUCT_RECYCLE_PRICE_ID);
                        break;
                    default: break;
                }
            }

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            switch(type) {
                case(IMAGES_PATH_TYPE):
                    RelativeLayout imagesAdapterView = viewBuilder.makeImagesAdapterView(viewGroup.getContext());
                    return new ViewHolder(imagesAdapterView);
                case(CIRCLES_NAME_TYPE):
                    RelativeLayout circlesAdapterView = viewBuilder.makeCirclesAdapterView(viewGroup.getContext());
                    return new ViewHolder(circlesAdapterView);
                case(PRODUCTS_TYPE):
                    RelativeLayout productsAdapterView = viewBuilder.makeProductsAdapterView(viewGroup.getContext());
                    return new ViewHolder(productsAdapterView);
                default: break;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            switch(type) {
                case(IMAGES_PATH_TYPE):
                    String imagePath = imagesPath.get(i);
                    final File imageFile = new File(imagePath);
                    Picasso.with(PostUGCActivity.this)
                            .load(imageFile)
                            .placeholder(R.mipmap.ic_launcher) // optional
                            .resize(SizeConstant.getPixel(168), SizeConstant.getPixel(168))
                            .into(viewHolder.imageView);
                    break;
                case(CIRCLES_NAME_TYPE):
                    Circle circle = circles.get(i);
                    viewHolder.textView.setText(circle.getCircleName());
                    String id = circle.getCircleId();
                    break;
                case(PRODUCTS_TYPE):
                    String productImage = products.get(i).getProductName();
                    String productName = products.get(i).getProductName();
                    String productPrice = String.valueOf(products.get(i).getProductPrice());
                    Picasso.with(PostUGCActivity.this)
                            .load(productImage)
                            .placeholder(R.mipmap.ic_launcher) // optional
                            .resize(SizeConstant.getPixel(180), SizeConstant.getPixel(136))
                            .into(viewHolder.productImage);
                    viewHolder.productName.setText(productName);
                    viewHolder.productPrice.setText(productPrice);
                    break;
                default: break;
            }
        }

        @Override
        public int getItemCount() {
            switch(type) {
                case(IMAGES_PATH_TYPE):
                    return imagesPath.size();
                case(CIRCLES_NAME_TYPE):
                   return circles.size();
                case(PRODUCTS_TYPE):
                    return products.size();
                default: break;
            }
            return 0;
        }



    }

}
