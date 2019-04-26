package com.example.ashaphotospicker.AshaModel;

public class Product {

    public static String PRODUCT_ID = "productId";
    public static String PRODUCT_NAME = "productName";
    public static String PRODUCT_IMAGE = "productUrl";
    public static String BRAND_ID = "brandId";
    public static String BRAND_NAME = "brandName";

    private String productId;
    private String productImage;
    private String productName;
    private Float productPrice;
    private String brandName;
    private String brandId;

    public Product(String productId, String productImage, String productName, Float productPrice, String brandName, String brandId) {
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.brandName = brandName;
        this.brandId = brandId;
    }

    public String getProductId() {
        return productId;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }
}
