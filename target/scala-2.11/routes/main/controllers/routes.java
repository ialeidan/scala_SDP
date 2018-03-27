
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/fares/IdeaProjects/scala_SDP/conf/routes
// @DATE:Sun Mar 25 17:12:42 GMT+03:00 2018

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseApplicationJava ApplicationJava = new controllers.ReverseApplicationJava(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseApplication Application = new controllers.ReverseApplication(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseApplicationJava ApplicationJava = new controllers.javascript.ReverseApplicationJava(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseApplication Application = new controllers.javascript.ReverseApplication(RoutesPrefix.byNamePrefix());
  }

}
