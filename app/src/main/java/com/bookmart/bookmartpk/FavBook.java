package com.bookmart.bookmartpk;

public class FavBook {
    private String authorName;
    private String bookImage;
    private String bookName;
    private String bookPdf;
    private String description;
    private boolean isFavorite = false;
    private String pages;

    public FavBook(){
        // empty constructor
    }

    public FavBook(String authorName, String bookImage, String bookName, String bookPdf, String description,String pages) {
        this.authorName = authorName;
        this.bookImage = bookImage;
        this.bookName = bookName;
        this.bookPdf = bookPdf;
        this.description = description;
        this.pages = pages;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBookImage() {
        return bookImage;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookPdf() {
        return bookPdf;
    }

    public String getDescription() {
        return description;
    }

    public String getPages() {
        return pages;
    }

    //    }
//
    public boolean getIsFavorite() {
        return isFavorite;
    }
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void toggleFavorite() {
        isFavorite = !isFavorite;
    }

}

