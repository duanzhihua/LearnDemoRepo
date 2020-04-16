package com.huawei.test.flink.model;

/**
 *	类必须为public
 * 	类必须有一个没有参数的公共构造函数（默认构造函数）
 * 	类所有字段都是public的，或者必须通过getter和setter函数访问。 对于名为name的字段，getter和setter方法必须命名为getName（）和setName（）
 *	类中的字段类型必须为Flink支持的类型。 目前，Flink使用Avro序列化任意对象（例如Date）
 */
public class Person {
	public String name;
    public int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}