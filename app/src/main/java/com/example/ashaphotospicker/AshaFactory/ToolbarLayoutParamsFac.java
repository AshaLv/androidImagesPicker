package com.example.ashaphotospicker.AshaFactory;

import android.support.v7.widget.Toolbar;

public class ToolbarLayoutParamsFac {

    public ToolbarLayoutParamsFac() {

    }

    public Toolbar.LayoutParams createDoubleParent() {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.MATCH_PARENT
        );
        return layoutParams;
    }

    public Toolbar.LayoutParams createOnlyWidthMatchParent() {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT
        );
        return layoutParams;
    }

    public Toolbar.LayoutParams createDoubleContent() {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
        );
        return layoutParams;
    }

    public Toolbar.LayoutParams createOnlHeightMatchParent() {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.MATCH_PARENT
        );
        return layoutParams;
    }
}
