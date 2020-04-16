package com.huawei.test.flink.api.transformations;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MapAPI {
	public static void main(String[] args) throws Exception {
		// 初始化流式环境
		StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
		// 初始化数据
		SingleOutputStreamOperator<String> dataset=  env.fromElements("c a b d a c","d c a b c d");
		// map操作,返回所有元素的大写
		dataset=dataset.map(new MapFunction<String, String>() {

			@Override
			public String map(String value) throws Exception {
				return value.toUpperCase();
			}
		});
		// 数据输出
		dataset.print();
		// 程序执行
		env.execute();
	}
}