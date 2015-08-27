package com.irengine.sandbox.web.rest.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;



public class SequenceGenerator {
	
	public static List<String> Populate(int max, int numbersNeeded){
		Random rng = new Random();
		Set<Integer> generated = new LinkedHashSet<Integer>();
		while (generated.size() < numbersNeeded) {
			Integer next = rng.nextInt(max) + 1;
			generated.add(next);
		}
		List<Integer> sorted = asSortedList(generated);
		List<String> strs=new ArrayList<String>();
		int size=(""+max).length();
		String sizeStr=String.format("%02d", size);
		for(Integer num:sorted){
			String str=String.format("%"+sizeStr+"d", num);
			strs.add(str);
		}
		return strs;
	}
	
	/**
	 * 
	 * @param max :随机码最大值
	 * @param numbersNeeded 生成码的数量
	 * @param prefix每条码前面加的字符串
	 * @param writer
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void Populate(int max, int numbersNeeded, String prefix, PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
		Random rng = new Random(); // Ideally just create one instance globally
		// Note: use LinkedHashSet to maintain insertion order
		//用Set为了不重复,LinkedHashSet:有顺序
		Set<Integer> generated = new LinkedHashSet<Integer>();
		while (generated.size() < numbersNeeded) {
			Integer next = rng.nextInt(max) + 1;
			// As we're adding to a set, this will automatically do a
			// containment check
			generated.add(next);
		}
		
//		PrintWriter writer = new PrintWriter("050password.txt", "UTF-8");
//				
//		for(Integer rnd : generated)
//			writer.println(String.format("%06d", rnd));
//
//		writer.close();
		
		List<Integer> sorted = asSortedList(generated);

		for(Integer rnd : sorted)
			//四位有效数字
			writer.println(prefix + String.format("%04d", rnd));

	}
	
	public static
	<T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}



}