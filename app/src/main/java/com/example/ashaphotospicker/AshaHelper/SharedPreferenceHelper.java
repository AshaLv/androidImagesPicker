package com.example.ashaphotospicker.AshaHelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ashaphotospicker.camera.bean.ImageTagsCache;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class SharedPreferenceHelper extends AppCompatActivity {

    private Gson gson = null;
    private SharedPreferences.Editor cachedImagesPathEditor = null;
    private SharedPreferences.Editor cachedImagesDataWithTagsPathEditor = null;
    private SharedPreferences cachedImagesPathSharedPreference = null;
    private SharedPreferences cachedImagesDataWithTagsSharedPreference = null;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    public SharedPreferenceHelper() {

    }

    //存放各类singleton
    public Gson getSingletonGson() {
        if(gson == null) {
            gson = new Gson();
        }
        return gson;
    }
    public SharedPreferences.Editor getSingletonCachedImagesPathEditor () {
        if(cachedImagesPathEditor == null) {
            cachedImagesPathEditor = getSingletonCachedImagesPathSharedPreference().edit();
        }
        return cachedImagesPathEditor;
    }
    public SharedPreferences.Editor getSingletonCachedImagesDataWithTagsPathEditor () {
        if(cachedImagesDataWithTagsPathEditor == null) {
            cachedImagesDataWithTagsPathEditor = getSingletonCachedImagesDataWithTagsSharedPreference().edit();
        }
        return cachedImagesDataWithTagsPathEditor;
    }
    public SharedPreferences getSingletonCachedImagesPathSharedPreference () {
        if(cachedImagesPathSharedPreference == null) {
            cachedImagesPathSharedPreference = getSharedPreferences("multiImageSelectorActivityImagesPathCache", MODE_PRIVATE);
        }
        return cachedImagesPathSharedPreference;
    }
    public SharedPreferences getSingletonCachedImagesDataWithTagsSharedPreference () {
        if(cachedImagesDataWithTagsSharedPreference == null) {
            cachedImagesDataWithTagsSharedPreference = getSharedPreferences("multiImageSelectorActivityImagesWithTags", MODE_PRIVATE);
        }
        return cachedImagesDataWithTagsSharedPreference;
    }

    //拿到发布动态存在sharedPreference的图片地址数据
    public ArrayList getCachedImagesPath() {
        ArrayList result = null;
        SharedPreferences sp = getSingletonCachedImagesPathSharedPreference();
        if(sp.contains("imagesPath") && sp.getString("imagesPath","").length() > 0) {
            result = getSingletonGson().fromJson(sp.getString("imagesPath",""), ArrayList.class);
        }
        return result;
    }
    //设置sharedPreference的图片地址数据
    public void saveCachedImagesPath(ArrayList imagesPath) {
        getSingletonCachedImagesPathEditor().putString("imagesPath",getSingletonGson().toJson(imagesPath));
        getSingletonCachedImagesPathEditor().commit();
    }

    //拿到发布动态存在sharedPreference的图片包含标签数据
    public Dictionary<String, ImageTagsCache> getCachedImagesDataWithTags() {
        Dictionary<String, ImageTagsCache> result = null;
        SharedPreferences sp = getSingletonCachedImagesDataWithTagsSharedPreference();
        if(sp.contains("imagesWithTags") && sp.getString("imagesWithTags","").length() > 0) {
            result = getSingletonGson().fromJson(sp.getString("imagesWithTags",""), Hashtable.class);
        }
        return result;
    }
    public ImageTagsCache getCachedImageTags(String path) {
        Dictionary<String, ImageTagsCache> d = getCachedImagesDataWithTags();
        if(d != null && d.get(path) != null) {
            String imageTagsCacheString = getSingletonGson().toJson(d.get(path));
            ImageTagsCache itc = getSingletonGson().fromJson(imageTagsCacheString, ImageTagsCache.class);
            return itc;
        }
        return null;
    }
    //设置sharedPreference的图片标签数据
    public void saveCachedImagesDataWithTags(Dictionary c) {
        getSingletonCachedImagesDataWithTagsPathEditor().putString("imagesWithTags",getSingletonGson().toJson(c));
        getSingletonCachedImagesDataWithTagsPathEditor().commit();
    }


}
