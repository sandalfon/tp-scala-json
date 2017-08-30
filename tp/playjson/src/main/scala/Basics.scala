class Basics {

  import play.api.libs.json._ // Contains JSON AST and JSON tooling

  val simpleJsonString = """
    {
      "name": "Deathstroke",
      "alias": ["Slade Wilson"],
      "active": true,
      "firstApparition": 1980,
      "cover": "https://upload.wikimedia.org/wikipedia/en/thumb/6/67/Deathstroke_Vol_2_8.png/250px-Deathstroke_Vol_2_8.png",
      "habilities": [
        "Genius level intellect",
        "Skilled manipulator and deceiver",
        "Increased superhuman"
      ],
      "publisher": {
        "name": "DC Comics",
        "authors": ["Marv Wolfman", "George Pérez"]
      }
    }
  """


  // 1. Parse the Json String into a JsValue
  // Use https://github.com/playframework/playframework/blob/master/framework/src/play-json/src/main/scala/play/api/libs/json/Json.scala
  val json: JsValue = ???


  // 2. Recreate manually the same JSON as `simpleJsonString` using JSON AST.
  // cf https://github.com/playframework/playframework/blob/master/framework/src/play-json/src/main/scala/play/api/libs/json/JsValue.scala
  // cf https://www.playframework.com/documentation/2.4.x/ScalaJson
  def createJsonManually(): JsValue = {

    ???

  }

}

