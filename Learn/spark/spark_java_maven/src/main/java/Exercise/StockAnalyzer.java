package Exercise;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

public class StockAnalyzer {
    public static void main(String args[]){
        JavaSparkContext sparkContext = getSparkContext();
        SparkSession sparkSession = getSparkSession();
//        JavaRDD<String> stocksRDD = sparkContext.textFile(getResourcePath("stocks.csv"));
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
        /**
         * Printing Largest Value in the date column. Logging Error if not 2016-08-15
         */
        /**
         * Registering stocksDataframe as temporary view
         */
        /**
         * Creating a new Dataframe stocks_last10 with 10 records for each stock.
         * Select date, symbol and adhclose columns
         * Optional : To use SQL statement and SQL window operation.
         * Window operation Reference : https://databricks.com/blog/2015/07/15/introducing-window-functions-in-spark-sql.html
         */

        /**
         * Create a new dataframe stocks_pivot, by pivoting the stocks_last10 dataframe.
         * For each stock, we want to find daily pct change for last 10 days.
         */

        /**
         * Find difference between adjclose for each pair of consecutive days
         * Expected Output : Symbol , diff1, diff2, diff3, ... diff10
         * diff1 : (adjclose of current date - adjclose of previous date)
         */
    }

    private static JavaSparkContext getSparkContext(){
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(getMainClass().getName());
        /**
         * Available Core definitions. * is a wildcard for using all available cores
         */
        sparkConf.setIfMissing("spark.master", "local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        return javaSparkContext;
    }

    private static SparkSession getSparkSession(){
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(getMainClass().getName());
        sparkConf.setIfMissing("spark.master", "local[4]");
        sparkConf.setIfMissing("spark.default.parallelism", "16");
//        sparkConf.setIfMissing("spark.hadoop.validateOutputSpecs", "false");
//        sparkConf.setIfMissing("spark.security.credentials.hiveserver2.enabled", "false");
        SparkSession sparkSession = SparkSession
                .builder()
                .config(sparkConf)
                .getOrCreate();
        return sparkSession;
    }

    private static String getResourcePath(String resourceFileName){
        return getMainClass().getResource(resourceFileName).toString();
    }

    private static Class<StockAnalyzer> getMainClass(){
        return StockAnalyzer.class;
    }
}
