import java.util.TimeZone

class Reader {

  import play.api.libs.json._ // Contains JSON AST and JSON tooling
  import play.api.libs.functional.syntax._ // Allow to compose readers

  import java.time._

  val json = Json.parse("""
    {
      "name": "XXXXXXXXXXXXX",
      "budget": 100000,
      "creative": {
        "id": 1000000000,
        "name": "Coca-cola",
        "attributes": ["attr1", "attr2", "attr3"],
        "active": true,
        "type": "banner"
      },
      "advertiser": {
        "advertiserId": 1000,
        "advertiserName": "Coca cola"
      },
      "buyingStrategy": {
        "directDealActivated": true,
        "directDeals": [ "deal1", "deal2", "deal3" ],
        "thirdInventoryActivated": true,
        "thirdInventoryBaseCPM": 43.23,
        "thirdInventoryMode": "base"
      },
      "schedule": {
        "start": "2015-08-01 00:00:00",
        "end": "2015-08-30 00:00:00",
        "timezone": "Europe/Paris"
      },
      "targetService": {
        "trackingUrls": ["http://www.xxxx.fr/track1", "http://www.xxxx.fr/track2"],
        "impressionPixelUrls": ["http://www.xxxx.fr/pixel1", "http://www.xxxx.fr/pixel2"],
        "landingPage": "http://www.xxxxx.fr/redirect",
        "clickCommandUrl": "http://www.xxxxx.fr/click-command"
      }
    }
  """)

  // The objective of this TP will be to map the JSON above into the business model defined below
  // Doc: https://www.playframework.com/documentation/2.4.x/ScalaJsonCombinators#Reads

  case class Order(
    name: String,
    budget: BigDecimal,
    creative: Creative,
    advertiser: Advertiser,
    buyingStrategy: BuyingStrategy,
    schedule: Schedule,
    targetService: Either[BannerTargetService, VideoTargetService])

  case class Creative(
    id: Long,
    name: String,
    attributes: Seq[String],
    active: Boolean,
    `type`: String
  )

  case class Schedule(
    start: String,
    end: String,
    timezone: TimeZone // Yes it's a bit weird to parse LocalDateTime and a timezone. Just do that for the exercise :)
  )

  case class Advertiser(
    id: Long,
    name: String
  )

  case class BuyingStrategy(
    directDeals: Option[Seq[String]],
    thirdInventory: Option[(BigDecimal, String)]
  )

  case class BannerTargetService(
    impressionPixelUrls: Seq[String],
    clickCommandUrl: String
  )
  case class VideoTargetService(
    trackingUrls: Seq[String],
    impressionPixelUrls: Seq[String],
    landingPage: String
  )

  // We will write Readers for each sub-object, from basic to more complex


  // 1: Write a reader for the Creative object
  // When the case class map exactly the json you're trying to parse, you can use the "Macro inception" to
  // automatically generate a reader (compile time)
  // https://www.playframework.com/documentation/2.4.x/ScalaJsonInception
  implicit val creativeReader: Reads[Creative] = ???
  // Look the test to discover what you can do with this reader
  // It works between the lib known how to read basic types (String, Boolean, Int, Long, Double...) and also composed types: Option[X], Seq[X], Map[String, X]...
  // There is also built-in readers for java.time._, org.joda._, java.util.UUID, etc

  // 2: Write a reader for the schedule object

  // Firstly, the lib doesn't know how to parse a `Timezone` object
  // So we need a Reads[Timezone] in the scope (i.e. in the implicit scope)
  // A Reads[T] is implemented by a function which return a JsResult[T] from a JsValue (`JsValue => JsResult[T]`)
  // You can write a reader "from roots" (i.e. from JsValue) like that
  implicit val timeZoneReader: Reads[TimeZone] = Reads {
    case JsString(timezoneId) => JsSuccess(TimeZone.getTimeZone(timezoneId))
    case _ => JsError("Cannot parse TimeZone")
  }

  // It's also possible to compose a Reader for a parsable Reader
  val timeZoneReader2 = Reads.of[String].map(TimeZone.getTimeZone)
  val timeZoneReader3 = __.read[String].map(TimeZone.getTimeZone)
  val timeZoneReader4 = __.read[String].map(TimeZone.getTimeZone).orElse(Reads.pure(TimeZone.getDefault)) // We can also fallback on a default value

  // Now the lib is able to generate a reader for Schedule (it knows how to parse TimeZone and LocalDateTime)
  implicit val scheduleReader: Reads[Schedule] = ???

  // 3: Write a reader for the Advertiser
  // Unfortunately, we can't use the macro for this object because the format of the object doesn't match the json object
  // Look carefully, the keys are not the same
  // You can always write a reader manually, as explained here https://www.playframework.com/documentation/2.4.x/ScalaJsonCombinators#Complex-Reads
  implicit val advertiserReader: Reads[Advertiser] = ???


  // 4: Write a reader for BuyingStrategy
  // Same as the 3., you can't use macro because the format is not the same
  // You must parse all values independently, and use all the data to recreate the object like you want
  // Tips: You can't write `BuyingStrategy.apply _`, you need a custom apply.
  implicit val buyingStrategyReader: Reads[BuyingStrategy] = ???

  // 5: Write a reader for each different targetService
  // Hardest case: you need to parse the TargerService into a BannerTargetService or a VideoTargetService, depending
  // on the type of the creative
  // Firstly, just write 2 distinct readers for the 2 cases.
  // Tips: superfluous values will just be ignored by the macro inception
  val bannerTargetServiceReader: Reads[BannerTargetService] = ???
  val videoTargetServiceReader: Reads[VideoTargetService] = ???

  // 6: Now write the real targetService Reader
  // He needs to firstly parse the `creative.type` value to choose which reader is the good one.
  implicit val targetServiceReader: Reads[Either[BannerTargetService, VideoTargetService]] = (__ \ "creative" \ "type").read[String].flatMap {
    case "video" => ???
    case "banner" => ???
  }

  // 7: Write a reader for the full order
  // Now, you just have to plug all readers together
  implicit val orderReader: Reads[Order] = ???


}
