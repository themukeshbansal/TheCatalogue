package Exercise;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.MapPartitionsFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

public class StockAnalyzer {
    public static void main(String args[]){
        SparkSession sparkSession = getSparkSession();
        /**
         * Loading stocks.csv file as dataframe with schema inferencing enabled
         */
        Dataset<Row> stocksDataframe = sparkSession
                .read()
                .option("header", true)
                .option("inferSchema", true)
                .csv("input_output_folder/inputs/stocks.csv");
        stocksDataframe.show(10,false);
        /**
         * Caching the data frame
         */
        stocksDataframe.cache();
        /**
         * Printing Data Types for each column
         */
        stocksDataframe.printSchema();
        /**
         * Casting the date field stocksDataframe as date type.
         */
        Dataset<Row> castedDateStocksDataframe = stocksDataframe
                .withColumn(
                        "castedDate",
                        functions.column("date").cast(DataTypes.DateType)
                );
        castedDateStocksDataframe.show(10, false);
        /**
         * Registering stocksDataframe as temporary view
         */
        castedDateStocksDataframe.createOrReplaceTempView("castedDateStocksDataframe");
        /**
         * Printing Largest Value in the date column.
         */
        castedDateStocksDataframe.agg(functions.max("castedDate").alias("Max Date")).show();
        /**
         * OutPut
         * +----------+
         * |  Max Date|
         * +----------+
         * |2016-08-15|
         * +----------+
         */
        /**
         * Creating a new Dataframe stocks_last10 with 10 records for each stock.
         * Select date, symbol and adhclose columns
         * Optional : To use SQL statement and SQL window operation.
         * Window operation Reference : https://databricks.com/blog/2015/07/15/introducing-window-functions-in-spark-sql.html
         */
        WindowSpec windowSpecCastedDateStocksDataframe = Window
                .partitionBy(castedDateStocksDataframe.col("symbol"))
                .orderBy(castedDateStocksDataframe.col("castedDate").desc());
        Dataset<Row> stocks_last10 = castedDateStocksDataframe
                .select(
                        castedDateStocksDataframe.col("castedDate")
                        , castedDateStocksDataframe.col("symbol")
                        , castedDateStocksDataframe.col("adjclose")
                        )
                .withColumn("rankedIndex",  functions.row_number().over(windowSpecCastedDateStocksDataframe))
                .where(functions.col("rankedIndex").leq(10))
                .drop(functions.col("rankedIndex"));
        stocks_last10.show(20, false);
        /**
         * Create a new dataframe stocks_pivot, by pivoting the stocks_last10 dataframe.
         * For each stock, we want to find daily pct change for last 10 days.
         */

        Dataset<Row> stocks_pivot = stocks_last10
                .groupBy(stocks_last10.col("symbol"))
                .pivot(stocks_last10.col("castedDate"))
                .sum("adjclose");
        stocks_pivot
                .coalesce(1)
                .write().mode(SaveMode.Overwrite)
                .format("csv").option("header", true).save("output");
        /**
         * Find difference between adjclose for each pair of consecutive days
         * Expected Output : Symbol , diff1, diff2, diff3, ... diff10
         * diff1 : (adjclose of current date - adjclose of previous date)
         */
        castedDateStocksDataframe.createOrReplaceTempView("stocks_pivot");
        ExpressionEncoder<Row> rowEncoder = RowEncoder.apply(stocks_pivot.schema());

        Dataset<Row> stocks_pivot_difference = stocks_pivot.map(
                (MapFunction<Row, Row>) rowItem -> {
                    Object[] resultingColumns = new Object[rowItem.size()];
                    resultingColumns[0] = rowItem.getString(0);
                    for (int i = 1; i < rowItem.size() - 1 ; i++) {
                        resultingColumns[i] = rowItem.getDouble(i + 1) - rowItem.getDouble(i);
                    }
                    Row returningRow = RowFactory.create(
                            resultingColumns
                    );
                    return returningRow;
                }
                , rowEncoder
        );
        stocks_pivot_difference
                .coalesce(1)
                .write().mode(SaveMode.Overwrite)
                .format("csv").option("header", true).save("output");
    }

    private static SparkSession getSparkSession(){
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(getMainClass().getName());
        sparkConf.setIfMissing("spark.master", "local[4]");
        sparkConf.setIfMissing("spark.default.parallelism", "16");
        SparkSession orCreate = SparkSession
                .builder()
                .config(sparkConf)
                .getOrCreate();
        return orCreate;
    }

    private static Class<StockAnalyzer> getMainClass(){
        return StockAnalyzer.class;
    }
}
