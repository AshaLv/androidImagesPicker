package com.example.ashaphotospicker.AshaFactory;

import android.widget.RelativeLayout;

public class RelativeLayoutParamsFac {

    static public RelativeLayoutParamsFac relativeLayoutParamsFac;

    public RelativeLayoutParamsFac() {

    }

    static public RelativeLayoutParamsFac getSingleton() {
        if(relativeLayoutParamsFac == null) {
            relativeLayoutParamsFac = new RelativeLayoutParamsFac();
        }
        return relativeLayoutParamsFac;
    }

    public RelativeLayout.LayoutParams createDoubleParent() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        return layoutParams;
    }

    public RelativeLayout.LayoutParams createOnlyWidthMatchParent() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        return layoutParams;
    }

    public RelativeLayout.LayoutParams createDoubleContent() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        return layoutParams;
    }

    public RelativeLayout.LayoutParams createOnlHeightMatchParent() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        return layoutParams;
    }
}
