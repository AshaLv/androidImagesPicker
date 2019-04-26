package com.example.ashaphotospicker.AshaInteractor;

import com.example.ashaphotospicker.AshaConstant.HttpAddressConstant;
import com.example.ashaphotospicker.AshaModel.Brand;

import java.util.ArrayList;

public class BrandInteractor {

    public interface BrandActivityInterface {
        public void didSearchTheBrands(ArrayList<Brand> products);
    }

    public BrandActivityInterface brandActivity;

    public void getBrands() {
        String api = HttpAddressConstant.getBrandsAddress();
        ArrayList<Brand> brands = new ArrayList<>();
        if(Math.random() > 0.5) {
            brands.add(new Brand("3242354235252","brandName1","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
            brands.add(new Brand("3242354235252","brandName2","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
            brands.add(new Brand("3242354235252","brandName3","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
            brands.add(new Brand("3242354235252","brandName4","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
            brands.add(new Brand("3242354235252","brandName5","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
            brands.add(new Brand("3242354235252","brandName6","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
            brands.add(new Brand("3242354235252","brandName7","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
            brands.add(new Brand("3242354235252","brandName8","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
            brands.add(new Brand("3242354235252","brandName9","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
            brands.add(new Brand("3242354235252","brandNameA","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","cool brand I like it"));
        }
        brandActivity.didSearchTheBrands(brands);
    }

}
