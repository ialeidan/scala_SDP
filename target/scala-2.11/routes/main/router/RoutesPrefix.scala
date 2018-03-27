
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/fares/IdeaProjects/scala_SDP/conf/routes
// @DATE:Sun Mar 25 17:12:42 GMT+03:00 2018


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
