import org.scalatest._

class BasicsSpec extends FreeSpec with Matchers {

  val basics = new Basics()

  "1. Parse the Json String into a JsValue" in {
    (basics.json \ "name").as[String] shouldEqual "Deathstroke"
  }

  "2. Recreate manually the same JSON as `simpleJsonString` using JSON AST" in {
    basics.createJsonManually() shouldEqual basics.json
  }

}
