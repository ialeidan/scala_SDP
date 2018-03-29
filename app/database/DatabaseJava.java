package database;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.casbah.commons.ValidBSONType;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import play.Configuration;
import play.api.Play;

import javax.inject.Inject;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

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


    public Document getToken(String email){
        MongoCollection<Document> collection = db.getCollection("Users");
        Document found = (Document) collection.find(eq("email", email)).first();
        Document temp = new Document();
//        temp.append("access_token", found.get("access_token"));
//        temp.append("user_id", found.get("user_id"));
        ObjectId id = (ObjectId)found.get( "_id" );
        temp.append("access_token", id.toHexString());
        temp.append("user_id", id.toHexString());
        return temp;
    }

    public HashMap Login(String json) throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        Document temp = new Document();
        temp.append("email", doc.get("email"));
        boolean check = collection.find(temp).first() == null;
        if(check){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("Errors",("EMAIL_NOT_FOUND"));
                }
            };
            return ret;
        }
        temp.append("password", hash(doc.get("password").toString()));
        check = collection.find(temp).first() == null;
        if(check){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("Errors",("INVALID_PASSWORD"));
                }
            };
            return ret;
        }
        Document token = getToken(doc.getString("email"));
        HashMap<String, Object> ret = new HashMap<String, Object>() {
            {
                put("access_token", token.get("access_token"));
                put("user_id", token.get("user_id"));
            }
        };
        return ret;
    }

    public HashMap register(String json)throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");


        boolean check = collection.find(eq("email", Document.parse(json).get("email"))).first() != null;
        if(check){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("Errors",("EMAIL_EXISTS"));
                }
            };
            return ret;
        }
        else {
            Document doc = Document.parse(json);
            doc.append("password", hash(doc.get("password").toString()));
            doc.append("type", "customer");
            collection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("access_token", id.toHexString());
                    put("user_id", id.toHexString());
                }
            };
            return ret;
        }
    }

    public HashMap spRegister(String json)throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");
        boolean check = collection.find(eq("email", Document.parse(json).get("email"))).first() != null;
        if(check){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("Errors",("EMAIL_EXISTS"));
                }
            };
            return ret;
        }
        else {
            Document doc = Document.parse(json);
            doc.append("password", hash(doc.get("password").toString()));
            doc.append("type", "SP");
            doc.append("device", doc.get("device"));
            collection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("access_token", id.toHexString());
                    put("user_id", id.toHexString());
                }
            };
            return ret;
        }
    }

    public String hash(String pass) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                pass.getBytes(StandardCharsets.UTF_8));
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
