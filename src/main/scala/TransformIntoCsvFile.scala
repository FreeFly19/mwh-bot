import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object TransformIntoCsvFile extends App {
  private val spark: SparkSession = SparkSession.builder()
    .master("local")
    .appName("hwm-bot")
    .getOrCreate()

  import spark.implicits._

  val dataFile = spark.read.textFile("/Users/freefly/temp/hwm/2018_01_roul.txt")

  dataFile
    .withColumn("_tmp", split($"value", "\\s+"))
    .select($"_tmp".getItem(0).as("id"), $"_tmp".getItem(1).as("date"),$"_tmp".getItem(2).as("time"), $"_tmp".getItem(4).as("value"))
    .write
    .option("header", "true")
    .csv("/Users/freefly/temp/hwm/data.cvs")

  println("Hello, world!!!")
}
