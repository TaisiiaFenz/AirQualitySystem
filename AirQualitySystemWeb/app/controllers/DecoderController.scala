package controllers

import models.SensorData
import io.circe.{ Decoder, Encoder, HCursor, Json }
import io.circe.parser
import io.circe.generic.auto._
object DecoderController {
    implicit val decodeFoo: Decoder[SensorData] = new Decoder[SensorData] {
      final def apply(c: HCursor): Decoder.Result[SensorData] =
        for {
          id <- c.downField("id").as[Int]
          value <- c.downField("value").as[Int]
        } yield {
          new SensorData(id, value)
        }

    }
    def decodeData(json: String): SensorData = {
        val decodedData = parser.decode[SensorData](json).toOption.get
        decodedData
    }
}
