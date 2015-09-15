package com.irengine.sandbox.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestCase {

	@Test
	public void test05(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("url","dummy");
		System.out.println(map.get("key"));
	}
	
	@Test
	public void test01(){
		int num=1;
		int size=6;
		String size2=String.format("%02d", size);
		System.out.println(size2);
		String str=String.format("%"+size2+"d", num);
		System.out.println("%"+size2+"d");
		System.out.println(str);
	}
	
	@Test
	public void test02(){
		System.out.println("activitys:1236".matches("activity:\\d+"));
	}
	
	@Test
	public void test03(){
		Double num=3.0;
		System.out.println(num/100);
		Integer num2=3;
		System.out.println(num2*1.0/100);
	}
	
	@Test
	public void test04(){
		String url="https://www.baidu.com/";
		url=url.replace("/", "%2F");
		url=url.replace(":", "%3A");
		System.out.println(url);
	}
	
}
