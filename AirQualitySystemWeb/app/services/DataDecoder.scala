package services

import io.circe.{Decoder, HCursor, parser}
import models.SensorData

object DataDecoder {
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
