
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object main_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

class main extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(title: String)(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.32*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>

<html>
    <head>
        <title>"""),_display_(/*7.17*/title),format.raw/*7.22*/("""</title>
        <title>Scala Getting Started on Heroku</title>
        <link rel="stylesheet" type="text/css" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript" src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
        <link rel="stylesheet" type="text/css" href=""""),_display_(/*12.55*/routes/*12.61*/.Assets.at("stylesheets/main.css")),format.raw/*12.95*/("""" />
    </head>
    <body>
        """),_display_(/*15.10*/nav()),format.raw/*15.15*/("""
        """),_display_(/*16.10*/content),format.raw/*16.17*/("""
    """),format.raw/*17.5*/("""</body>
</html>
"""))
      }
    }
  }

  def render(title:String,content:Html): play.twirl.api.HtmlFormat.Appendable = apply(title)(content)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)

  def ref: this.type = this

}


}

/**/
object main extends main_Scope0.main
              /*
                  -- GENERATED --
                  DATE: Sat Mar 24 12:08:26 AST 2018
                  SOURCE: /Users/ibrahimaleidan/Dev/Heruko SDP/scala-getting-started/app/views/main.scala.html
                  HASH: 40f4ec7f1d24cc484c0b32f97b8efc6f4b46d320
                  MATRIX: 530->1|655->31|683->33|760->84|785->89|1265->542|1280->548|1335->582|1399->619|1425->624|1462->634|1490->641|1522->646
                  LINES: 20->1|25->1|27->3|31->7|31->7|36->12|36->12|36->12|39->15|39->15|40->16|40->16|41->17
                  -- GENERATED --
              */
          