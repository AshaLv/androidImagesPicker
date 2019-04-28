package com.example.ashaphotospicker.AshaFactory;


import android.widget.LinearLayout;

public class LinearLayoutParamsFac  {

    static public LinearLayoutParamsFac linearLayoutParamsFac;

    public LinearLayoutParamsFac() {

    }

    static public LinearLayoutParamsFac getSingleton() {
        if(linearLayoutParamsFac == null) {
            linearLayoutParamsFac = new LinearLayoutParamsFac();
        }
        return linearLayoutParamsFac;
    }

    public LinearLayout.LayoutParams createDoubleParent() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        return layoutParams;
    }

    public LinearLayout.LayoutParams createOnlyWidthMatchParent() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        return layoutParams;
    }

    public LinearLayout.LayoutParams createDoubleContent() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        return layoutParams;
    }

    public LinearLayout.LayoutParams createOnlHeightMatchParent() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        return layoutParams;
    }

}
