package com.example.ashaphotospicker.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.ashaphotospicker.AshaConstant.ColorConstant;
import com.example.ashaphotospicker.AshaConstant.SizeConstant;
import com.example.ashaphotospicker.AshaFactory.LinearLayoutParamsFac;
import com.example.ashaphotospicker.AshaFactory.RelativeLayoutParamsFac;
import com.example.ashaphotospicker.AshaHelper.BaseActivity;
import com.example.ashaphotospicker.AshaHelper.PositionHelper;
import com.example.ashaphotospicker.AshaHelper.SharedPreferenceHelper;
import com.example.ashaphotospicker.AshaInteractor.BrandInteractor;
import com.example.ashaphotospicker.AshaModel.Brand;
import com.example.ashaphotospicker.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddBrandNameActivity extends SharedPreferenceHelper implements BrandInteractor.BrandActivityInterface {

    private final int BRAND_IMAGE_ID = 324;
    private final int BRAND_NAME_ID = 3;
    private final int BRAND_DESC_ID = 425;
    private final int NO_RESULT_TEXT_ID = 4532;
    private final int BRAND_HISTORY_TEXT_ID =237249;

    private LinearLayout rootView;
    private ViewBuilder viewBuilder;
    private ArrayList<Brand> brands;
    private BrandInteractor brandInteractor;
    private BrandsWrapperAdapter brandsWrapperAdapter;
    private LinearLayout historyView;
    private BrandsWrapperAdapter historyAdapter;
    private RelativeLayout noResultView;
    private SearchView searchInputView;
    private LinearLayout linearLayoutHistoryView;
    private  RecyclerView historyRecycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brandInteractor = new BrandInteractor();
        brandInteractor.brandActivity = this;
        SizeConstant.getSingletonMetrics(this);
        brands = new ArrayList<>();
        viewBuilder = new ViewBuilder();
        rootView = viewBuilder.makeRoot();
        setContentView(rootView);
        super.setNavigationBar();
        searchInputView = viewBuilder.makeSearchInputView();
        historyRecycleview = viewBuilder.makeHistoryView();
        RecyclerView brandsWrapper = viewBuilder.makeBrandsWrapper();
        brandsWrapperAdapter = new BrandsWrapperAdapter(brands);
        brandsWrapper.setAdapter(brandsWrapperAdapter);
        brandsWrapper.setHasFixedSize(true);
        brandsWrapper.setLayoutManager(new LinearLayoutManager(this));
        rootView.addView(searchInputView);
        if(historyRecycleview!=null){
            //在这里写历史搜索数据
            ArrayList<String> brandsHistoryName = AddBrandNameActivity.this.getCachedBrandsHistory();
            if(brandsHistoryName != null) {
                TextView historyPrompt = viewBuilder.makeHistoryPrompt();
                historyAdapter = new BrandsWrapperAdapter(brandsHistoryName,"brandsHistoryName");
                LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createOnlyWidthMatchParent();
                params.leftMargin = SizeConstant.getPixel(30);
                params.rightMargin = SizeConstant.getPixel(30);
                historyRecycleview.setHasFixedSize(true);
                historyRecycleview.setAdapter(historyAdapter);
                historyRecycleview.setLayoutParams(params);
                rootView.addView(historyPrompt);
                rootView.addView(historyRecycleview);
            }
        }
        rootView.addView(brandsWrapper);

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
        if(this.brands.size() > 0) {
            this.brands.clear();
        }
        if(brands.size() > 0) {
            Log.d("r",brands.toString());
            //搜索得到品牌
            this.brands.addAll(brands);
            brandsWrapperAdapter.notifyDataSetChanged();
            if(noResultView != null){
                rootView.removeView(noResultView);
                noResultView = null;
            }
        } else{
            //没有搜索到，那么只能看看没有结果
            brandsWrapperAdapter.notifyDataSetChanged();
            showNoBrandSearchedView();
        }
    }

    private void updateBrandsHistory(String brandName,String brandId) {
        ArrayList<String> bransHistoryName = this.getCachedBrandsHistory();
        if( bransHistoryName!=null && bransHistoryName.size() > 3) {
            //只保留4个，将末尾去除
            bransHistoryName.remove(3);
        }
        if(bransHistoryName == null){
            bransHistoryName = new ArrayList<String>();
        }
        bransHistoryName.add(0,brandName+":"+brandId);
        this.saveCachedBrandsHistory(bransHistoryName);
    }

    private void addClickListenerToNoResultView(RelativeLayout v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView brandNameView = v.findViewById(NO_RESULT_TEXT_ID);
                String brandName = (String) brandNameView.getText();
                String brandId = "noBrandId";
                Brand brand = new Brand(brandId,brandName.split(":")[1]);
                //:分割了提示字和搜索的品牌名
                BrandViewWrapperDidClicked(brand);
            }
        });
    }

    private void showNoBrandSearchedView() {
        noResultView = viewBuilder.makeNoResultView();
        rootView.addView(noResultView,1);
        addClickListenerToNoResultView(noResultView);
    }

    class ViewBuilder {

        public ViewBuilder() {

        }

        public LinearLayout makeRoot() {
            LinearLayout root = new LinearLayout(AddBrandNameActivity.this);
            root.setOrientation(LinearLayout.VERTICAL);
            return root;
        }

        public SearchView makeSearchInputView() {
            LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createDoubleContent();
            params.width = (int)(690*SizeConstant.getDensity());
            params.height = (int)(60*SizeConstant.getDensity());
            params.topMargin = (int)(20 * SizeConstant.getDensity());
            SearchView searchView = new SearchView(AddBrandNameActivity.this);
            searchView.setGravity(Gravity.CENTER);
            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint("填写品牌名");
            searchView.setLayoutParams(params);
            return searchView;
        }

        public TextView makeHistoryPrompt() {
            LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createDoubleContent();
            params.topMargin = SizeConstant.getPixel(10);
            params.leftMargin = SizeConstant.getPixel(20);
            TextView historyPrompt = new TextView(AddBrandNameActivity.this);
            historyPrompt.setText("常用品牌");
            historyPrompt.setTextColor(Color.GRAY);
            historyPrompt.setTextSize(SizeConstant.mediumSize());
            historyPrompt.setLayoutParams(params);
            return historyPrompt;
        }

        public RecyclerView makeHistoryView() {
            RecyclerView historyRecycleView = new RecyclerView(AddBrandNameActivity.this);
            historyRecycleView.setLayoutManager(new GridLayoutManager(AddBrandNameActivity.this,4));
            return historyRecycleView;
        }

        public RelativeLayout makeNoResultView() {
            LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createDoubleContent();
            params.topMargin = SizeConstant.getPixel(30);
            params.leftMargin = SizeConstant.getPixel(30);
            RelativeLayout relativeLayout = new RelativeLayout(AddBrandNameActivity.this);
            relativeLayout.setLayoutParams(params);

            //子控件
            RelativeLayout.LayoutParams params2 = new RelativeLayoutParamsFac().createDoubleContent();
            TextView textView = new TextView(AddBrandNameActivity.this);
            textView.setText("搜索不到,直接添加:"+searchInputView.getQuery());
            textView.setId(NO_RESULT_TEXT_ID);
            textView.setLayoutParams(params2);
            relativeLayout.addView(textView);

            return relativeLayout;
        }

        public RecyclerView makeBrandsWrapper() {
            LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createDoubleContent();
            params.topMargin = SizeConstant.getPixel(20);
            params.leftMargin = SizeConstant.getPixel(30);
            RecyclerView brandsWrapper = new RecyclerView(AddBrandNameActivity.this);
            brandsWrapper.setLayoutParams(params);
            return  brandsWrapper;
        }

        public RelativeLayout makeBrand() {
            LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createDoubleContent();
            params.bottomMargin = SizeConstant.getPixel(20);
            RelativeLayout relativeLayout = new RelativeLayout(AddBrandNameActivity.this);
            relativeLayout.addView(makeBrandImageView());
            relativeLayout.addView(makeBrandName());
            relativeLayout.addView(makeBrandDesc());
            relativeLayout.setLayoutParams(params);
            return relativeLayout;
        }

        public ImageView makeBrandImageView() {
            ImageView imageView = new ImageView(AddBrandNameActivity.this);
            imageView.setId(BRAND_IMAGE_ID);
            return imageView;
        }

        public TextView makeBrandName() {
            RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createDoubleContent();
            PositionHelper.right(params,BRAND_IMAGE_ID,SizeConstant.getPixel(20));
            TextView textView = new TextView(AddBrandNameActivity.this);
            textView.setId(BRAND_NAME_ID);
            textView.setLayoutParams(params);
            return textView;
        }

        public TextView makeBrandDesc() {
            RelativeLayout.LayoutParams params = new RelativeLayoutParamsFac().createDoubleContent();
            PositionHelper.equal_left(params,BRAND_NAME_ID);
            PositionHelper.below(params,BRAND_NAME_ID,SizeConstant.getPixel(5));
            TextView textView = new TextView(AddBrandNameActivity.this);
            textView.setId(BRAND_DESC_ID);
            textView.setLayoutParams(params);
            return textView;
        }

        public TextView makeBrandHistoryNameTextView() {
            LinearLayout.LayoutParams params = new LinearLayoutParamsFac().createDoubleContent();
            TextView textView = new TextView(AddBrandNameActivity.this);
            textView.setTextSize(SizeConstant.getSpPixel(24));
            textView.setTextColor(ColorConstant.mainColor);
            //:分割了名字和id
            textView.setLayoutParams(params);
            return textView;
        }

    }

    class BrandsWrapperAdapter extends RecyclerView.Adapter<BrandsWrapperAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            //如果是展示品牌
            ImageView brandImage;
            TextView brandName;
            TextView brandDesc;
            //如果是展示历史数据
            TextView brandHistoryTextView;

            public ViewHolder(View v) {
                super(v);
                if (brands != null) {
                    this.brandImage = v.findViewById(BRAND_IMAGE_ID);
                    this.brandName = v.findViewById(BRAND_NAME_ID);
                    this.brandDesc = v.findViewById(BRAND_DESC_ID);
                }
                if (brandsHistoryName != null) {
                    this.brandHistoryTextView = (TextView) v;
                }
            }
        }

        private ArrayList<Brand> brands;
        private ArrayList<String> brandsHistoryName;

        public BrandsWrapperAdapter(ArrayList<Brand> brands) {
            this.brands = brands;
        }

        //如果是品牌历史搜索记录的画
        public BrandsWrapperAdapter(ArrayList<String> brandsHistoryName, String type) {
            this.brandsHistoryName = brandsHistoryName;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if(brands != null) {
                RelativeLayout view = viewBuilder.makeBrand();
                return new ViewHolder(view);
            }
            if(brandsHistoryName != null) {
                TextView view = viewBuilder.makeBrandHistoryNameTextView();
                return new ViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final @NonNull ViewHolder viewHolder, final int position) {
            if(brands != null) {
                viewHolder.brandImage.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(AddBrandNameActivity.this)
                                .load(brands.get(position).getBrandImage())
                                .resize(SizeConstant.smallImageSize(),SizeConstant.smallImageSize())
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(viewHolder.brandImage);
                    }
                });
                viewHolder.brandName.setText(brands.get(position).getBrandName());
                viewHolder.brandDesc.setText(brands.get(position).getBrandDesc());
                addClickListenerToProductViewWrapper((RelativeLayout) viewHolder.brandName.getParent(),
                        brands.get(position));
            }
            if(brandsHistoryName != null) {
                viewHolder.brandHistoryTextView.setText(brandsHistoryName.get(position).split(":")[0]);
                //":"分割brandName和brandId,所以0里面就是brandName
                viewHolder.brandHistoryTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] brandSeperator = brandsHistoryName.get(position).split(":");
                        //":"分割brandName和brandId
                        Brand brand = new Brand(brandSeperator[1],brandSeperator[0]);
                        BrandViewWrapperDidClicked(brand);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            int size = 0;
            if(brands != null) {
                size = brands.size();
            }
            if(brandsHistoryName != null) {
                size = brandsHistoryName.size();
            }
            return size;
        }

        private void addClickListenerToProductViewWrapper(RelativeLayout v, final Brand brand) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BrandViewWrapperDidClicked(brand);
                }
            });
        }
    }

    private void BrandViewWrapperDidClicked(Brand brand) {
        updateBrandsHistory(brand.getBrandName(),brand.getBrandId());
        Intent backIntent = new Intent();
        backIntent.putExtra(Brand.BRAND_NAME,brand.getBrandName());
        backIntent.putExtra(Brand.BRAND_ID,brand.getBrandId());
        setResult(Activity.RESULT_OK,backIntent);
        finish();
    }

}
