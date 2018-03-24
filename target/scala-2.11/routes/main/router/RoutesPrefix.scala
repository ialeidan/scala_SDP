
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/ibrahimaleidan/Dev/Heruko SDP/heroku_scala/conf/routes
// @DATE:Sat Mar 24 18:06:38 AST 2018


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
