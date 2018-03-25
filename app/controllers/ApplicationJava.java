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
        JsonNode Id =request().body().asJson();


        HashMap<String, Object> ret = new HashMap<String, Object>(){
            {
                put("id", Id.get("id").intValue()+1);
                put("username", Id.get("username"));
            }
        };

<<<<<<< HEAD

     //   database.testRead();
=======
        database.Add();
        database.testRead();
>>>>>>> c9fcb5777000666213b1370d3c02233b154ac8b3


        return ok(Json.toJson(ret));
//        return ok(Id);
//        return ok(Id.get("id").intValue() + "");


    }

}
