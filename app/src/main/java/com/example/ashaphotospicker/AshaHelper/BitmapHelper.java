package com.example.ashaphotospicker.AshaHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BitmapHelper {

    public interface DoneGetUrlActivity {
        public void doneGetUrl();
    }

    static public DoneGetUrlActivity activity;

    static public byte[] bitmapToByte(Bitmap bitmap) {
        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        try {
            bos.close();
        } catch (IOException e) {
            Log.d("close","io exception in convert bitmap to byte[] happened");
        }
        return  bitmapdata;
    }

    static public void uploadToServerAndGetTheUrl(ArrayList<String> paths) {
        Log.d("paths",paths.toString());
        BitmapHelper.activity.doneGetUrl();
    }

}
