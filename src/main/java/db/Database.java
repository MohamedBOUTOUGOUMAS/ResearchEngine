package db;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import service.utils.Helper;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Projections.exclude;

public class Database {

    public static MongoCollection<Document> getCollection(String collection) {
        return getDB(DBStatic.mongo_db).getCollection(collection);
    }

    public static MongoCollection<Document> getCollectionFromDB(MongoDatabase db, String collection) {
        return db.getCollection(collection);
    }

    public static  MongoDatabase getDB(String db){
        //MongoClient mongoClient = new MongoClient(DBStatic.mongo_url);
        MongoClient mongoClient = new MongoClient(DBStatic.mongo_uri);
        return mongoClient.getDatabase(db);
    }

    public static void addWordNbOccurs(MongoCollection<Document> collection, String book, String word, int nbOccurs) {
        Document newDocument = new Document().append("fileName", book).append(word, nbOccurs);
        collection.insertOne(newDocument);
    }

    public static void bulkInsert(MongoCollection<Document> collection, String book, Map<String, Integer> words) {
        List<BasicDBObject> l = new ArrayList<>();
        for (Map.Entry<String, Integer> e : words.entrySet()){
            BasicDBObject w = new BasicDBObject("word", e.getKey()).append("nbOccurs", e.getValue());
            l.add(w);
        }
        Document newDocument = new Document().append("fileName", book).append("words", l);
        collection.insertOne(newDocument);
    }

    public static List<String> matchDB(int firstLetter, String pattern){
        MongoDatabase dbBook = Database.getDB(Helper.getFileName(DBStatic.mongo_index));
        MongoCollection<Document> collection = dbBook.getCollection(Helper.COLLECTION+firstLetter);
        FindIterable<Document> res = collection.find(Filters.eq("words.word", pattern)).projection(exclude("_id", "words"));
        List<String> l = new ArrayList<>();
        for(Document doc : res){
            for (Map.Entry<String, Object> e : doc.entrySet()){
                l.add((String) e.getValue());
            }
        }
        return l;
    }

    // { words.word: { $all: ["Sargon"] } }
    // { words.word: "Sargon" }
}
