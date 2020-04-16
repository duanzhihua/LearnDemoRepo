package com.huawei.test.flink.source;

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import scala.util.Random;

public class UserSource extends RichParallelSourceFunction<String>{

	@Override
	public void run(SourceContext<String> ctx) throws Exception {
		for(int i=0;i<10;i++) {
			ctx.collect(new Random().nextInt(5)+"");
			Thread.sleep(1000);
		}
	}

	@Override
	public void cancel() {
		
	}
}
