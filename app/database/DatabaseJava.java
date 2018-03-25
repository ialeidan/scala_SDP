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

    private  Configuration configuration = Play.current().injector().instanceOf(Configuration .class);


    MongoClientURI mongoClientURI;
    MongoClient mongoClient;
    MongoDatabase db;

    public DatabaseJava(){
        System.out.println(configuration.getString("mongodb.uri"));
        mongoClientURI = new MongoClientURI(configuration.getString("mongodb.uri"));
        mongoClient = new MongoClient(mongoClientURI);
        db = mongoClient.getDatabase("heroku_85mqw3gf");
    }

    public boolean  Add(){
        String s = "{ \"_id\": { \"$oid\": \"5ab673d6734d1d57bac4a670\" }, \"Customers\": { \"1\": { \"name\": \"Mohammed\", \"email\": \"ibrahim@example.com\", \"phone\": \"0500854443\" } } }";
        MongoCollection<Document> collection = db.getCollection("User");
        Document doc = Document.parse(s);
        collection.insertOne(doc);
        return true;
    }


    static Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };



    public void testRead(){
        MongoCollection<Document> collection = db.getCollection("restaurants");

        collection.find().forEach(printBlock);

    }

}
