package helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import db.Metadata;
import topics.Book;
import topics.ResearchResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenericHelper {

    public static String BOOKS_PATH = "books-master";
    public static String BOOKS_INDEX = "index";
    public static String JACCARD_PATH = "jaccard";
    public static String JACCARD_MAP = "jaccard-map";
    public static String INDEXES_PATH = "indexes";
    public static String INDEXES_TABLES_PATH = "indexesTable";
    public static int NB_BOOKS = 1664;
    public static String PAGE_RANK_MAP = "page-rank-map";
    public static String FLOYD_WARSHALL = "floyd-warshall";
    public static String COLLECTION = "Collection_";
    public static String METABOOKS = "metaBooks";
    public static String BETWEENNES_MAP = "betweennes-map";
    public static String SUGGESTIONS_MAP = "suggestions-map";


    public static ArrayList<String> readBooks(String path) {
        ArrayList<String> books = new ArrayList<>();
        File repertoire = new File(path);
        String[] liste = repertoire.list();
        //Set<String> rejectedFiles = CleanData.rejectedFiles();
        if (liste != null) {
            for (int i = 0; i < (Math.min(liste.length, NB_BOOKS)); i++) {
            //for (int i = 0; i < liste.length; i++) {
                //if (!rejectedFiles.contains(liste[i])) {
                    books.add(liste[i]);
                //}
            }
        } else {
            System.err.println("Nom de repertoire invalide");
        }
        return books;
    }

    public static String getTitleFromFile(String line) {
        if (line.contains("Title: ")) {
            String[] array = line.split("Title: ");
            return array[1];
        }
        return null;
    }

    public static String getAuthorFromFile(String line) {
        if (line.contains("Author: ")) {
            String[] array = line.split("Author: ");
            return array[1];
        }
        return null;
    }

    public static String getReleaseDateFromFile(String line) {
        if (line.contains("Release Date: ")) {
            String[] array = line.split("Release Date: ");
            return array[1];
        }
        return null;
    }

    private static String getLanguageFromFile(String line) {
        if (line.contains("Language: ")) {
            String[] array = line.split("Language: ");
            return array[1];
        }
        return null;
    }

    public static boolean decorateBookWithTitle(String ligne, Book book, boolean findTitle) {
        if (!findTitle) {
            String title = GenericHelper.getTitleFromFile(ligne);
            if (title != null) {
                book.title = title;
                return true;
            }
        }
        return false;
    }

    public static boolean decorateBookWithAuthor(String ligne, Book book, boolean findAuthor) {
        if (!findAuthor) {
            String autor = GenericHelper.getAuthorFromFile(ligne);
            if (autor != null) {
                book.author = autor;
                return true;
            }
        }
        return false;
    }

    public static boolean decorateBookWithReleaseDate(String ligne, Book book, boolean findReleaseDate) {
        if (!findReleaseDate) {
            String releaseDate = GenericHelper.getReleaseDateFromFile(ligne);
            if (releaseDate != null) {
                book.releaseDate = releaseDate;
                return true;
            }
        }
        return false;
    }

    public static boolean decorateBookWithLanguage(String line, Book book, boolean findLanguage) {
        if (!findLanguage) {
            String language = GenericHelper.getLanguageFromFile(line);
            if (language != null) {
                book.language = language;
                return true;
            }
        }
        return false;
    }

    public static boolean isRegEx(String word) {
        return word.contains("(") || word.contains("+") || word.contains("*") || word.contains(".");
    }

    public static JsonObject getJsonFromBody(String body){
        return new JsonParser().parse(body).getAsJsonObject();
    }

    public static String getFileName(String file) {
        String[] tokens = file.split("\\.");
        return tokens[0];
    }

    public static List<ResearchResult> sortBy(List<ResearchResult> results, String classification){
        if (classification != null && classification.equals("pgr")){
            results.sort((o1, o2) -> {
                if (o2.pageRank > o1.pageRank) return 1;
                else if (o2.pageRank < o1.pageRank) return -1;
                return 0;
            });
        }
        else if(classification.equals("btw")){
            results.sort((o1, o2) -> {
                if (o2.betweeness > o1.betweeness) return 1;
                else if (o2.betweeness < o1.betweeness) return -1;
                return 0;
            });
        }
        else if(classification.equals("nbClick")){
            List<String> fileNames = results.stream().map(rr -> rr.book.fileName).collect(Collectors.toList());
            Map<String, Integer> nbClick = Metadata.getNbClickBooks(fileNames);
            results.sort((o1, o2) -> nbClick.get(o2.book.fileName) - nbClick.get(o1.book.fileName));
        }
        else if(classification.equals("nbOccurs")){
            Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);
        }

        return results;
    }
}
