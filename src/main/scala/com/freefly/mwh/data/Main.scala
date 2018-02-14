package com.freefly.mwh.data

import org.apache.spark.sql.SparkSession

object Main {
  private val spark: SparkSession = SparkSession.builder()
    .master("local")
    .appName("hwm-bot")
    .getOrCreate()

  import spark.implicits._

  def main(args: Array[String]): Unit = {
    val dataFile = spark.read
      .option("header", "true")
      .csv("/Users/freefly/temp/hwm/data.csv")

    dataFile.groupByKey(_.getString(3)).count().orderBy($"count(1)").show(40)


  }
}
