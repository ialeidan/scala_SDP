package controllers

import com.fasterxml.jackson.databind.JsonNode
import com.mongodb.casbah.MongoClient
import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import play.api.db._
import play.api.libs.json.Json

object Application extends Controller {

//  val mongoClient = MongoClient(Play.configuration.getString("mongodb.uri").get)
//  val mongDB = mongoClient("heroku_85mqw3gf")


  def index = Action {
    Ok(views.html.index(null))
  }

  def db = Action {
    var out = ""
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())")

      val rs = stmt.executeQuery("SELECT tick FROM ticks")

      while (rs.next) {
        out += "Read from DB: " + rs.getTimestamp("tick") + "\n"
      }
    } finally {
      conn.close()
    }
    Ok(out)
  }


  def test = Action { implicit request =>
    var out = request.body.asJson

    out.map {jsValue =>
      val name = (jsValue \ "name")
      val password = (jsValue \ "password")

//      Ok(Json.toJson(Map("status" -> "OK", "message" -> ("Hello " + name.toString))))
      Ok(Play.configuration.getString("mongodb.uri").get)
    }.getOrElse{
      BadRequest(Json.toJson(Map("status" -> "KO", "message" -> "Missing parameter [name]")))
    }
  }


//  def testDB = Action { implicit request =>
//    var out = request.body.asJson
//    var ret = "";
//
//
//    val coll = mongDB("Users")
//
//    val allDocs = coll.find()
//    println( allDocs )
//    for(doc <- allDocs){
//        ret += " / " + doc.toString()
//    }
//
//    out.map {jsValue =>
//      val name = (jsValue \ "name")
//      val password = (jsValue \ "password")
//
//
//
//
////      Ok(Json.toJson(Map("status" -> "OK", "message" -> ("Hello " + name.toString))))
//      Ok(ret)
//    }.getOrElse{
//      BadRequest(Json.toJson(Map("status" -> "KO", "message" -> "Missing parameter [name]")))
//    }
//  }

}
