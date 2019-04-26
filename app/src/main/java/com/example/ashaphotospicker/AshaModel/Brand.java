package com.example.ashaphotospicker.AshaModel;

public class Brand {

    static public String BRAND_ID = "brandId";
    static public String BRAND_NAME = "brandName";

    private String brandId;
    private String brandName;
    private String brandImage;
    private String brandDesc;

    public Brand(String brandId, String brandName, String brandImage, String brandDesc) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandImage = brandImage;
        this.brandDesc = brandDesc;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public String getBrandImage() {
        return brandImage;
    }
}
