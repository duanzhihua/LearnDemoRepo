package com.huawei.test.flink.api.transformations;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class FlapMap {

	public static void main(String[] args) throws Exception {
		// 初始化环境
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// 初始化数据
		SingleOutputStreamOperator<String> dataset = env.fromElements("c a b d a c", "d c a b c d");
		// flapmap操作,对每一个元素操作，平分元素
		dataset = dataset.flatMap(new FlatMapFunction<String, String>() {

			@Override
			public void flatMap(String value, Collector<String> out) throws Exception {
				String[] values=value.split(" ");
				for(String v:values) {
					out.collect(v);
				}
				
			}
		});
		// 数据输出
		dataset.print();
		// 程序执行
		env.execute();
	}
}