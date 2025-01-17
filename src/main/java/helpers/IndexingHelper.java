package helpers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import db.DBStatic;
import db.Database;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import topics.Book;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class IndexingHelper {

    public static void createIndex() {

        ArrayList<String> files = GenericHelper.readBooks(GenericHelper.BOOKS_PATH);

        for (int f = 0; f < files.size(); f++) {
            String book = files.get(f);
            try {

                Map<String, Integer> dic = DataHelper.getAllWordsFromFile(book);
                Map<String, Integer> result = dic.entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new));

                File indexDirectory = new File(GenericHelper.INDEXES_TABLES_PATH + "/" + book);
                boolean createdDir = indexDirectory.mkdir();
                System.out.println(createdDir);
                if (createdDir) {

                    FileWriter[] fileWriters = new FileWriter[26];
                    BufferedWriter[] bufferedWriters = new BufferedWriter[26];
                    for (int i = 0; i < 26; i++) {
                        fileWriters[i] = new FileWriter(GenericHelper.INDEXES_TABLES_PATH + "/" + book + "/" + i + ".txt");
                        bufferedWriters[i] = new BufferedWriter(fileWriters[i]);
                    }

                    for (Entry<String, Integer> e : result.entrySet()) {
                        if (e.getKey().length() > 3 && e.getValue() < 100) {
                            int asciiCode = (e.getKey().toUpperCase().charAt(0) - 65);
                            bufferedWriters[asciiCode].write(e.getKey() + " " + e.getValue().toString() + "\n");
                        }
                    }

                    for (int i = 0; i < 26; i++) {
                        bufferedWriters[i].close();
                        fileWriters[i].close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("End");
    }

    public static void makeDB() {

        ArrayList<String> files = GenericHelper.readBooks(GenericHelper.INDEXES_TABLES_PATH);

        MongoDatabase dbBook = Database.getDB(GenericHelper.getFileName(DBStatic.mongo_index));
        MongoCollection<Document> [] collections = new MongoCollection[26];
        for (int i = 0; i < 26; i++) {
            collections[i] = Database.getCollectionFromDB(dbBook, GenericHelper.COLLECTION+i);
            for (int f = 0; f < files.size(); f++) {
                String book = files.get(f);
                System.out.println(book);
                Map<String, Integer> words;
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(GenericHelper.INDEXES_TABLES_PATH +"/"+book+"/"+i+".txt"));
                    words = reader.lines()
                            .map(line -> line.split(" "))
                            .map(line -> new AbstractMap.SimpleEntry<>(line[0], Integer.parseInt(line[1])))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    reader.close();

                    Database.bulkInsert(collections[i], book, words);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("End");
    }

    public static void addMetadataToDB(){
        ArrayList<String> files = GenericHelper.readBooks(GenericHelper.INDEXES_TABLES_PATH);
        MongoDatabase dbBook = Database.getDB(GenericHelper.getFileName(DBStatic.mongo_index));
        MongoCollection<Document> collection = Database.getCollectionFromDB(dbBook, GenericHelper.METABOOKS);
        for (String fileName : files){
            Book modelBook = Book.getEmptyBook(fileName);
            Document b = new Document("fileName", fileName)
                    .append("title", modelBook.title)
                    .append("author", modelBook.author)
                    .append("releaseDate", modelBook.releaseDate);
            collection.insertOne(b);
        }
    }

    public static void addDBIndexes(){
        MongoDatabase dbBook = Database.getDB(GenericHelper.getFileName(DBStatic.mongo_index));
        for (int i = 0; i < 26; i++) {
            MongoCollection<Document> collection = Database.getCollectionFromDB(dbBook, GenericHelper.COLLECTION+i);
            collection.createIndex(new Document("words.word", "text"));
        }
        MongoCollection<Document> collection = Database.getCollectionFromDB(dbBook, GenericHelper.METABOOKS);
        collection.createIndex(new Document("title", "text").append("author", "text"));
        System.out.println("End");
    }

    public static void main(String[] args) {
        createIndex();
        //makeDB();
        //addMetadataToDB();
        //addDBIndexes();
    }

}
