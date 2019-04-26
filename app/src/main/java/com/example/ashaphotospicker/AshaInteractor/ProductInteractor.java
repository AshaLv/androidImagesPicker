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
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        products.add(new Product("3242354235252","https://file.mengqu.meng2333.com/M5MBYPA8AhQIaMPtprjHO1C.jpg","productName",(float)133,"brandName","3413314324131"));
        productActivity.didFetchTheOrderRelatedProducts(products);
    }
}

