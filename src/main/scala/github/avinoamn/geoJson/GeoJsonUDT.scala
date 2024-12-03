package github.avinoamn.geoJson

import GeoJson._
import github.avinoamn.geoJson.GeoJsonUDT.TypeFactory.createType
import github.avinoamn.spark.sql.UDT.flat._
import org.apache.spark.sql.types.UDTRegistration

class GeoJsonUDT extends FlatUDT[GeoJson] {
  override def types: Array[FlatUDTType[GeoJson, _]] = GeoJsonUDT.types
}

object GeoJsonUDT {
  object TypeFactory extends FlatUDTTypeFactory[GeoJson] {
    override def typesCount: Int = GeoJson.Types.getClass.getDeclaredFields.length - 1
  }

  val types: Array[FlatUDTType[GeoJson, _]] = Array(
    createType[PointCoordinates](Point, {
      case Point(c) => Some(c)
      case _ => None
    }),
    createType[LineStringCoordinates](LineString, {
      case LineString(c) => Some(c)
      case _ => None
    }),
    createType[PolygonCoordinates](Polygon, {
      case Polygon(c) => Some(c)
      case _ => None
    }),
    createType[Seq[PointCoordinates]](MultiPoint, {
      case MultiPoint(c) => Some(c)
      case _ => None
    }),
    createType[Seq[LineStringCoordinates]](MultiLineString, {
      case MultiLineString(c) => Some(c)
      case _ => None
    }),
    createType[Seq[PolygonCoordinates]](MultiPolygon, {
      case MultiPolygon(c) => Some(c)
      case _ => None
    })
  )

  def register(): Unit = UDTRegistration.register(classOf[GeoJson].getName, classOf[GeoJsonUDT].getName)
}
