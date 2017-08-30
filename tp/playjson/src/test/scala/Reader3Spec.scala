import java.util.TimeZone

import play.api.data.validation.ValidationError
import org.scalatest._
import play.api.libs.json._

class ReaderSpec extends FreeSpec with Matchers {

  val reader = new Reader()

  val validCreativeObj = reader.Creative(1000000000, "Coca-cola", Seq("attr1", "attr2", "attr3"), true, "banner")

  val validBuyingStrategyObj = reader.BuyingStrategy(
    Some(Seq("deal1", "deal2", "deal3")),
    Some((BigDecimal("43.23"), "base"))
  )

  val validAdvertiserObj = reader.Advertiser(1000, "Coca cola")

  val validScheduleObj = reader.Schedule("2015-08-01 00:00:00", "2015-08-30 00:00:00", TimeZone.getTimeZone("Europe/Paris"))

  "1: Write a reader for the Creative object" in {
    val invalidCreative = Json.obj("name" -> "foo")
    reader.creativeReader.reads(invalidCreative) shouldBe a [JsError]
    reader.creativeReader.reads(invalidCreative).asOpt shouldEqual None
    reader.creativeReader.reads(invalidCreative).asEither.isLeft shouldEqual true
    reader.creativeReader.reads(invalidCreative).isError shouldEqual true
    reader.creativeReader.reads(invalidCreative).isSuccess shouldEqual false

    val validCreativeJson = (reader.json \ "creative").as[JsValue]

    reader.creativeReader.reads(validCreativeJson) shouldEqual JsSuccess(validCreativeObj)
    reader.creativeReader.reads(validCreativeJson).asOpt shouldEqual Some(validCreativeObj)
    reader.creativeReader.reads(validCreativeJson).asEither shouldEqual Right(validCreativeObj)

    reader.creativeReader.reads(validCreativeJson).fold(
      errors => s"Cannot parse because $errors",
      value => s"Parsed with success as $value"
    ) should startWith("Parsed with success")

    // Importing the reader values puts implicit variable in the scope, so we can parse a JSON without explicitly giving the reader to the parser
    import reader._
    Json.fromJson[reader.Creative](validCreativeJson) shouldEqual JsSuccess(validCreativeObj)

  }

  "2: Write a reader for the Schedule object" in {
    reader.timeZoneReader.reads(JsNumber(2)) shouldBe a[JsError]
    reader.timeZoneReader.reads(JsNumber(2)) shouldEqual JsError(Seq(JsPath() -> Seq(ValidationError("Cannot parse TimeZone"))))

    reader.timeZoneReader.reads(JsString("Europe/Paris")) shouldEqual JsSuccess(TimeZone.getTimeZone("Europe/Paris"))

    val validScheduleJson = (reader.json \ "schedule").as[JsValue]
    reader.scheduleReader.reads(validScheduleJson) shouldEqual JsSuccess(validScheduleObj)

  }

  "3: Write a reader for the Advertiser" in {
    val invalidJson = Json.obj("advertiserName" -> 1, "advetiserId" -> "Coca Cola") // the types are wrong
    reader.advertiserReader.reads(invalidJson).isError shouldEqual true

    val validJson = (reader.json \ "advertiser").as[JsValue]
    reader.advertiserReader.reads(validJson) shouldEqual JsSuccess(validAdvertiserObj)
  }

  "4: Write a reader for BuyingStrategy" in {
    val validJson = (reader.json \ "buyingStrategy").as[JsValue]
    reader.buyingStrategyReader.reads(validJson) shouldEqual JsSuccess(validBuyingStrategyObj)
  }

  "5: Write a reader for each different targetService" in {
    // now you should know how to write our own tests :)
  }

  "6: Now write the real targetService Reader" in {
    // idem
  }

  "7: Write a reader for the full order" in {
    val json = reader.json

    val obj = reader.Order(
      name = "XXXXXXXXXXXXX",
      budget = BigDecimal(100000),
      creative = validCreativeObj,
      advertiser = validAdvertiserObj,
      buyingStrategy = validBuyingStrategyObj,
      schedule = validScheduleObj,
      targetService = Left(reader.BannerTargetService(
        impressionPixelUrls = Seq("http://www.xxxx.fr/pixel1", "http://www.xxxx.fr/pixel2"),
        clickCommandUrl = "http://www.xxxxx.fr/click-command"
      ))
    )


  }

}
