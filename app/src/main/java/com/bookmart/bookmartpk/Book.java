package com.bookmart.bookmartpk;

public class Book {
    private String AuthorName;
    private String BookImage;
    private String BookName;
    private String BookPdf;
    private String Description;
    private boolean isFavorite = false;
    private String Pages;

    public Book(){
        // empty constructor
    }

    public Book(String AuthorName, String BookImage, String BookName,  String BookPdf, String Description, String Pages) {
        this.AuthorName = AuthorName;
        this.BookImage = BookImage;
        this.BookName = BookName;
        this.BookPdf = BookPdf;
        this.Description = Description;
        this.Pages = Pages;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public String getBookImage() {
        return BookImage;
    }

    public String getBookName() {
        return BookName;
    }

    public String getBookPdf() {
        return BookPdf;
    }

    public String getDescription() {
        return Description;
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

    public String getPages() {
        return Pages;
    }
}

