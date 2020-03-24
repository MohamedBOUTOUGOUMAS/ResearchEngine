package service.utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import db.DBStatic;
import db.Database;
import org.bson.Document;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Indexing {

    public static void makeMap() {

        ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);

        for (int f = 0; f < files.size(); f++) {
            String book = files.get(f);
            try {

                Map<String, Integer> dic = CleanData.getAllWordsFromFile(book);
                Map<String, Integer> result = dic.entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new));

                File indexDirectory = new File(Helper.INDEXES_TABLES_PATH + "/" + book);
                boolean createdDir = indexDirectory.mkdir();
                System.out.println(createdDir);
                if (createdDir) {

                    FileWriter[] fileWriters = new FileWriter[26];
                    BufferedWriter[] bufferedWriters = new BufferedWriter[26];
                    for (int i = 0; i < 26; i++) {
                        fileWriters[i] = new FileWriter(Helper.INDEXES_TABLES_PATH + "/" + book + "/" + i + ".txt");
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

        ArrayList<String> files = Helper.readBooks(Helper.INDEXES_TABLES_PATH);

        MongoDatabase dbBook = Database.getDB(Helper.getFileName(DBStatic.mongo_index));
        MongoCollection<Document> [] collections = new MongoCollection[26];
        for (int i = 0; i < 26; i++) {
            collections[i] = Database.getCollectionFromDB(dbBook, Helper.COLLECTION+i);
            for (int f = 0; f < files.size(); f++) {
                String book = files.get(f);
                System.out.println(book);
                Map<String, Integer> words;
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(Helper.INDEXES_TABLES_PATH +"/"+book+"/"+i+".txt"));
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

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //String txt = "123.txt.utf-8";

        //System.out.println(Helper.getFileName(txt));
        makeDB();
        //makeMap();
    }

}
