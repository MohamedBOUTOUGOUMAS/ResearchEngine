package db;

import com.mongodb.MongoClientURI;

public class DBStatic {

    public static String mongo_url = "localhost";
    public static String mongo_db = "research_engine";
    public static String mongo_index = "indexesTable";
    public static String mongo_meta = "metadata";
    public static MongoClientURI mongo_uri =
            new MongoClientURI("mongodb+srv://sargon:sargon@cluster0-icdko.mongodb.net/test?retryWrites=true&w=majority");
}
