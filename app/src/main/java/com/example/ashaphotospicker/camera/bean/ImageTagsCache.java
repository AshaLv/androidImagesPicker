package com.example.ashaphotospicker.camera.bean;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageTagsCache {

    public String url;
    public ArrayList<String> tagNames;
    // 用;ashaSeparator;做品牌名称的分割线，例子"tagName;ashaSperator;ProductName:ProductUrl:ProductId:BrandId:productPrice
    public ArrayList<Float> tagPoints;
    //tagNames对应在tagPoints的坐标是为tagNames的坐标index=》（index*2）和（index*2）+ 1
    public float ratio;

    public ImageTagsCache(String url, ArrayList<String> tagNames, ArrayList<Float> tagPoints) {
        this.url = url;
        this.tagNames = tagNames;
        this.tagPoints = tagPoints;
    }

    public ImageTagsCache() {
        this.url = "";
        this.tagNames = new ArrayList<>();
        this.tagPoints = new ArrayList<>();
    }

}
