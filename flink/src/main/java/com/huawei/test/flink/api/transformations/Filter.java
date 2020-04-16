package com.huawei.test.flink.api.transformations;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Filter {

	public static void main(String[] args) throws Exception {
		// 初始化流式环境
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// 初始化数据
		SingleOutputStreamOperator<String> dataset = env.fromElements("c a b d a c", "d c a b c d");
		// filter操作，满足条件的筛选出来
		dataset = dataset.filter(new FilterFunction<String>() {
			
			@Override
			public boolean filter(String value) throws Exception {
				// 筛选包含“a c”字符串的数据
				return value.contains("a c");
			}
		});
		// 数据输出
		dataset.print();
		// 程序执行
		env.execute();

	}

}