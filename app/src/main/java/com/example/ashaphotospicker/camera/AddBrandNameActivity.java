package com.example.ashaphotospicker.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.ashaphotospicker.AshaConstant.SizeConstant;
import com.example.ashaphotospicker.AshaFactory.LinearLayoutParamsFac;
import com.example.ashaphotospicker.AshaHelper.BaseActivity;
import com.example.ashaphotospicker.AshaInteractor.BrandInteractor;
import com.example.ashaphotospicker.AshaModel.Brand;
import com.example.ashaphotospicker.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddBrandNameActivity extends BaseActivity implements BrandInteractor.BrandActivityInterface {

    private final int BRAND_IMAGE_ID = 324;
    private final int BRAND_NAME_ID = 3;
    private final int BRAND_PRICE_ID = 425;

    private LinearLayout rootView;
    private ViewBuilder viewBuilder;
    private ArrayList<Brand> brands;
    private BrandInteractor brandInteractor;
    private BrandsWrapperAdapter brandsWrapperAdapter;
    private RelativeLayout historyView;
    private RelativeLayout noResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brands = new ArrayList<>();
        viewBuilder = new ViewBuilder();
        rootView = viewBuilder.makeRoot();
        setContentView(rootView);
        SearchView searchInputView = viewBuilder.makeSearchInputView();
        historyView = viewBuilder.makeHistoryView();
        RecyclerView brandsWrapper = viewBuilder.makeBrandsWrapper();
        brandsWrapperAdapter = new BrandsWrapperAdapter(brands);
        brandsWrapper.setAdapter(brandsWrapperAdapter);
        brandsWrapper.setHasFixedSize(true);
        rootView.addView(searchInputView);
        if(historyView != null)rootView.addView(historyView);
        rootView.addView(brandsWrapper);
        super.setNavigationBar();
        addSearchListenerToSearchView(searchInputView);
    }

    private void addSearchListenerToSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                brandInteractor.getBrands();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void didSearchTheBrands(ArrayList<Brand> brands) {
        if(brands.size() > 0) {
            //搜索得到品牌
            if(this.brands.size() > 0) {
                this.brands.clear();
                this.brands.addAll(brands);
                brandsWrapperAdapter.notifyDataSetChanged();
            }
        } else{
            //没有搜索到，那么只能看看没有结果
            this.brands.clear();
            brandsWrapperAdapter.notifyDataSetChanged();
            showNoBrandSearchedView();
        }
    }

    private void showNoBrandSearchedView() {
        if(noResultView == null){
            noResultView = viewBuilder.makeNoResultView();
            rootView.addView(noResultView);
        }
        else noResultView.setVisibility(View.VISIBLE);
    }

    class ViewBuilder {

        public ViewBuilder() {

        }

        public LinearLayout makeRoot() {
            LinearLayout root = new LinearLayout(AddBrandNameActivity.this);
            return root;
        }

        public SearchView makeSearchInputView() {
            SearchView searchView = new SearchView(AddBrandNameActivity.this);
            return searchView;
        }

        public RelativeLayout makeHistoryView() {
            RelativeLayout relativeLayout = new RelativeLayout(AddBrandNameActivity.this);
            return relativeLayout;
        }

        public RelativeLayout makeNoResultView() {
            RelativeLayout relativeLayout = new RelativeLayout(AddBrandNameActivity.this);
            return relativeLayout;
        }

        public RecyclerView makeBrandsWrapper() {
            RecyclerView brandsWrapper = new RecyclerView(AddBrandNameActivity.this);
            return  brandsWrapper;
        }

        public RelativeLayout makeBrand() {
            RelativeLayout relativeLayout = new RelativeLayout(AddBrandNameActivity.this);
            relativeLayout.addView(makeBrandImageView());
            relativeLayout.addView(makeBrandName());
            relativeLayout.addView(makeBrandDesc());
            return relativeLayout;
        }

        public ImageView makeBrandImageView() {
            ImageView imageView = new ImageView(AddBrandNameActivity.this);
            return imageView;
        }

        public TextView makeBrandName() {
            TextView textView = new TextView(AddBrandNameActivity.this);
            return textView;
        }

        public TextView makeBrandDesc() {
            TextView textView = new TextView(AddBrandNameActivity.this);
            return textView;
        }

    }

    class BrandsWrapperAdapter extends RecyclerView.Adapter<BrandsWrapperAdapter.ViewHolder> {

        private ArrayList<Brand> brands;

        public BrandsWrapperAdapter(ArrayList<Brand> brands) {
            this.brands = brands;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView brandImage;
            TextView brandName;
            TextView brandDesc;

            public ViewHolder(View v) {
                super(v);
                this.brandImage = v.findViewById(BRAND_IMAGE_ID);
                this.brandName = v.findViewById(BRAND_NAME_ID);
                this.brandDesc = v.findViewById(BRAND_PRICE_ID);
            }

        }

        @Override
        public BrandsWrapperAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout view = viewBuilder.makeBrand();
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final BrandsWrapperAdapter.ViewHolder holder, final int position) {
            holder.brandImage.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(AddBrandNameActivity.this)
                            .load(brands.get(position).getBrandImage())
                            .resize(SizeConstant.smallImageSize(),SizeConstant.smallImageSize())
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(holder.brandImage);
                }
            });
            holder.brandName.setText(brands.get(position).getBrandName());
            holder.brandDesc.setText(String.valueOf(brands.get(position).getBrandDesc()));
            addClickListenerToProductViewWrapper((RelativeLayout) holder.brandName.getParent(),
                    brands.get(position));
        }

        @Override
        public int getItemCount() {
            return brands.size();
        }

        private void addClickListenerToProductViewWrapper(RelativeLayout v, final Brand brand) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BrandViewWrapperDidClicked(brand);
                }
            });
        }

        private void BrandViewWrapperDidClicked(Brand brand) {
            Intent backIntent = new Intent();
            backIntent.putExtra(Brand.BRAND_NAME,brand.getBrandName());
            backIntent.putExtra(Brand.BRAND_ID,brand.getBrandId());
            setResult(Activity.RESULT_OK,backIntent);
            finish();
        }
    }

}
