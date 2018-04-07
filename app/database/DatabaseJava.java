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
        boolean noexist = collection.find(temp).first() == null;
        if(noexist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("Errors",("EMAIL_NOT_FOUND"));
                }
            };
            return ret;
        }
        temp.append("password", hash(doc.get("password").toString()));
        noexist = collection.find(temp).first() == null;
        if(noexist){
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


        boolean exist = collection.find(eq("email", Document.parse(json).get("email"))).first() != null;
        if(exist){
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
        boolean exist = collection.find(eq("email", Document.parse(json).get("email"))).first() != null;
        if(exist){
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

    public HashMap sendRequest(String json)throws NoSuchAlgorithmException  {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("code", "400");
                    put("error", "Error");
                    put("message", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        ///check if another request is running
        collection = db.getCollection("Requests");
        exist = collection.find(eq("customer_id", doc.get("user_id"))).first() != null;
        if(exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("request", "error");
                }
            };
            return ret;
        }
        ////else send success and request id
        Document temp = new Document();
        temp.append("customer_id", doc.get("user_id"));
        temp.append("service", doc.get("service"));
        temp.append("location : { latitude ", doc.get("location : { latitude "));
        temp.append("location : { longitude ", doc.get("location : { longitude "));
        collection.insertOne(temp);
        ObjectId id = (ObjectId)temp.get( "_id" );
        HashMap<String, Object> ret = new HashMap<String, Object>() {
            {
                put("request", "success");
                put("request_id", id.toHexString());
            }
        };
        return ret;
    }

    public HashMap [] getRequests(String json) {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object>[] ret = new HashMap[1];
            ret[0] = new HashMap<String, Object>() {
                {
                    put("Errors", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        ///count how many document are there
        collection = db.getCollection("Requests");
        int i = 0;
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                cursor.next();
                i++;
            }
        } finally {
            cursor.close();
        }
        ///adding document to the HashMap
        cursor = collection.find().iterator();
        try {
            HashMap<String, Object>[] ret = new HashMap[i];
            i = 0;
            while (cursor.hasNext()) {
                Document temp = cursor.next();
                ObjectId id = (ObjectId)temp.get( "_id" );
                ret[i] = new HashMap<String, Object>() {
                    {
                        put("request_id", id.toHexString());
                        put("customer_id", temp.get("customer_id"));
                        put("service", temp.get("service"));
                        put("info", temp.get("info"));
                        put("location : { latitude ", temp.get("location : { latitude "));
                        put("location : { longitude ", temp.get("location : { longitude "));
                    }
                };
                i++;
            }
            return ret;
        } finally {
            cursor.close();
        }
    }

    public HashMap sendBid(String json) throws NoSuchAlgorithmException{
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("code", "400");
                    put("error", "Error");
                    put("message", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        ///check if request still in the Requests collection
        collection = db.getCollection("Requests");
        exist = collection.find(eq("_id", doc.get("request_id"))).first() != null;
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("request", "error");
                }
            };
            return ret;
        }
        ///creating the bid and adding it to the collection
        Document request = collection.find(eq("_id", doc.get("request_id"))).first();
        collection = db.getCollection("Bid");
        Document bid = new Document();
        bid.append("request_id", doc.get("request_id"));
        bid.append("customer_id", request.get("customer_id"));
        bid.append("sp_id", doc.get("user_id"));
        bid.append("status", "waiting");
        bid.append("price", doc.get("price"));
        bid.append("location : { latitude ", doc.get("location : { latitude "));
        bid.append("location : { longitude ", doc.get("location : { longitude "));
        collection.insertOne(bid);
        HashMap<String, Object> ret = new HashMap<String, Object>() {
            {
                put("request", "success");
            }
        };
        return ret;
    }

    public HashMap [] getBids(String json) throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object>[] ret = new HashMap[1];
            ret[0] = new HashMap<String, Object>() {
                {
                    put("code", "400");
                    put("error", "Error");
                    put("message", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        collection = db.getCollection("Bids");
        ///count how many document are there
        int i = 0;
        MongoCursor<Document> cursor = collection.find(eq("customer_id",doc.get("user_id"))).iterator();
        try {
            while (cursor.hasNext()) {
                cursor.next();
                i++;
            }
        } finally {
            cursor.close();
        }
        ///adding document to the HashMap
        collection.createIndex(new Document("price", 1));
        cursor = collection.find(eq("customer_id",doc.get("user_id"))).iterator();
        try {
            HashMap<String, Object>[] ret = new HashMap[i];
            i = 0;
            while (cursor.hasNext()) {
                Document temp = cursor.next();
                ObjectId id = (ObjectId)temp.get( "_id" );
                ret[i] = new HashMap<String, Object>() {
                    {
                        put("bid_id", id.toHexString());
                        put("request_id", temp.get("request_id"));
                        put("customer_id", temp.get("customer_id"));
                        put("sp_id", temp.get("sp_id"));
                        put("status", temp.get("status"));
                        put("price", temp.get("price"));
                        put("location : { latitude ", temp.get("location\" : { latitude "));
                        put("location : { longitude ", temp.get("location\" : { latitude "));
                    }
                };
                i++;
            }
            return ret;
        } finally {
            cursor.close();
        }
    }

    public HashMap getBidStatus(String json) throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("code", "400");
                    put("error", "Error");
                    put("message", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        ///check status and return it
        collection = db.getCollection("Bids");
        Document bid = collection.find(and(eq("_id", doc.get("user_id")),eq("request_id","request_id"))).first();
        exist = bid.get("status").equals("waiting");
        if(exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("status", bid.get("status"));
                }
            };
            return ret;
        }
        else {
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("status", bid.get("status"));
                }
            };
            collection.deleteOne(and(eq("_id", doc.get("user_id")),eq("request_id","request_id")));
            return ret;

        }
    }

    public HashMap chooseBid(String json) throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("code", "400");
                    put("error", "Error");
                    put("message", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        ///check if request exist
        collection = db.getCollection("Requests");
        exist = collection.find(eq("user_id", doc.get("user_id"))).first() != null;
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("request", "error");
                }
            };
            return ret;
        }
        ///get the information of the request and the bidder
        collection = db.getCollection("Bids");
        Document bid = collection.find(eq("_id", doc.get("bid_id"))).first();
        collection = db.getCollection("Requests");
        Document request = collection.find(eq("_id", doc.get("request_id"))).first();

        ///move the request to the Progress collection
        collection = db.getCollection("Progress");
        Document temp = new Document();
        ObjectId id = (ObjectId)request.get( "_id" );
        temp.append("_id", id);
        temp.append("customer_id", request.get("customer_id"));
        temp.append("sp_id", bid.get("sp_id"));
        temp.append("service", bid.get("service"));
        temp.append("info", request.get("info"));
        temp.append("status", "in service");
        temp.append(" location : { latitude ", request.get(" location : { latitude "));
        temp.append(" location : { longitude ", request.get(" location : { longitude "));
        collection.insertOne(temp);
        collection = db.getCollection("Requests");
        collection.deleteOne(eq("_id", doc.get("user_id")));
        ///delete other bids from collection
        collection = db.getCollection("Bids");
        collection.updateMany(
                eq("request_id", request.get("_id")),
                combine(set("status", "canceled")));
        collection.updateOne(
                and(eq("sp_id", bid.get("sp_id")),eq("request_id",bid.get("request_id"))),
                combine(set("status","accepted")));
        HashMap<String, Object> ret = new HashMap<String, Object>(){
            {
                put("request", "success");
            }
        };
        return ret;
    }

    public HashMap getStatus(String json)throws NoSuchAlgorithmException  {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("code", "400");
                    put("error", "Error");
                    put("message", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        ////check if requesting
        collection = db.getCollection("Requests");
        exist = collection.find(eq("customer_id", doc.get("user_id"))).first() != null;
        if(exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("status", "requesting");
                }
            };
            return ret;
        }
        ////check if in service or waiting for payment
        collection = db.getCollection("Progress");
        exist = collection.find(eq("customer_id", doc.get("user_id"))).first() != null;
        if(exist){
            ////check if in service
            exist = doc.get("status").equals("in service");
            if(exist) {
                HashMap<String, Object> ret = new HashMap<String, Object>() {
                    {
                        put("status", "in service");
                    }
                };
                return ret;
            }
            ////check if waiting for payment
            exist = doc.get("status").equals("payment");
            if(exist) {
                HashMap<String, Object> ret = new HashMap<String, Object>() {
                    {
                        put("status", "payment");
                    }
                };
                return ret;
            }
        }
        ////if not all above, return not on service
        HashMap<String, Object> ret = new HashMap<String, Object>() {
            {
                put("status", "not on service");
            }
        };
        return ret;
    }

    public HashMap getService(String json) throws NoSuchAlgorithmException  {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("code", "400");
                    put("error", "Error");
                    put("message", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        collection = db.getCollection("Progress");
        Document progress = collection.find(eq("_id", doc.get("user_id"))).first();
        exist = progress.get("status").equals("in service");
        if(exist) {
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("service", progress.get("service"));
                    put("location : { latitude ", progress.get("location : { latitude "));
                    put("location : { longitude ", progress.get("location : { longitude "));
                }
            };
            return ret;
        }
        exist = progress.get("status").equals("payment");
        if(exist) {
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("service", progress.get("service"));
                    put("payment", progress.get("payment"));
                    put("sp_id", progress.get("sp_id"));
                }
            };
            return ret;
        }
        else {
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("request", "error");
                }
            };
            return ret;
        }
    }

    public HashMap sendPayment(String json)throws NoSuchAlgorithmException  {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("code", "400");
                    put("error", "Error");
                    put("message", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        ///moving the document from progress to history
        collection = db.getCollection("Progress");
        Document progress = collection.find(eq("_id", doc.get("user_id"))).first();
        progress.append("info", doc.get("payment"));
        progress.append("rating", doc.get("Rating"));
        progress.append("timestamp", "0");
        progress.remove("status");
        collection = db.getCollection("History");
        collection.insertOne(progress);
        collection = db.getCollection("Progress ");
        collection.deleteOne(eq("_id", doc.get("user_id")));
        HashMap<String, Object> ret = new HashMap<String, Object>() {
            {
                put("request", "success");
            }
        };
        return ret;

    }

    public HashMap [] getHistory(String json){
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", doc.get("user_id"))).first() != null;
        ////check if user authenticated
        if(!exist){
            HashMap<String, Object>[] ret = new HashMap[1];
            ret[0] = new HashMap<String, Object>() {
                {
                    put("Errors", "NOT_AUTHENTICATED");
                }
            };
            return ret;
        }
        ///count how many document are there
        collection = db.getCollection("History");
        int i = 0;
        MongoCursor<Document> cursor = collection.find(eq("customer_id",doc.get("user_id"))).iterator();
        try {
            while (cursor.hasNext()) {
                cursor.next();
                i++;
            }
        } finally {
            cursor.close();
        }
        ///adding document to the HashMap
        cursor = collection.find(eq("customer_id",doc.get("user_id"))).iterator();
        try {
            HashMap<String, Object>[] ret = new HashMap[i];
            i = 0;
            while (cursor.hasNext()) {
                Document temp = cursor.next();
                ObjectId id = (ObjectId)temp.get( "_id" );
                ret[i] = new HashMap<String, Object>() {
                    {
                        put("request_id", id.toHexString());
                        put("customer_id", temp.get("customer_id"));
                        put("sp_id", temp.get("sp_id"));
                        put("service", temp.get("service"));
                        put("info", temp.get("info"));
                        put("rating", temp.get("rating"));
                        put("timestamp", temp.get("timestamp"));
                        put("location : { from : { latitude ", temp.get("location : { from : { latitude "));
                        put("location : { from : { longitude ", temp.get("location : { from : { longitude "));
                        put("location : { to : { latitude ", temp.get("location : { to : { latitude "));
                        put("location : { to : { longitude ", temp.get("location : { to : { longitude "));
                    }
                };
                i++;
            }
            return ret;
        } finally {
            cursor.close();
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
