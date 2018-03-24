
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object index_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

class index extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template1[String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(message: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.19*/("""

"""),_display_(/*3.2*/main("GettingStarted")/*3.24*/ {_display_(Seq[Any](format.raw/*3.26*/("""

"""),format.raw/*5.1*/("""<div class="jumbotron text-center">
  <div class="container">
    <a href="/" class="lang-logo">
      <img src=""""),_display_(/*8.18*/routes/*8.24*/.Assets.at("images/lang-logo.png")),format.raw/*8.58*/("""">
    </a>
    <h1>Getting Started with Scala/Play on Heroku</h1>
    <p>This is a sample Play application deployed to Heroku. It's a reasonably simple app - but a good foundation for understanding how to get the most out of the Heroku platform.</p>
    <a type="button" class="btn btn-lg btn-default" href="https://devcenter.heroku.com/articles/getting-started-with-scala"><span class="glyphicon glyphicon-flash"></span> Getting Started with Scala/Play</a>
    <a type="button" class="btn btn-lg btn-primary" href="https://github.com/heroku/scala-getting-started"><span class="glyphicon glyphicon-download"></span> Source on GitHub</a>
  </div>
</div>
<div class="container">
  """),_display_(/*17.4*/if(message != null)/*17.23*/{_display_(Seq[Any](format.raw/*17.24*/("""
  """),format.raw/*18.3*/("""<div class="alert alert-success text-center" role="alert">
    """),_display_(/*19.6*/message),format.raw/*19.13*/("""
  """),format.raw/*20.3*/("""</div>
  """)))}),format.raw/*21.4*/("""
  """),format.raw/*22.3*/("""<div class="alert alert-info text-center" role="alert">
    To deploy your own copy, and learn the fundamentals of the Heroku platform, head over to the <a href="https://devcenter.heroku.com/articles/getting-started-with-scala" class="alert-link">Getting Started with Scala/Play on Heroku</a> tutorial.
  </div>
  <hr>
  <div class="row">
    <div class="col-md-6">
      <h3><span class="glyphicon glyphicon-info-sign"></span> How this sample app works</h3>
      <ul>
        <li>This app was deployed to Heroku, either using Git or by using <a href="https://github.com/heroku/scala-getting-started">Heroku Button</a> on the repository.</li>

        <li>When Heroku received the source code, it fetched all the dependencies in the <a href="https://github.com/heroku/scala-getting-started/blob/master/build.sbt">build.sbt</a>, creating a deployable slug.</li>
        <li>The platform then spins up a dyno, a lightweight container that provides an isolated environment in which the slug can be mounted and executed.</li>
        <li>You can scale your app, manage it, and deploy over <a href="https://addons.heroku.com/">150 add-on services</a>, from the Dashboard or CLI.</li>
      </ul>
    </div>
    <div class="col-md-6">
      <h3><span class="glyphicon glyphicon-link"></span> Next Steps</h3>
      <ul>
        <li>If you are following the <a href="https://devcenter.heroku.com/articles/getting-started-with-scala">Getting Started</a> guide, then please head back to the tutorial and follow the next steps!</li>
        <li>If you deployed this app by deploying the Heroku Button, then in a command line shell, run:</li>
        <ul>
          <li><code>git clone https://github.com/heroku/scala-getting-started.git</code> - this will create a local copy of the source code for the app</li>
          <li><code>cd scala-getting-started</code> - change directory into the local source code repository</li>
          <li><code>heroku git:remote -a &lt;your-app-name></code> - associate the Heroku app with the repository</li>
          <li>You'll now be set up to run the app locally, or <a href="https://devcenter.heroku.com/articles/getting-started-with-scala#push-local-changes">deploy changes</a> to Heroku</li>
        </ul>
      </ul>
      <h3><span class="glyphicon glyphicon-link"></span> Helpful Links</h3>
      <ul>
        <li><a href="https://www.heroku.com/home">Heroku</a></li>
        <li><a href="https://devcenter.heroku.com/">Heroku Dev Center</a></li>
        <li><a href="https://devcenter.heroku.com/articles/getting-started-with-scala">Getting Started with Scala/Play on Heroku</a></li>
        <li><a href="https://devcenter.heroku.com/articles/deploying-scala">Deploying Scala Apps on Heroku</a></li>
        <li><a href="https://devcenter.heroku.com/articles/play-support">Heroku Play Framework Support</a></li>
        <li><a href="https://devcenter.heroku.com/articles/scala-support">Heroku Scala Support</a></li>
      </ul>
    </div>
  </div> <!-- row -->
   <div class="alert alert-info text-center" role="alert">
    Please do work through the Getting Started guide, even if you do know how to build such an application.  The guide covers the basics of working with Heroku, and will familiarize you with all the concepts you need in order to build and deploy your own apps.
  </div>
</div>

""")))}),format.raw/*65.2*/("""
"""))
      }
    }
  }

  def render(message:String): play.twirl.api.HtmlFormat.Appendable = apply(message)

  def f:((String) => play.twirl.api.HtmlFormat.Appendable) = (message) => apply(message)

  def ref: this.type = this

}


}

/**/
object index extends index_Scope0.index
              /*
                  -- GENERATED --
                  DATE: Sat Mar 24 12:08:26 AST 2018
                  SOURCE: /Users/ibrahimaleidan/Dev/Heruko SDP/scala-getting-started/app/views/index.scala.html
                  HASH: 6c89925f71ca77afa63ad9accdadfeed68c2b90c
                  MATRIX: 527->1|639->18|667->21|697->43|736->45|764->47|904->161|918->167|972->201|1679->882|1707->901|1746->902|1776->905|1866->969|1894->976|1924->979|1964->989|1994->992|5360->4328
                  LINES: 20->1|25->1|27->3|27->3|27->3|29->5|32->8|32->8|32->8|41->17|41->17|41->17|42->18|43->19|43->19|44->20|45->21|46->22|89->65
                  -- GENERATED --
              */
          