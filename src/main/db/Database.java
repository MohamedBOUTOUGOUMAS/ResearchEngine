package main.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {

    public static MongoCollection<Document> getCollection(String collection) {
        MongoCollection<Document> msg = null;
        try {
            MongoClient mongoClient = new MongoClient(DBStatic.mongo_url);
            MongoDatabase database = mongoClient.getDatabase(DBStatic.mongo_db);
            msg = database.getCollection(collection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }
}
