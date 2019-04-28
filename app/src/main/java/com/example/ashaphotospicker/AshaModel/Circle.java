package com.example.ashaphotospicker.AshaModel;

public class Circle {

    private String circleId;
    private String circleName;

    public Circle(String circleId,String circleName) {
        this.circleId = circleId;
        this.circleName = circleName;
    }

    public String getCircleId() {
        return circleId;
    }

    public String getCircleName() {
        return circleName;
    }
}
