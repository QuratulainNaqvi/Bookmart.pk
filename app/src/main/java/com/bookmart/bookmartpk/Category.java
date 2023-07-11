package com.bookmart.bookmartpk;

public class Category {
    private String Name;
    private String Image;

    public Category(){
        // empty constructor
    }

    public Category(String Name, String Image) {
        this.Name = Name;
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public String getImage() {
        return Image;
    }
}
