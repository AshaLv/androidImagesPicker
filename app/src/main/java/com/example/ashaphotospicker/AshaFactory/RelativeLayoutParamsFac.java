package com.example.ashaphotospicker.AshaFactory;

import android.widget.RelativeLayout;

public class RelativeLayoutParamsFac {

    public RelativeLayoutParamsFac() {

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
