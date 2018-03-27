package database;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import play.Configuration;
import play.api.Play;

import javax.inject.Inject;
import org.bson.Document;

import java.util.HashMap;

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
        String s = "{\"Name\": \"Mohammed\"}";
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(s);
        collection.insertOne(doc);
        return true;
    }

    public String Return(String json){
        MongoCollection<Document> collection = db.getCollection("User");
        Document found = (Document) collection.find(Document.parse(json)).first();
        Document temp = new Document();
        temp.append("email", found.get("email"));
        return temp.toJson();
    }

    public HashMap test(String email){

        HashMap<String, Object> ret = new HashMap<String, Object>(){
            {
                put("id","");
                put("username", "");
            }
        };

        return ret;

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
