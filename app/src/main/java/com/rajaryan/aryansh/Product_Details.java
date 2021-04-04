package com.rajaryan.aryansh;

public class Product_Details {
    String Image,Price,Name,Size,pr,calories,Quantity;

    public Product_Details() {
    }

    public Product_Details(String image, String price, String name, String size, String pr, String calories, String quantity) {
        Image = image;
        Price = price;
        Name = name;
        Size = size;
        this.pr = pr;
        this.calories = calories;
        Quantity = quantity;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getPr() {
        return pr;
    }

    public void setPr(String pr) {
        this.pr = pr;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
