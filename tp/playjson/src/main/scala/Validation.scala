import play.api.data.validation.ValidationError

class Validation {

  import play.api.libs.json._ // Contains JSON AST and JSON tooling
  import play.api.libs.functional.syntax._  // Allow to compose readers

  // It's highly recommended to validate the JSON your receive in the same time you parse it.
  // Play-json libs provides a lot of validators by default
  // https://www.playframework.com/documentation/2.4.x/ScalaJsonCombinators#Validation-with-Reads
  // https://github.com/playframework/playframework/blob/master/framework/src/play-json/src/main/scala/play/api/libs/json/JsConstraints.scala#L82
  // https://github.com/playframework/playframework/blob/master/framework/src/play-json/src/main/scala/play/api/libs/json/Reads.scala#L185-L904


  // 1: Using only built-in validators, write a reader which check the following rules
  // - id: valid java UUID
  // - firstname required, non empty, max 30 chars
  // - lastname required, non empty, min 3 chars, max 30 chars
  // - password required, only digits, min 4 chars (high security !!!)
  // - email optional. If defined, must be a valid email
  // - age required, min 18
  case class User(id: java.util.UUID, firstname: String, lastname: String, password: String, email: Option[String], age: Int)

  // Tips: Use `keepAnd` to compose multiple validators and return the original value.
  val userValidator: Reads[User] = ???


  // Now, you will have to write your own validators

  // 2: Write a reader which check that a string is in lowercase (don't use pattern)

  // First way: use `Reads.verifying`
  val lowercase: Reads[String] = ???

  // Second way: use `Reads.filter`
  val lowercase2: Reads[String] = ???

  // Write a raw reader with `Reads.apply()`
  val lowercase3: Reads[String] = ???

  // You can also write a reader which check and then transform a value

  // 3: Write a reader which check if the string is a valid email (only check that the string contains a '@'), and then transform the string in lowercase
  val email: Reads[String] = ???

  // FYI, you can find here some example of readers
  // https://gist.github.com/studiodev/02f2f14f5d67a19655b8

  // Note: When you compose multiples rules on the same field, only the first failure will be reported in the error.
  // It's a known drawback of this lib, solved in the new https://github.com/jto/validation


}
