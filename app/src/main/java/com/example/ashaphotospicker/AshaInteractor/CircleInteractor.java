package com.example.ashaphotospicker.AshaInteractor;

import android.util.Log;

import com.example.ashaphotospicker.AshaConstant.HttpAddressConstant;
import com.example.ashaphotospicker.AshaModel.Circle;

import java.util.ArrayList;

public class CircleInteractor {

    public interface CircleActivity {
        public void didGetTheCircles(ArrayList<Circle> circles);
    }

    private CircleActivity activity;

    public void getUserRelatedCircles(String userId) {
        String api = HttpAddressConstant.getUserRelatedCirclesAddress();
        ArrayList<Circle> circles = new ArrayList<>();
        double r = Math.random();
        if(r > 0.3) {
            circles.add(new Circle("3242354235252","circleName1"));
            circles.add(new Circle("3242354235252","circleName2"));
            circles.add(new Circle("3242354235252","circleName3"));
            circles.add(new Circle("3242354235252","circleName4"));
        }
        activity.didGetTheCircles(circles);
    }

    public void setActivity(CircleActivity activity) {
        this.activity = activity;
    }
}
