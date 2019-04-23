package com.example.ashaphotospicker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ashaphotospicker.camera.MultiImageSelectorActivity;



public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private TextView addPhotosButtonText;

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_ACCESS_PERMISSION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addPhotosButtonText = (TextView) findViewById(R.id.addPhotosButtonText);
        Log.d(TAG,"addPhotosButtonText openImageGallery");
        Log.d(TAG,(String)addPhotosButtonText.getText());
        addPhotosButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"openImageGallery");
                openImageGallery();
            }
        });
        Log.d(TAG,(String)addPhotosButtonText.getText());
    }

    public void checkCameraRelatedPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestCameraRelatedPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_ACCESS_PERMISSION);
        } else {
            pickImage();
        }
    }

    public void requestCameraRelatedPermission(final String permission, String rationale, final int requestCode) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel,null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"does permission finished go into here");
        if(requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"4234242");
                pickImage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void pickImage() {
        Log.d(TAG,"does pickImage function work well");
        boolean showCamera = false;
        int maxNum = 9;
        Intent intent = new Intent(MainActivity.this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,showCamera);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,maxNum);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        startActivity(intent);
    }

    public void openImageGallery() {
        checkCameraRelatedPermission();
    }

}
