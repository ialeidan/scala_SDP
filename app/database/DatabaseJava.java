package database;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.casbah.commons.ValidBSONType;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import play.Configuration;
import play.api.Play;

import javax.inject.Inject;
import javax.print.Doc;

import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        temp.append("type", found.get("type"));
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
                put("type", token.get("type"));
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
                    put("type", "customer");
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
                    put("type", "SP");
                }
            };
            return ret;
        }
    }

    public HashMap sendRequest(String json)throws NoSuchAlgorithmException  {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", new ObjectId(doc.getString("user_id")))) != null;
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
        temp.append("location", doc.get("location"));
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
//
    public HashMap [] getRequests(String user_id) throws NoSuchAlgorithmException{
        MongoCollection<Document> collection = db.getCollection("Users");
        String json = "{\"user_id\": \"" + user_id + "\" }";

        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", new ObjectId(user_id))) != null;
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

                String response = temp.toJson().toString();
                HashMap<String, Object> resultMap = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>(){}.getType());

                resultMap.put("request_id", id.toHexString());

                ret[i] = resultMap;

                i++;
            }
            return ret;
        } finally {
            cursor.close();
        }
    }
//
    public HashMap sendBid(String json) throws NoSuchAlgorithmException{
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", new ObjectId(doc.getString("user_id")))).first() != null;
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
        collection = db.getCollection("Users");
        Document user = collection.find(eq("_id", new ObjectId(doc.getString("user_id")))).first();

        ///check if request still in the Requests collection
        collection = db.getCollection("Requests");
        exist = collection.find(eq("_id", new ObjectId(doc.getString("request_id")))).first() != null;
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("request", "error");
                }
            };
            return ret;
        }
        ///creating the bid and adding it to the collection
        Document request = collection.find(eq("_id", new ObjectId(doc.getString("request_id")))).first();
        collection = db.getCollection("Bid");
        Document bid = new Document();
        bid.append("request_id", doc.get("request_id"));
        bid.append("customer_id", request.get("customer_id"));
        bid.append("sp_id", doc.get("user_id"));
        bid.append("status", "waiting");
        bid.append("price", doc.get("price"));
        bid.append("location", request.get("location"));
        bid.append("device", user.get("device"));
        collection.insertOne(bid);
        // usr: 5abbff30eed4650004e1588e
        // sp ; 5abc8a5002ba7f0004585fb6
        // req: 5ace5758cfaeee000408b0ee


        HashMap<String, Object> ret = new HashMap<String, Object>() {
            {
                put("request", "success");
            }
        };
        return ret;
    }
//
    public HashMap [] getBids(String user_id) throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");
        String json = "{\"user_id\": \"" + user_id + "\" }";

        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", new ObjectId(user_id))) != null;
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
        collection = db.getCollection("Bid");
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

                String response = temp.toJson().toString();
                HashMap<String, Object> resultMap = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>(){}.getType());

                resultMap.put("bid_id", id.toHexString());

                ret[i] = resultMap;

                i++;
            }
            return ret;
        } finally {
            cursor.close();
        }
    }
//
    public HashMap getBidStatus(String user_id, String request_id) throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");
        String json = "{\"user_id\": \"" + user_id + "\", \"request_id\": \"" + request_id + "\" }";

        Document doc = Document.parse(json);
        //TODO: Check this,
        boolean exist = collection.find(eq("_id", new ObjectId(user_id))) != null;
        ////check if user authenticated
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
        collection = db.getCollection("Bid");
        Document bid = collection.find((eq("request_id",doc.get("request_id")))).first();
//        exist = bid.get("status").equals("waiting");


        String status = bid.getString("status");
        if(status.equals("waiting")){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("status", bid.get("status"));
                }
            };
            return ret;
        }
        else if(status.equals("accepted")){
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("status", bid.get("status"));
                }
            };
            collection.deleteOne((eq("request_id",doc.get("request_id"))));
            return ret;

        }else{
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {

                    put("bb", status);
                    put("status", "canceled");
                }
            };
            return ret;
        }
    }
//
    public HashMap chooseBid(String json) throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", new ObjectId(doc.getString("user_id")))).first() != null;
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
        exist = collection.find(eq("customer_id", doc.get("user_id"))).first() != null;
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("request", "error");
                }
            };
            return ret;
        }
        ///get the information of the request and the bidder
        collection = db.getCollection("Bid");
        Document bid = collection.find(eq("_id", new ObjectId(doc.getString("bid_id")))).first();
        collection = db.getCollection("Requests");
        Document request = collection.find(eq("_id", new ObjectId(bid.getString("request_id")))).first();

        ///move the request to the Progress collection
        collection = db.getCollection("Progress");
        Document temp = new Document();
        ObjectId id = (ObjectId)request.get( "_id" );
        temp.append("_id", id);
        temp.append("customer_id", request.get("customer_id"));
        temp.append("sp_id", bid.get("sp_id"));
        temp.append("service", request.get("service"));
        temp.append("info", request.get("info"));
        temp.append("price", bid.get("price"));
        temp.append("status", "in service");
        temp.append("location", request.get("location"));
        temp.append("request_id", id.toHexString());
        temp.append("device", bid.get("device"));
        temp.append("device_status", "unlocked");
        collection.insertOne(temp);

        collection = db.getCollection("Requests");
        collection.deleteOne(eq("_id", new ObjectId(bid.getString("request_id"))));
        ///delete other bids from collection
        collection = db.getCollection("Bid");
        collection.updateMany(
                eq("request_id", bid.get("request_id")),
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
//
    public HashMap getStatus(String user_id)throws NoSuchAlgorithmException  {
        MongoCollection<Document> collection = db.getCollection("Users");

        String json = "{\"user_id\": \"" + user_id + "\" }";

        Document doc = Document.parse(json);
        //TODO: Check this,
        boolean exist = collection.find(eq("_id", new ObjectId(user_id))) != null;
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
        //TODO: CHECK.
        ////check if in service or waiting for payment
        collection = db.getCollection("Progress");
        Document pro = collection.find(eq("customer_id", doc.get("user_id"))).first();
        exist = collection.find(eq("customer_id", doc.get("user_id"))).first() != null;
        if(exist){
            ////check if in service

            exist = pro.get("status").equals("in service");
            if(exist) {
                HashMap<String, Object> ret = new HashMap<String, Object>() {
                    {
                        put("status", "in service");
                    }
                };
                return ret;
            }
            ////check if waiting for payment
            exist = pro.get("status").equals("payment");
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
                put("status", "not service");
            }
        };
        return ret;
    }
//
    public HashMap getSPStatus(String user_id)throws NoSuchAlgorithmException  {
    MongoCollection<Document> collection = db.getCollection("Users");

    String json = "{\"user_id\": \"" + user_id + "\" }";

    Document doc = Document.parse(json);
    //TODO: Check this,
    boolean exist = collection.find(eq("_id", new ObjectId(user_id))) != null;
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
    exist = collection.find(eq("sp_id", doc.get("user_id"))).first() != null;
    if(exist){
        HashMap<String, Object> ret = new HashMap<String, Object>(){
            {
                put("status", "requesting");
            }
        };
        return ret;
    }
    //TODO: CHECK.
    ////check if in service or waiting for payment
    collection = db.getCollection("Progress");
    Document pro = collection.find(eq("sp_id", doc.get("user_id"))).first();
    exist = collection.find(eq("sp_id", doc.get("user_id"))).first() != null;
    if(exist){
        ////check if in service

        exist = pro.get("status").equals("in service");
        if(exist) {
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("status", "in service");
                }
            };
            return ret;
        }
        ////check if waiting for payment
        exist = pro.get("status").equals("payment");
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
            put("status", "not service");
        }
    };
    return ret;
}

    public HashMap getService(String user_id) throws NoSuchAlgorithmException  {
        MongoCollection<Document> collection = db.getCollection("Users");
        String json = "{\"user_id\": \"" + user_id + "\" }";

        Document doc = Document.parse(json);
        //TODO: Check this,
        boolean exist = collection.find(eq("_id", new ObjectId(user_id))) != null;
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
        Document progress = collection.find(eq("customer_id", doc.get("user_id"))).first();
        exist = progress.get("status").equals("in service");
        if(exist) {
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("service", progress.get("service"));
                    put("location", progress.get("location"));
                }
            };
            return ret;
        }
        exist = progress.get("status").equals("payment");
        if(exist) {
            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("service", progress.get("service"));
                    put("price", progress.get("price"));
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

    public HashMap endService(String json) throws NoSuchAlgorithmException {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", new ObjectId(doc.getString("user_id")))).first() != null;
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
        collection = db.getCollection("Progress");
        exist = collection.find(eq("sp_id", doc.get("user_id"))).first() != null;
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("request", "error");
                }
            };
            return ret;
        }
        ///get the information of the request and the bidder
        collection = db.getCollection("Progress");
        Document bid = collection.find(eq("sp_id", doc.get("user_id"))).first();

        ///move the request to the Progress collection

        collection.updateOne(
                (eq("request_id", doc.get("request_id"))),
                combine(set("status","payment")));
        HashMap<String, Object> ret = new HashMap<String, Object>(){
            {
                put("request", "success");
            }
        };
        return ret;
    }
//
    public HashMap sendPayment(String json)throws NoSuchAlgorithmException  {
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", new ObjectId(doc.getString("user_id")))).first() != null;
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
        Document progress = collection.find(eq("customer_id", doc.get("user_id"))).first();
        progress.append("info", doc.get("payment"));
        progress.append("rating", doc.get("rating"));
//        progress.append("timestamp", doc.get("rating"));
        progress.append("status", "completed");
//        progress.remove("status");
        collection = db.getCollection("History");
        collection.insertOne(progress);
        collection = db.getCollection("Progress");
        collection.deleteOne(eq("customer_id", doc.get("user_id")));
        HashMap<String, Object> ret = new HashMap<String, Object>() {
            {
                put("request", "success");
            }
        };
        return ret;

    }

    public HashMap [] getHistory(String user_id) throws NoSuchAlgorithmException{
        MongoCollection<Document> collection = db.getCollection("Users");

        String json = "{\"user_id\": \"" + user_id + "\" }";

        Document doc = Document.parse(json);
        boolean exist = collection.find(eq("_id", new ObjectId(user_id))) != null;
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

                String response = temp.toJson().toString();
                HashMap<String, Object> resultMap = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>(){}.getType());

                ret[i] = resultMap;
                i++;
            } //5abbff30eed4650004e1588e
            return ret;
        } finally {
            cursor.close();
        }

    }

    public HashMap sendLock(String json) throws NoSuchAlgorithmException{
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        ///check if request exist
        collection = db.getCollection("Progress");
        boolean exist = collection.find(eq("device", doc.get("device_id"))).first() != null;
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("request", "error");
                }
            };
            return ret;
        }
        ///get the information of the request and the bidder
        collection = db.getCollection("Progress");
        Document progress = collection.find(eq("device", doc.get("device_id"))).first();

        collection.updateOne(
                eq("device", doc.get("device_id")),
                combine(set("device_status",doc.get("status"))));
        HashMap<String, Object> ret = new HashMap<String, Object>(){
            {
                put("request", "success");
            }
        };
        return ret;
    }

    public HashMap sendDeviceLocation(String json) throws NoSuchAlgorithmException{
        MongoCollection<Document> collection = db.getCollection("Users");
        Document doc = Document.parse(json);
        ///check if request exist
        collection = db.getCollection("Progress");
        boolean exist = collection.find(eq("device", doc.get("device_id"))).first() != null;
        if(!exist){
            HashMap<String, Object> ret = new HashMap<String, Object>(){
                {
                    put("request", "error");
                }
            };
            return ret;
        }
        ///get the information of the request and the bidder
        collection = db.getCollection("Progress");
        Document progress = collection.find(eq("device", doc.get("device_id"))).first();

        collection.updateMany(
                eq("device", doc.get("device_id")),
                combine(set("device_status",doc.get("status")), set("location", doc.get("location"))));
        HashMap<String, Object> ret = new HashMap<String, Object>(){
            {
                put("request", "success");
            }
        };
        return ret;
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
