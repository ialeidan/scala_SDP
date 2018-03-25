package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;

public class ApplicationJava extends Controller{


    public Result index1() {
        JsonNode Id =request().body().asJson();


        HashMap<String, Object> ret = new HashMap<String, Object>(){
            {
                put("id", Id.get("id").intValue()+1);
                put("username", Id.get("name"));
            }
        };

//        return ok(Json.toJson(ret));
        return ok(Id);


    }

}
