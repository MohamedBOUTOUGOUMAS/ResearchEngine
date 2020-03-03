package db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metadata {

    public static void addClick(String fileName) {
        MongoCollection<Document> books = Database.getCollection("books");
        Document newDocument = new Document().append("$inc", new BasicDBObject().append("click", 1));
        books.updateOne(new Document().append("fileName", fileName), newDocument, (new UpdateOptions()).upsert(true));
        System.out.println("Done!");
    }

    public static Map<String, Integer> getListFromCursor(MongoCursor<Document> cursor) {
        Map<String, Integer> bookNbClickMap = new HashMap<>();
        while (cursor.hasNext()) {
            Document book = cursor.next();
            Object[] keys = book.keySet().toArray();
            Object[] values = book.values().toArray();

            if (keys.length == 3 && values.length == 3) {
                bookNbClickMap.put(values[1].toString(), Integer.parseInt(values[2].toString()));
            }
        }

        return bookNbClickMap;
    }

    public static Map<String, Integer> getNbClickBooks() {
        MongoCollection<Document> books = Database.getCollection("books");
        MongoCursor<Document> cursor = books.find().cursor();
        return getListFromCursor(cursor);
    }

    public static Map<String, Integer> getNbClickBooks(List<String> fileNames) {
        MongoCollection<Document> books = Database.getCollection("books");
        MongoCursor<Document> cursor = books.find(Filters.in("fileName", fileNames)).cursor();
        Map<String, Integer> result = getListFromCursor(cursor);
        for (String fileName : fileNames) {
            if (!result.containsKey(fileName)) result.put(fileName, 0);
        }
        return result;
    }

    public static Integer getNbClickBook(String fileName) {
        MongoCollection<Document> books = Database.getCollection("books");
        MongoCursor<Document> cursor = books.find(Filters.eq("fileName", fileName)).cursor();
        return getListFromCursor(cursor).get(fileName);
    }

    public static List<Object> getAutoComplete() {
        MongoCollection<Document> autoCompleteSearch = Database.getCollection("autoCompleteSearch");
        MongoCursor<Document> cursor = autoCompleteSearch.find().cursor();
        while (cursor.hasNext()) {
            Document autoC = cursor.next();
            return (List<Object>) autoC.values().toArray()[1];
        }
        return new ArrayList<>();
    }

    public static void addAutoCompleteSearch(String search) {
        MongoCollection<Document> autoCompleteSearch = Database.getCollection("autoCompleteSearch");
        Document newDocument = new Document().append("$addToSet", new BasicDBObject().append("autoComplete", search));
        autoCompleteSearch.updateOne(new Document(), newDocument, (new UpdateOptions()).upsert(true));
    }
}
