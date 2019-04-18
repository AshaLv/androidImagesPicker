package com.example.ashaphotospicker.camera;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class MyGlideApp  extends AppGlideModule{

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}
