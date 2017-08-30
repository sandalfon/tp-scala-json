class Query {

  import play.api.libs.json._ // Contains JSON AST and JSON tooling

  val json = Json.parse("""
    {
      "alias": ["Slade Wilson"],
      "active": true,
      "firstApparition": 1980,
      "cover": null,
      "habilities": [
        "Genius level intellect",
        "Skilled manipulator and deceiver",
        "Increased superhuman"
      ],
      "publisher": {
        "name": "DC Comics"
      },
      "comics": [
        {
          "id": 1000,
          "name": "Deathstroke versus Dora"
        },
        {
          "id": 1001,
          "name": "Deathstroke and Pikachu love"
        }
      ]
    }
  """)

  // 1. Use basic path traversing to extract value in the `json` object
  // https://www.playframework.com/documentation/2.4.x/ScalaJson#Traversing-a-JsValue-structure
  def name: String = ???

  def alias: Seq[String] = ???

  def isActive: Boolean = ???

  def firstApparition: Int = ???

  def cover: Option[String] = ???

  def publisherName: String = ???

  def getComicsName: Seq[String] = ???

  def getFirstComicsId: Option[Int] = ???

  // But it's not a good idea to use .as[X], it doesn't handle error in JSON.
  // The 'name' method will fail!
  // Try with `validate` instead of `as` now

  // 2. Return heroes name in UPPERCASE
  // If the name is undefined, return 'n.a.'
  // Learn what is a JsResult with https://github.com/playframework/playframework/blob/2.4.x/framework/src/play-json/src/main/scala/play/api/libs/json/JsResult.scala
  def getSafeName(json: JsValue): String = ???


  // 3. Return the heroes name or the JS validation error in JSON
  def getNameOrError(json: JsValue): Either[JsObject, String] = ???

}
