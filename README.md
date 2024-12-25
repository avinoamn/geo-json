# Geo-JSON
`geo-json` is a Scala library for working with GeoJSON data structures. It provides a type-safe way to handle
GeoJSON features like Points, LineStrings, Polygons, and their Multi* counterparts. It also includes
serialization/deserialization support using circe and integration with Apache Spark via User Defined Types (UDTs).


## Table of Contents
- [Installation](#installation)
- [Features](#features)
- [Usage](#usage)
    - [Core GeoJSON Types](#core-geojson-types)
    - [Serialization and Deserialization](#serialization-and-deserialization)
    - [Apache Spark Integration](#apache-spark-integration)


## Installation
To include this library in your project, clone this repository and install it locally.

For Maven users, add the following dependency to your `pom.xml`:

```xml
<dependency>
   <groupId>github.avinoamn</groupId>
   <artifactId>geo-json_${spark.major.version}_${scala.major.version}</artifactId>
   <version>1.0.0</version>
   <scope>compile</scope>
</dependency>
```


## Features
- **Core GeoJSON Types**: Type-safe implementations of GeoJSON elements (`Point`, `LineString`, `Polygon`, etc.).
- **JSON Codec**: Serialize and deserialize GeoJSON objects using circe.
- **Apache Spark Integration**: Support for registering GeoJSON as Spark UDTs for seamless SQL operations.


## Usage

### Core GeoJSON Types
The core GeoJSON types are implemented as Scala case classes. Here is an example:

```scala
import github.avinoamn.geoJson.GeoJson._

val point = Point(Seq(100.0, 0.0))
val lineString = LineString(Seq(Seq(100.0, 0.0), Seq(101.0, 1.0)))
val polygon = Polygon(Seq(Seq(Seq(100.0, 0.0), Seq(101.0, 0.0), Seq(101.0, 1.0), Seq(100.0, 0.0))))

println(point) // Point(Seq(100.0, 0.0))
println(lineString) // LineString(Seq(Seq(100.0, 0.0), Seq(101.0, 1.0)))
println(polygon) // Polygon(Seq(Seq(Seq(100.0, 0.0), Seq(101.0, 0.0), Seq(101.0, 1.0), Seq(100.0, 0.0))))
```


### Serialization and Deserialization
You can serialize and deserialize GeoJSON objects to and from JSON using the provided circe codecs.

**Serialization**
```scala
import github.avinoamn.geoJson.GeoJson
import github.avinoamn.geoJson.GeoJson._
import github.avinoamn.geoJson.GeoJsonCodec._
import io.circe.syntax._

val point: GeoJson = Point(Seq(100.0, 0.0))
val json = point.asJson
println(json) // {"type":"Point","coordinates":[100.0,0.0]}
```

**Deserialization**
```scala
import io.circe.parser._
import github.avinoamn.geoJson.GeoJson
import github.avinoamn.geoJson.GeoJsonCodec._

val jsonString = """{"type":"Point","coordinates":[100.0,0.0]}"""
val geoJson = decode[GeoJson](jsonString)
println(geoJson) // Right(Point(Seq(100.0, 0.0)))
```

### Apache Spark Integration

Register GeoJSON UDTs with Apache Spark to enable usage within Spark SQL operations.

```scala
case class Data(geoJson: GeoJson)

object MainApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("GeoJsonUDT Example")
      .master("local[*]")
      .getOrCreate()

    // Register the UDT
    GeoJsonUDT.register()

    import spark.implicits._

    // Sample data
    val data: Seq[Data] = Seq(
      Data(Point(Seq(100.0, 0.0))),
      Data(LineString(Seq(Seq(100.0, 0.0), Seq(101.0, 1.0)))),
      Data(Polygon(Seq(Seq(Seq(100.0, 0.0), Seq(101.0, 0.0), Seq(101.0, 1.0), Seq(100.0, 0.0)))))
    )

    // Convert data to Dataset
    val ds: Dataset[Data] = data.toDS()
    ds.show()

    // Serialize data using UDT
    val df = ds.toDF("geoJson")
    df.printSchema()
    df.show()
  }
}
```
