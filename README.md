# Tutorial Scala JSON library

## Introduction

### Library requirements

 - Parse String to JSON, Write JSON to String
 - Parse JSON to Scala objects
 - Write JSON from Scala objects
 - Eventually, transform JSON into another JSON without parsing into scala class
 - Eventually, validate JSON objects before parsing
 - Eventually, works with others protocols (xml, multipart/form-data...)
 
### Lots of library, high fragmentation
    
#### Play JSON
https://www.playframework.com/documentation/2.4.x/ScalaJson<br />
Parsing, Writing, Transformation, Validation     

#### Validation (based on Play JSON)
https://github.com/jto/validation<br />
Parsing, Writing, Transformation, Advanced Validation, Format Agnostic

#### Circe
https://github.com/travisbrown/circe<br />
Parsing, Writing, Transformation, Validation, Cursor, Lenses...
     
#### Spray JSON
https://github.com/spray/spray-json<br />
Parsing, Writing, Simple validation
     
#### Lift JSON
https://github.com/lift/lift/tree/master/framework/lift-base/lift-json/<br />
Parsing, Writing, Validation, Advanced Querying
     
#### Rapture JSON
https://github.com/propensive/rapture-json<br />
Dynamic, Parsing, Writing

### My recommendation ?

#### Use case 1: management-application

The json parsing is a quick operation, doesn't need to be ultra-performant, but you need an efficient way to validate inputs.

I recommend Play JSON or Validation. The validation features are better than all other frameworks, and it's globally fast.

#### Use case 2: high-throughput application

Proxy, loggers or real-time server that need to parse a lot of tiny JSON inputs very quickly, without doing detailled validation.

I recommend Circe, which is one of the [more performant](https://github.com/studiodev/json-bidrequests-benchmark-scala) library, but also provides a very robust, scala-ish & clean API.
 
## Play Json tutorial

Open `tp/playjson` and follows:

 - Basics
 - Query
 - Reader
 - Validation
 - Write

There is a test class for each tutorial, follow them in parallel.
 
## Validation tutorial

TODO (try to do the same as play-json !)
https://github.com/jto/validation#documentation

## Circe tutorial

TODO (try to do the same as play-json !)

