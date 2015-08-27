package com.irengine.sandbox.test;

import org.junit.Test;

public class TestCase {

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
	
}
