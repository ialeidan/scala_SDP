package database;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import play.Configuration;
import play.api.Play;

import javax.inject.Inject;
import org.bson.Document;

public class DatabaseJava{

    @Inject
    private static Configuration configuration;

    static MongoClientURI mongoClientURI = new MongoClientURI(configuration.getString("mongodb.uri"));
    static MongoClient mongoClient = new MongoClient(mongoClientURI);
    static MongoDatabase db = mongoClient.getDatabase("heroku_85mqw3gf");

    static Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

    public static void testRead(){
        MongoCollection<Document> collection = db.getCollection("restaurants");

        collection.find().forEach(printBlock);

    }

}
