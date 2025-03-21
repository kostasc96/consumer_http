package app

import akka.actor.ActorSystem
import akka.kafka._
import akka.kafka.scaladsl._
import akka.stream.scaladsl._
import akka.stream._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HttpConsumer extends App {
  implicit val system = ActorSystem("KafkaCommittableConsumerSystem")
  implicit val materializer: Materializer = ActorMaterializer()(system)
  import system.dispatcher

  val kafkaConsumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers("kafka:9092")
    .withGroupId("requests-responses-consumer")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  Consumer
    .committableSource(kafkaConsumerSettings, Subscriptions.topics("requests-responses"))
    .mapAsync(4) { msg =>
      val url = msg.record.value()
      println(s"Received URL: $url")

      // Simulate async processing, like making an HTTP call:
      val processingFuture = Future.successful(url) // Replace with actual logic if needed

      // After processing, commit offset
      processingFuture.flatMap(_ => msg.committableOffset.commitScaladsl())
    }
    .runWith(Sink.ignore)
}
