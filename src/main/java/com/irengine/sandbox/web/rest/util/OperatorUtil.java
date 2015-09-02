package com.irengine.sandbox.web.rest.util;

import org.apache.commons.lang3.StringUtils;

import com.irengine.sandbox.web.rest.util.Filter.Operator;

public class OperatorUtil {

	public static Filter.Operator getNumberFilter(String num){
		if(StringUtils.equals(num, "1")){
			return Filter.Operator.EQ;
		}
		if(StringUtils.equals(num, "2")){
			return Filter.Operator.LTE;
		}
		if(StringUtils.equals(num, "3")){
			return Filter.Operator.GTE;
		}
		return null;
	}

	public static Operator getTextFilter(String num) {
		if(StringUtils.equals(num, "1")){
			//模糊查询
			return Filter.Operator.LIKE;
		}
		if(StringUtils.equals(num, "2")){
			//等于
			return Filter.Operator.EQ;
		}
		if(StringUtils.equals(num, "3")){
			//开始字符
			return Filter.Operator.SW;
		}
		if(StringUtils.equals(num, "4")){
			//结束字符
			return Filter.Operator.EW;
		}
		return null;
	}
}
