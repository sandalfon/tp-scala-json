import org.scalatest._
import play.api.libs.json._

class QuerySpec extends FreeSpec with Matchers {

  val query = new Query()

  "1. Use basic path traversing to extract value in the `json` object" in {
    intercept[JsResultException] {
      query.name
    }
    query.alias shouldEqual Seq("Slade Wilson")
    query.isActive shouldEqual true
    query.firstApparition shouldEqual 1980
    query.cover shouldEqual None
    query.publisherName shouldEqual "DC Comics"
    query.getComicsName shouldEqual Seq("Deathstroke versus Dora", "Deathstroke and Pikachu love")
    query.getFirstComicsId shouldEqual Some(1000)
  }

  "2. Return heroes name in UPPERCASE" in {
    query.getSafeName(Json.obj("name" -> "dora")) shouldEqual "DORA"
    query.getSafeName(Json.obj()) shouldEqual "n.a."
  }

  "3. Return the heroes name or the JS validation error in JSON" in {
    query.getNameOrError(Json.obj("name" -> "dora")) shouldEqual Right("dora")
    query.getNameOrError(Json.obj()).isLeft shouldEqual true
  }

}
