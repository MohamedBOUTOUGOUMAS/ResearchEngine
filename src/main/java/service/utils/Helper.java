package main.java.service.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class Helper {

    public static String BOOKS_PATH = "books-master";
    public static String BOOKS_INDEX = "index";
    public static String JACCARD_PATH = "jaccard";
    public static String JACCARD_MAP = "jaccard-map";
    public static String INDEXES_PATH = "indexes";
    public static String INDEXES_TABLES_PATH = "indexesTable";
    public static int NB_BOOKS = 1664;
    public static String PAGE_RANK_MAP = "page-rank-map";
    public static String FLOYD_WARSHALL = "floyd-warshall";




    public static ArrayList<String> readBooks(String path){
        ArrayList<String> books = new ArrayList<>();
        File repertoire = new File(path);
        String liste[] = repertoire.list();

        if (liste != null) {
            for (int i = 0; i < (Math.min(liste.length, NB_BOOKS)); i++) {
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

    public static boolean isRegEx(String word) {
        if (word.contains("(") || word.contains("+") || word.contains("*") || word.contains(".")) return true;
        return false;
    }

    public static String getPatternFromJson(String body){
        JsonObject payload = new JsonParser().parse(body).getAsJsonObject();
        String pattern = null;
        for (Map.Entry<String, JsonElement> e : payload.entrySet()){
            if (e.getKey().equals("regEx")) {
                pattern = e.getValue().getAsString();
            }
        }
        return pattern;
    }

    public static boolean getFastFromJson(String body){
        JsonObject payload = new JsonParser().parse(body).getAsJsonObject();
        boolean fast = false;
        for (Map.Entry<String, JsonElement> e : payload.entrySet()){
            if (e.getKey().equals("fast")) {
                fast = e.getValue().getAsBoolean();
            }
        }
        return fast;
    }

    public static String cleanText(String ligne) {
        // strips off all non-ASCII characters
        ligne = ligne.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        ligne = ligne.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        ligne = ligne.replaceAll("\\p{C}", "");
        ligne = ligne.trim();
        return ligne;
    }

}
