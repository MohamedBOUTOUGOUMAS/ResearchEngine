package topics;

import helpers.GenericHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Book {
    public String title;
    public String author;
    public String fileName;
    public String releaseDate;
    public ArrayList<String> content;
    public String language;

    public Book(String fileName) {
        this.fileName = fileName;
        this.content = new ArrayList<>();
    }

    public static Book getBook(String fileName) {
        BufferedReader lecteurAvecBuffer;
        Book book = new Book(fileName);
        try {
            lecteurAvecBuffer = new BufferedReader(new FileReader(GenericHelper.BOOKS_PATH + "/" + fileName));
            String line;
            boolean findTitle = false;
            boolean findAuthor = false;
            boolean findReleaseDate = false;
            while ((line = lecteurAvecBuffer.readLine()) != null) {
                if (!findTitle) findTitle = GenericHelper.decorateBookWithTitle(line, book, findTitle);
                if (!findAuthor) findAuthor = GenericHelper.decorateBookWithAuthor(line, book, findAuthor);
                if (!findReleaseDate) findReleaseDate = GenericHelper.decorateBookWithReleaseDate(line, book, findReleaseDate);
                book.content.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return book;
    }

    public static Book getEmptyBook(String fileName) {
        BufferedReader lecteurAvecBuffer;
        Book book = new Book(fileName);
        try {
            lecteurAvecBuffer = new BufferedReader(new FileReader(GenericHelper.BOOKS_PATH + "/" + fileName));
            String line;
            boolean findTitle = false;
            boolean findAuthor = false;
            boolean findReleaseDate = false;
            boolean findLanguage = false;
            while ((line = lecteurAvecBuffer.readLine()) != null) {
                if (!findTitle) findTitle = GenericHelper.decorateBookWithTitle(line, book, findTitle);
                if (!findAuthor) findAuthor = GenericHelper.decorateBookWithAuthor(line, book, findAuthor);
                if (!findReleaseDate) findReleaseDate = GenericHelper.decorateBookWithReleaseDate(line, book, findReleaseDate);
                if (!findLanguage) findLanguage = GenericHelper.decorateBookWithLanguage(line, book, findLanguage);
                if (findTitle && findAuthor && findReleaseDate && findLanguage) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return book;
    }


}
