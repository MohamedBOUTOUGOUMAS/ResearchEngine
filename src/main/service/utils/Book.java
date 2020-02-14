package main.service.utils;

import java.util.ArrayList;

public class Book {
    public String title;
    public String author;
    public String fileName;
    public String releaseDate;
    public ArrayList<String> content;
    public Book(String fileName) {
        this.fileName = fileName;
        this.content = new ArrayList<>();
    }
}
