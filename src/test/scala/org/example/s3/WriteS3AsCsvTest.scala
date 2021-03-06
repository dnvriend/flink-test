package org.example.s3

import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem
import org.example.TestSpec

class WriteS3AsCsvTest extends TestSpec {
  it should "do a wordcount" in withDataSetEnv { env =>

    // source
    val text: DataSet[String] = env.fromElements("To be, or not to be,--that is the question:--",
      "Whether 'tis nobler in the mind to suffer", "The slings and arrows of outrageous fortune",
      "Or to take arms against a sea of troubles,")

    // transformations
    val counts: AggregateDataSet[(String, Int)] = {
      text
        .flatMap(_.toLowerCase.split("\\W+"))
        .map((_, 1))
        .groupBy(0)
        .sum(1)
      }


    counts.writeAsCsv("s3://dnvriend-data/flink-write-as-text-test.csv", writeMode = FileSystem.WriteMode.OVERWRITE)
    env.execute("WordCount s3 csv")
  }
}
