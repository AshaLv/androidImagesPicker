package com.example.ashaphotospicker.AshaHelper;

import android.widget.RelativeLayout;

public class PositionHelper {

    static public void left(RelativeLayout.LayoutParams params, int id, int distance) {
        params.addRule(
                RelativeLayout.LEFT_OF,
                id
        );
        params.rightMargin = distance;
    }

    static public void right(RelativeLayout.LayoutParams params, int id, int distance) {
        params.addRule(
                RelativeLayout.RIGHT_OF,
                id
        );
        params.leftMargin = distance;
    }

    static public void below(RelativeLayout.LayoutParams params, int id, int distance) {
        params.addRule(
                RelativeLayout.BELOW,
                id
        );
        params.topMargin = distance;
    }

    static public void above(RelativeLayout.LayoutParams params, int id, int distance) {
        params.addRule(
                RelativeLayout.ABOVE,
                id
        );
        params.bottomMargin = distance;
    }

    static public void equal_left(RelativeLayout.LayoutParams params, int id) {
        params.addRule(
                RelativeLayout.ALIGN_LEFT,
                id
        );
    }

    static void equal_right(RelativeLayout.LayoutParams params, int id) {
        params.addRule(
                RelativeLayout.ALIGN_RIGHT,
                id
        );
    }

}
