package main.java.db;

import com.mongodb.MongoClientURI;

public class DBStatic {

    public static String mongo_url = "localhost";
    public static String mongo_db = "research_engine";

    public static MongoClientURI mongo_uri = new MongoClientURI(
            "mongodb+srv://sargon:sargon@daar-cnwfd.mongodb.net/test?retryWrites=true&w=majority");
}
