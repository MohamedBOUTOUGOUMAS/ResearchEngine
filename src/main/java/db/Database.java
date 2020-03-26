package db;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import controller.HomeController;
import org.bson.Document;
import topics.Book;
import helpers.GenericHelper;
import topics.ResearchResult;

import java.util.*;

import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.include;

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

    public static List<ResearchResult> matchDB(int firstLetter, String pattern){
        MongoDatabase dbBook = Database.getDB(GenericHelper.getFileName(DBStatic.mongo_index));
        MongoCollection<Document> collection = dbBook.getCollection(GenericHelper.COLLECTION+firstLetter);
        FindIterable<Document> res = collection.find(Filters.eq("words.word", pattern))
                .projection(new BasicDBObject("words.word.$", 1).append("fileName", 1).append("_id", 0));
        List<ResearchResult> l = new ArrayList<>();
        for(Document doc : res){
            ResearchResult rr = new ResearchResult(null, 0);
            l.add(rr);
            for (Map.Entry<String, Object> e : doc.entrySet()){
                if(e.getKey().equals("fileName")){
                    rr.book = Book.getEmptyBook((String) e.getValue());
                    Float rank = HomeController.pageRang.get(rr.book.fileName);
                    rr.pageRank = rank != null ? rank : 0F;
                    Float btw = HomeController.betweennes.get(rr.book.fileName);
                    rr.betweeness = btw != null ? btw : 0F;
                }
                if(e.getKey().equals("words")){
                    Document word = ((List<Document>)e.getValue()).get(0);
                    for (Map.Entry<String, Object> e1 : word.entrySet()){
                        if(e1.getKey().equals("nbOccurs")){
                            rr.nbMatched = (Integer) e1.getValue();
                        }
                    }
                }
            }
        }
        System.out.println(l.size());
        return l;
    }

    // { words.word: { $all: ["Sargon"] } }
    // { words.word: "Sargon" }
}
