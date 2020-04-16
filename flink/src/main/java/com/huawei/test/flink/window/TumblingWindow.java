package com.huawei.test.flink.window;

import java.text.SimpleDateFormat;
import java.util.Random;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import com.huawei.test.flink.source.UserSource;

public class TumblingWindow {

	public static void main(String[] args) {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);
		DataStream<String> dataStreamSource = env.addSource(new UserSource());
		SingleOutputStreamOperator<Tuple2<String, Integer>> mapStream = dataStreamSource
				.map(new MapFunction<String, Tuple2<String, Integer>>() {
					@Override
					public Tuple2<String, Integer> map(String value) throws Exception {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						long timeMillis = System.currentTimeMillis();
						int random = new Random().nextInt(10);
						System.out.println("value: " + value + " random: " + random + "|" + format.format(timeMillis));
						return new Tuple2<String, Integer>(value, random);
					}
				});

		KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = mapStream.keyBy(0);

		// 基于时间驱动，每隔2s划分一个窗口
		WindowedStream<Tuple2<String, Integer>, Tuple, TimeWindow> timeWindow = keyedStream.timeWindow(Time.seconds(2));
		
		// apply是窗口的应用函数，即apply里的函数将应用在此窗口的数据上。
		//timeWindow.apply(new MyTimeWindowFunction()).print();

		try {
			// 转换算子都是lazy init的, 最后要显式调用 执行程序
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}