import org.scalatest._
import play.api.libs.json._

class ValidationSpec extends FreeSpec with Matchers {

  implicit class RichJsonTransformer(json: JsValue) {
    def update(path: JsPath, value: JsValue): JsValue = json.validate(__.json.update(path.json.put(value))).get
    def update(key: String, value: JsValue): JsValue = update(__ \ key, value)
    def update(key: String, value: String): JsValue = update(key, JsString(value))
    def update(key: String, value: BigDecimal): JsValue = update(key, JsNumber(value))
    def update(key: String, value: Boolean): JsValue = update(key, JsBoolean(value))
    def update(key: String, value: Long): JsValue = update(key, JsNumber(BigDecimal(value)))

    def prune(key: String): JsValue = json.validate((__ \ key).json.prune).get
  }

  val validation = new Validation()

  "1: Using only built-in validators, write a reader which check the following rules" in {
    val validJson = Json.obj(
      "id" -> "58f31000-33bd-45cb-8ec6-cf19049d3f60",
      "firstname" -> "julien",
      "lastname" -> "lafont",
      "password" -> "1234",
      "age" -> 26,
      "email" -> "a.b@c.fr"
    ).as[JsValue]

    validation.userValidator.reads(validJson) shouldBe a[JsSuccess[_]]
    validation.userValidator.reads(validJson).get.id shouldEqual java.util.UUID.fromString("58f31000-33bd-45cb-8ec6-cf19049d3f60")

    val invalidId = validJson.update("id", "aa58f31000-33bd-45cb-8ec6-cf19049d3f61")
    val invalidFirstname = validJson.update("firstname", "")
    val invalidLastname = validJson.update("lastname", "ju")
    val invalidPassword = validJson.update("password", "123a4")
    val invalidEmail = validJson.update("email", "a@")
    val allowEmptyEmail = validJson.prune("email")

    validation.userValidator.reads(invalidId).isSuccess shouldEqual false
    validation.userValidator.reads(invalidFirstname).isSuccess shouldEqual false
    validation.userValidator.reads(invalidLastname).isSuccess shouldEqual false
    validation.userValidator.reads(invalidPassword).isSuccess shouldEqual false
    validation.userValidator.reads(invalidEmail).isSuccess shouldEqual false
    validation.userValidator.reads(allowEmptyEmail).isSuccess shouldEqual true
  }

  "2: Write a reader which check that a string is in lowercase" in {
    val ok = "a b c d eeee ff"
    val nok = "a b c D eee f"

    validation.lowercase.reads(JsString(ok)).isSuccess shouldEqual true
    validation.lowercase.reads(JsString(nok)).isSuccess shouldEqual false

    validation.lowercase2.reads(JsString(ok)).isSuccess shouldEqual true
    validation.lowercase2.reads(JsString(nok)).isSuccess shouldEqual false

    validation.lowercase3.reads(JsString(ok)).isSuccess shouldEqual true
    validation.lowercase3.reads(JsString(nok)).isSuccess shouldEqual false
  }

  "3: Write a reader which check if the string is a valid email" in {
    validation.email.reads(JsString("A.A@A.A")) shouldEqual JsSuccess("a.a@a.a")
    validation.email.reads(JsString("A.A")).isSuccess shouldEqual false
  }
}
