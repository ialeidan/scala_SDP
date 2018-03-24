
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/ibrahimaleidan/Dev/Heruko SDP/heroku_scala/conf/routes
// @DATE:Sat Mar 24 18:06:38 AST 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset

object Routes extends Routes

class Routes extends GeneratedRouter {

  import ReverseRouteContext.empty

  override val errorHandler: play.api.http.HttpErrorHandler = play.api.http.LazyHttpErrorHandler

  private var _prefix = "/"

  def withPrefix(prefix: String): Routes = {
    _prefix = prefix
    router.RoutesPrefix.setPrefix(prefix)
    
    this
  }

  def prefix: String = _prefix

  lazy val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation: Seq[(String, String, String)] = List(
    ("""GET""", prefix, """controllers.Application.index"""),
    ("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """db""", """controllers.Application.db"""),
    ("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """test""", """controllers.Application.test"""),
    ("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""", """controllers.Assets.at(path:String = "/public", file:String)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_Application_index0_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_Application_index0_invoker = createInvoker(
    controllers.Application.index,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Application",
      "index",
      Nil,
      "GET",
      """ Home page""",
      this.prefix + """"""
    )
  )

  // @LINE:7
  private[this] lazy val controllers_Application_db1_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("db")))
  )
  private[this] lazy val controllers_Application_db1_invoker = createInvoker(
    controllers.Application.db,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Application",
      "db",
      Nil,
      "GET",
      """""",
      this.prefix + """db"""
    )
  )

  // @LINE:8
  private[this] lazy val controllers_Application_test2_route: Route.ParamsExtractor = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("test")))
  )
  private[this] lazy val controllers_Application_test2_invoker = createInvoker(
    controllers.Application.test,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Application",
      "test",
      Nil,
      "POST",
      """""",
      this.prefix + """test"""
    )
  )

  // @LINE:11
  private[this] lazy val controllers_Assets_at3_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_at3_invoker = createInvoker(
    controllers.Assets.at(fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "at",
      Seq(classOf[String], classOf[String]),
      "GET",
      """ Map static resources from the /public folder to the /assets URL path""",
      this.prefix + """assets/$file<.+>"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_Application_index0_route(params) =>
      call { 
        controllers_Application_index0_invoker.call(controllers.Application.index)
      }
  
    // @LINE:7
    case controllers_Application_db1_route(params) =>
      call { 
        controllers_Application_db1_invoker.call(controllers.Application.db)
      }
  
    // @LINE:8
    case controllers_Application_test2_route(params) =>
      call { 
        controllers_Application_test2_invoker.call(controllers.Application.test)
      }
  
    // @LINE:11
    case controllers_Assets_at3_route(params) =>
      call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at3_invoker.call(controllers.Assets.at(path, file))
      }
  }
}