package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import database.DatabaseJava;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class ApplicationJava extends Controller {


    private DatabaseJava database = new DatabaseJava();

    public Result index1() {


        return ok("{\"name\": \"Mohammed\"}");


    }

    public Result register() {
        JsonNode reg = request().body().asJson();

        if (reg.get("email") == null || reg.get("password") == null || reg.get("phone") == null || reg.get("username") == null) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }


        try {
            HashMap<String, Object> ret = database.register(reg.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }

    }

    public Result spRegister() {
        JsonNode spReg = request().body().asJson();

        if (spReg.get("email") == null || spReg.get("password") == null || spReg.get("phone") == null || spReg.get("username") == null || spReg.get("device") == null) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.spRegister(spReg.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }

    }

    public Result login() {
        JsonNode spReg = request().body().asJson();

        if (spReg.get("email") == null || spReg.get("password") == null) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.Login(spReg.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }

    }

    public Result history(String user_id) {
//        JsonNode hist = request().body().asJson();

//        if (hist.get("User_id") == null) {
        if (user_id == ""){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "User Id FIELDS is MISSING");
                }

            };
            return ok(Json.toJson(ERR));
        }
        try {
            HashMap<String, Object>[] retDB = database.getHistory(user_id);

            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("history", retDB);
                }
            };

            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));
        }
    }

    public Result status(String user_id) {
//        JsonNode status = request().body().asJson();

//        if (status.get("user_id") == null) {
        if (user_id == ""){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.getStatus(user_id);//status function
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }

    public Result spStatus(String user_id) {
//        JsonNode status = request().body().asJson();

//        if (status.get("user_id") == null) {
        if (user_id == ""){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.getSPStatus(user_id);//status function
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }

    public Result sendrequest() {
        JsonNode request = request().body().asJson();

        if (request.get("user_id") == null || request.get("service") == null || request.get("location") == null){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.sendRequest(request.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
    public Result getBids(String user_id) {
//        JsonNode request = request().body().asJson();

//        if (hist.get("User_id") == null) {
        if (user_id == ""){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object>[] retDB = database.getBids(user_id);

            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("bids", retDB);
                }
            };

            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
//
    public Result chooseBid() {
        JsonNode request = request().body().asJson();

        if (request.get("user_id") == null || request.get("bid_id") == null) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.chooseBid(request.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
    public Result getService(String user_id) {
//        JsonNode status = request().body().asJson();

//        if (status.get("user_id") == null) {
        if (user_id == ""){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.getService(user_id);
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
    public Result sendPayment() {
        JsonNode request = request().body().asJson();

        if (request.get("user_id") == null || request.get("payment") == null  || request.get("rating") == null ){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.sendPayment(request.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
    public Result sendBid() {
        JsonNode request = request().body().asJson();

        if (request.get("user_id") == null || request.get("request_id") == null  || request.get("price") == null){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.sendBid(request.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
    public Result getRequest(String user_id) {
//        JsonNode request = request().body().asJson();

//        if (hist.get("User_id") == null) {
        if (user_id == ""){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object>[] retDB = database.getRequests(user_id);

            HashMap<String, Object> ret = new HashMap<String, Object>() {
                {
                    put("requests", retDB);
                }
            };


            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
//
    public Result getBidStatus(String user_id, String request_id) {
//        JsonNode request = request().body().asJson();

//        if (hist.get("User_id") == null) {
        if (user_id == "" || request_id == ""){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.getBidStatus(user_id, request_id);
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
    public Result endService() {
        JsonNode request = request().body().asJson();

        if (request.get("user_id") == null || request.get("request_id") == null) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.endService(request.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }

    public Result sendLock() {
        JsonNode request = request().body().asJson();

        if (request.get("device_id") == null || request.get("status") == null  ){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.sendLock(request.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
    public Result sendDeviceLocation() {
        JsonNode request = request().body().asJson();

        if (request.get("device_id") == null || request.get("status") == null  || request.get("location") == null){
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }

        try {
            HashMap<String, Object> ret = database.sendDeviceLocation(request.toString());
            return ok(Json.toJson(ret));
        } catch (NoSuchAlgorithmException e) {
            HashMap<String, Object> ERR = new HashMap<String, Object>() {
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };
            e.printStackTrace();
            return ok(Json.toJson(ERR));

        }
    }
}