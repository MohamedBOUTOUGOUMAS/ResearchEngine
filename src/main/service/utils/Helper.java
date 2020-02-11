package main.service.utils;

import java.io.File;
import java.util.ArrayList;

public class Helper {

    public static String BOOKS_PATH = "books-master";

    public static ArrayList<String> readBooks(){
        ArrayList<String> books = new ArrayList<>();
        File repertoire = new File(Helper.BOOKS_PATH);
        String liste[] = repertoire.list();

        if (liste != null) {
            for (int i = 0; i < liste.length; i++) {
                books.add(liste[i]);
            }
        } else {
            System.err.println("Nom de repertoire invalide");
        }
        return books;
    }

    public static String getTitleFromFile(String line){
        if (line.contains("Title: ")){
            String [] array = line.split("Title: ");
            return array[1];
        }
        return null;
    }

    public static String getAuthorFromFile(String line){
        if (line.contains("Author: ")){
            String [] array = line.split("Author: ");
            return array[1];
        }
        return null;
    }

    public static String getReleaseDateFromFile(String line){
        if (line.contains("Release Date: ")){
            String [] array = line.split("Release Date: ");
            return array[1];
        }
        return null;
    }

    public static boolean decorateBookWithTitle(String ligne, Book book, boolean findTitle){
        if (!findTitle){
            String title = Helper.getTitleFromFile(ligne);
            if (title != null) {
                book.title = title;
                return true;
            }
        }
        return false;
    }

    public static boolean decorateBookWithAuthor(String ligne, Book book, boolean findAuthor){
        if (!findAuthor){
            String autor = Helper.getAuthorFromFile(ligne);
            if (autor != null) {
                book.author = autor;
                return true;
            }
        }
        return false;
    }

    public static boolean decorateBookWithReleaseDate(String ligne, Book book, boolean findReleaseDate){
        if (!findReleaseDate){
            String releaseDate = Helper.getReleaseDateFromFile(ligne);
            if (releaseDate != null) {
                book.releaseDate = releaseDate;
                return true;
            }
        }
        return false;
    }

}
