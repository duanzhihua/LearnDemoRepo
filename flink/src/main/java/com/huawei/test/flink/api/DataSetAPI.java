package com.huawei.test.flink.api;

import java.io.IOException;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.jdbc.JDBCInputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.api.scala.hadoop.mapreduce.HadoopInputFormat;
import org.apache.flink.hadoopcompatibility.HadoopInputs;
import org.apache.flink.types.Row;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.huawei.test.flink.model.Person;

public class DataSetAPI {

	public static void main(String[] args) throws IOException {
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		// 从本地读取TXT文件内容，按行读取
		DataSet<String> localLines = env.readTextFile("file:///home/hw18868091790/data");

		// 从HDFS文件系统中读取TXT文件，按行读取
		DataSet<String> hdfsLines = env.readTextFile("hdfs://masters/user/hw18868091790/flink/data");

		// 从HDFS文件系统中读取CSV文件，并指定三列为int、String、Double，返回元组
		DataSet<Tuple3<Integer, String, Double>> csvInput = env.readCsvFile("hdfs://masters/user/hw18868091790/flink/CSV/file")
			                       .types(Integer.class, String.class, Double.class);

		// 从HDFS文件系统中读取CSV文件，并指定第1和4列数据，返回元组
		DataSet<Tuple2<String, Double>> csvInput1 = env.readCsvFile("hdfs://masters/user/hw18868091790/flink/CSV/file")
		                               .includeFields("10010")  // 获取第1和4数据
			                       .types(String.class, Double.class);

		// 从HDFS中读取CSV文件，并将CSV文件中的name、age数据获取封装成对象返回
		DataSet<Person> csvInput2 = env.readCsvFile("hdfs://masters/user/hw18868091790/flink/CSV/file")
		                         .pojoType(Person.class, "name", "age");

		// 通过字符串数组获取数据
		DataSet<String> value = env.fromElements("Foo", "bar", "foobar", "fubar");

		// 给定一个开始值和结束值来创建序列并返回数据
		DataSet<Long> numbers = env.generateSequence(1, 10000000);

		// 从一个JDBC的读取接口获取数据并返回数据，需要添加flink_jdbc包
		DataSet<Row> dbData =
		    env.createInput(
		      JDBCInputFormat.buildJDBCInputFormat()
		      				 // 设置驱动类
		                     .setDrivername("com.mysql.jdbc.Driver")
		                     // 设置连接URL
		                     .setDBUrl("jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false")
		                     // 设置数据库用户名
		                     .setUsername("sqoop")
		                     // 设置数据库密码
		                     .setPassword("sqoop")
		                     // 设置执行的语句
		                     .setQuery("select name, age from persons")
		                     // 设置返回值数据类型
		                     .setRowTypeInfo(new RowTypeInfo(BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO))
		                     .finish()
		    );
		// 也可以直接从Hadoop中接入数据，需要添加flink-hadoop-compatibility、flink-avro、parquet-avro等包
		DataSet<Tuple2<IntWritable, Text>> tuples =	
				env.createInput(
						HadoopInputs.readSequenceFile(
								IntWritable.class, 
								Text.class, 
								"hdfs://nnHost:nnPort/path/to/file"
								)
						);
	}
}