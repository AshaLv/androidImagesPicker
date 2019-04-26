package com.example.ashaphotospicker.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ashaphotospicker.AshaFactory.LinearLayoutParamsFac;
import com.example.ashaphotospicker.AshaFactory.RelativeLayoutParamsFac;
import com.example.ashaphotospicker.AshaHelper.BaseActivity;
import com.example.ashaphotospicker.AshaConstant.SizeConstant;
import com.example.ashaphotospicker.AshaInteractor.ProductInteractor;
import com.example.ashaphotospicker.AshaModel.Product;
import com.example.ashaphotospicker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddProductActivity extends BaseActivity implements ProductInteractor.ProductActivityInterface {

    private int PRODUCT_IMAGE_ID = 432;
    private int PRODUCT_NAME_ID = 33;
    private int PRODUCT_PRICE_ID = 43;
    private int ADAPTER_ROOT = 423;

    private ViewBuilder viewBuilder;
    private ArrayList<Product> products;
    private ProductInteractor productInteractor;
    private ProductWrapperAdapter productWrapperAdapter;

    private LinearLayout rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SizeConstant.getSingletonMetrics(this);
        viewBuilder = new ViewBuilder();
        rootView = viewBuilder.makeRoot();
        setContentView(rootView);
        productInteractor = new ProductInteractor();
        products  = new ArrayList();
        super.setNavigationBar();
        TextView titleView = viewBuilder.makeTitle();
        rootView.addView(titleView);
        RecyclerView productWrapper = viewBuilder.makeProductWrapper();
        productWrapperAdapter = new ProductWrapperAdapter(products);
        productWrapper.setAdapter(productWrapperAdapter);
        productWrapper.setLayoutManager(new LinearLayoutManager(this));
        rootView.addView(productWrapper);
        productInteractor.productActivity = this;
        productInteractor.getOrderRelatedProducts();

    }

    @Override
    public void didFetchTheOrderRelatedProducts(ArrayList<Product> products) {
        this.products.addAll(products);
        productWrapperAdapter.notifyDataSetChanged();
    }

    class ViewBuilder {

        public ViewBuilder() {

        }

        public LinearLayout makeRoot() {
            LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createOnlyWidthMatchParent();
            params.topMargin = SizeConstant.extremeSmallDistance();
            params.leftMargin = SizeConstant.extremeSmallDistance();
            LinearLayout root = new LinearLayout(AddProductActivity.this);
            root.setOrientation(LinearLayout.VERTICAL);
            root.setLayoutParams(params);
            return root;
        }

        public TextView makeTitle() {
            RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createDoubleContent();
            params.width = (int)(SizeConstant.getDensity() * 139);
            params.height = (int)(SizeConstant.getDensity() * 48);
            TextView title = new TextView(AddProductActivity.this);
            title.setText("最近订单");
            title.setTextColor(Color.BLACK);
            title.setBackgroundColor(Color.RED);
            title.setLayoutParams(params);
            Log.d("111",String.valueOf(SizeConstant.hugeSize()));
            title.setTextSize(SizeConstant.hugeSize());
            return title;
        }

        public RecyclerView makeProductWrapper() {
            LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createOnlyWidthMatchParent();
            params.topMargin = SizeConstant.extremeSmallDistance();
            RecyclerView productWrapper = new RecyclerView(AddProductActivity.this);
            productWrapper.setLayoutParams(params);
            return productWrapper;
        }

        public RelativeLayout makeProduct(Context context) {
            RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createOnlyWidthMatchParent();
            params.bottomMargin = SizeConstant.extremeSmallDistance();
            RelativeLayout relativeLayout = new RelativeLayout(context);
            relativeLayout.addView(makeProductImageView(context));
            relativeLayout.addView(makeProductNameVIew(context));
            relativeLayout.addView(makeProductPriceView(context));
            relativeLayout.setLayoutParams(params);
            return relativeLayout;
        }

        public ImageView makeProductImageView(Context context) {
            RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createDoubleContent();
            ImageView imageView = new ImageView(context);
            imageView.setId(PRODUCT_IMAGE_ID);
            imageView.setLayoutParams(params);
            return imageView;
        }

        public TextView makeProductNameVIew(Context context) {
            RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createDoubleContent();
            TextView textView = new TextView(context);
            textView.setId(PRODUCT_NAME_ID);
            textView.setLayoutParams(params);
            return textView;
        }

        public TextView makeProductPriceView(Context context) {
            RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createDoubleContent();
            TextView textView = new TextView(context);
            textView.setId(PRODUCT_PRICE_ID);
            textView.setLayoutParams(params);
            return textView;
        }

    }

    class ProductWrapperAdapter extends RecyclerView.Adapter<ProductWrapperAdapter.ViewHolder> {

        private ArrayList<Product> products;

        public ProductWrapperAdapter(ArrayList<Product> products) {
            this.products = products;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView productImage;
            TextView productName;
            TextView productPrice;

            public ViewHolder(View v) {
                super(v);
                this.productImage = v.findViewById(PRODUCT_IMAGE_ID);
                this.productName = v.findViewById(PRODUCT_NAME_ID);
                this.productPrice = v.findViewById(PRODUCT_PRICE_ID);
            }

        }

        @Override
        public ProductWrapperAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout view = viewBuilder.makeProduct(parent.getContext());
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductWrapperAdapter.ViewHolder holder, final int position) {
            holder.productImage.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(AddProductActivity.this)
                            .load(products.get(position).getProductImage())
                            .resize(SizeConstant.smallImageSize(),SizeConstant.smallImageSize())
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(holder.productImage);
                }
            });
            holder.productName.setText(products.get(position).getProductName());
            holder.productPrice.setText(String.valueOf(products.get(position).getProductPrice()));
            addClickListenerToProductViewWrapper((RelativeLayout) holder.productName.getParent(),
                    products.get(position));
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        private void addClickListenerToProductViewWrapper(RelativeLayout v, final Product product) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductViewWrapperDidClicked(product);
                }
            });
        }

        private void ProductViewWrapperDidClicked(Product product) {
            Intent backIntent = new Intent();
            backIntent.putExtra(Product.PRODUCT_NAME,product.getProductName());
            backIntent.putExtra(Product.PRODUCT_ID,product.getProductId());
            backIntent.putExtra(Product.PRODUCT_IMAGE,product.getProductImage());
            backIntent.putExtra(Product.BRAND_NAME,product.getBrandName());
            backIntent.putExtra(Product.BRAND_ID,product.getBrandId());
            setResult(Activity.RESULT_OK,backIntent);
            finish();
        }
    }

}
