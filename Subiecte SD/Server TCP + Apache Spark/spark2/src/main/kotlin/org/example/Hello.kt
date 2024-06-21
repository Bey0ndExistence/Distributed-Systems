@file:JvmName("HelloKt")

package org.example

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.streaming.Durations
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream
import org.apache.spark.streaming.api.java.JavaStreamingContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class Stire(
    val url: String,
    val source: String,
    val summary: String,
    val headline: String,
    val datetime: Long,
)

fun main(args: Array<String>) {
    val sparkConf = SparkConf().setMaster("local[2]").setAppName("Spark Example").set("spark.executor.cores", "4")
    val streamingContext = JavaStreamingContext(sparkConf, Durations.seconds(5))
    val tcpStream: JavaReceiverInputDStream<String> = streamingContext.socketTextStream("localhost", 12345)
    val res = tcpStream.map {
        Json { ignoreUnknownKeys = true}.decodeFromString<Stire>(it)
    }.filter {
        it.source != "Yahoo" && it.summary.length < 500
    }.foreachRDD {
        rdd -> rdd.foreach {

            println(" =============== ${it.url} ${it.headline} ${SimpleDateFormat("DD.MM.YYYY").format(Date(it.datetime))}")
        }
    }



//    tcpStream.foreachRDD {
//        rdd ->
//            println(" \n\n\n\n\n============ RDD ============= \n\n\n\n\n\n")
//            rdd.foreach {
//                val stire = Json.decodeFromString<Stire>(it)
//                println(it)
//                println("\n\n\n\n")
//            }
//
//    }
    streamingContext.start()
    streamingContext.awaitTermination()

}

