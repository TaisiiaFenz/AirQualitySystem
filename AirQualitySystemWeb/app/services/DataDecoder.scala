package services

import java.text.SimpleDateFormat

import io.circe.{Decoder, HCursor, parser}
import models.SensorData
import java.util.Calendar

object DataDecoder {
    implicit val decodeFoo: Decoder[SensorData] = new Decoder[SensorData] {
      final def apply(c: HCursor): Decoder.Result[SensorData] =
        for {
          id <- c.downField("id").as[Int]
          data <- c.downField("data").as[Int]

        } yield {
          new SensorData(id, data, new SimpleDateFormat("dd.mm.yyyy").toString)
        }

    }
    def decodeData(json: String): SensorData = {
        val decodedData = parser.decode[SensorData](json).toOption.get
        decodedData
    }
}
