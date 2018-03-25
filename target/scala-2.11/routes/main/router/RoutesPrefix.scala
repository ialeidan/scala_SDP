
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/ibrahimaleidan/Dev/Heruko SDP/heroku_scala/conf/routes
// @DATE:Sun Mar 25 11:00:53 AST 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
