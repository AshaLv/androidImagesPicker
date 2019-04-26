package com.example.ashaphotospicker.AshaInteractor;

import android.app.Activity;
import android.util.Log;

import com.example.ashaphotospicker.AshaConstant.HttpAddressConstant;
import com.example.ashaphotospicker.AshaModel.Product;

import java.util.ArrayList;

public class ProductInteractor {

    public interface ProductActivityInterface {
        public void didFetchTheOrderRelatedProducts(ArrayList<Product> products);
    }

    public ProductActivityInterface productActivity;

    public void getOrderRelatedProducts() {
        String api = HttpAddressConstant.getOrderRelatedProductsAddress();
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameX","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameC","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameV","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameH","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameE","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameW","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameQ","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameG","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameH","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameD","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameY","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameU","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandNameI","3413314324131"));
        productActivity.didFetchTheOrderRelatedProducts(products);
    }
}

