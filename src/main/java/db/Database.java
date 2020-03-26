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

        /*Bson lookup = new Document("$lookup",
                new Document("from", GenericHelper.METABOOKS)
                        .append("localField", "fileName")
                        .append("foreignField", "fileName")
                        .append("as", "join"));

        List<Bson> filters = new ArrayList<>();
        filters.add(lookup);
        filters.add(Aggregates.unwind("$words"));
        filters.add(Aggregates.match(Filters.eq("words.word", pattern)));

        AggregateIterable<Document> res = collection.aggregate(filters);*/

        FindIterable<Document> res = collection.find(Filters.eq("words.word", pattern))
                .projection(new BasicDBObject("words.word.$", 1).append("fileName", 1).append("_id", 0));
        List<ResearchResult> l = new ArrayList<>();

        for(Document doc : res){
            ResearchResult rr = new ResearchResult(null, 0);
            l.add(rr);
            for (Map.Entry<String, Object> e : doc.entrySet()){
                if(e.getKey().equals("fileName")){
                    rr.book = Book.getEmptyBook((String) e.getValue());
                    Float rank = HomeController.pageRang.get(e.getValue());
                    rr.pageRank = rank != null ? rank : 0F;
                    Float btw = HomeController.betweennes.get(e.getValue());
                    rr.betweeness = btw != null ? btw : 0F;
                }
                if(e.getKey().equals("words")){
                    //Document word = (Document)e.getValue();
                    Document word = ((List<Document>)e.getValue()).get(0);
                    for (Map.Entry<String, Object> e1 : word.entrySet()){
                        if(e1.getKey().equals("nbOccurs")){
                            rr.nbMatched = (Integer) e1.getValue();
                        }
                    }
                }
                /*if(e.getKey().equals("join")){
                    Document emptyBook = ((List<Document>) e.getValue()).get(0);
                    for (Map.Entry<String, Object> e1 : emptyBook.entrySet()){
                        if(e1.getKey().equals("fileName")){
                            rr.book.fileName = (String) e1.getValue();
                        }
                        if(e1.getKey().equals("title")){
                            rr.book.title = (String) e1.getValue();
                        }
                        if(e1.getKey().equals("author")){
                            rr.book.author = (String) e1.getValue();
                        }
                        if(e1.getKey().equals("releaseDate")){
                            rr.book.releaseDate = (String) e1.getValue();
                        }
                    }
                }*/
            }
        }
        return l;
    }

    // { words.word: { $all: ["Sargon"] } }
    // { words.word: "Sargon" }
}
