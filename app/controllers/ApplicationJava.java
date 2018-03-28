package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import database.DatabaseJava;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;


public class ApplicationJava extends Controller{


    private DatabaseJava database = new DatabaseJava();

    public Result index1() {


        return ok("{\"name\": \"Mohammed\"}");


    }
    public Result register() {
        JsonNode reg =request().body().asJson();

        if(reg.get("email")== null || reg.get("password")==null || reg.get("phone")==null || reg.get("username")==null ){
            HashMap<String, Object> ERR = new HashMap<String, Object>(){
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));
        }



// return ok(Json.toJson( register(reg.get("first_name"),reg.get("last_name"),reg.get("email"),reg.get("password"),reg.get("national_id"),reg.get("phone"),reg.get("username") ))); //function will be created by Bakri, it will return HashMap Object,parameter is JsonNode

        return ok("Hello ");
    }
    public Result spRegister() {
        JsonNode spReg =request().body().asJson();

        if(spReg.get("email")== null || spReg.get("password")==null || spReg.get("phone")==null || spReg.get("username")==null )
        {
            HashMap<String, Object> ERR = new HashMap<String, Object>(){
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "SOME FIELDS ARE MISSING");
                }

            };

            return ok(Json.toJson(ERR));}

//        return ok(Json.toJson( spRegister( spReg.get("name"),spReg.get("email"),spReg.get("password"),spReg.get("phone"),spReg.get("username")))); //function will be created by Bakri, it will return HashMap Object,parameter is JsonNode

        return ok("Hello ");
    }
    public Result history() {
        JsonNode hist =request().body().asJson();
        if(hist.get("User_id")== null)
        {
            HashMap<String, Object> ERR = new HashMap<String, Object>(){
                {
                    put("error", "Error");
                    put("code", "400");
                    put("message", "User Id FIELDS is MISSING");
                }

            };

            return ok(Json.toJson(ERR));}

//          return ok(Json.toJson( history( hist.get("User_id") ))); //function will be created by Bakri, it will return HashMap Object,parameter is JsonNode
        return ok("Hello ");
    }
}
