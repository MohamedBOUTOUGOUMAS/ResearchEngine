package main.service.utils;

public class Book {
    public String title;
    public String author;
    public String filePath;
    public String releaseDate;

    public Book(String filePath) {
        this.filePath = filePath;
    }
}
