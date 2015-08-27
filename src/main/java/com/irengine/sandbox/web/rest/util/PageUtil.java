package com.irengine.sandbox.web.rest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageUtil {

	private static Logger logger = LoggerFactory
			.getLogger(PageUtil.class);
	
	public static <T> Map<String, Object> pagequery(List<T> list,
			int offset, int limit) {
		logger.debug("分页查询,limit="+limit+",offset="+offset);
		Map<String,Object> map=new HashMap<String, Object>();
		/*得到总页数*/
		int totalpage=list.size()%limit>0?(list.size()/limit)+1:list.size()/limit;
		map.put("totalpage", totalpage);
		int start=(offset-1)*limit;
		int end=offset*limit;
		if(end>list.size()){
			end=list.size();
		}
		if(end<=start){
			list=new ArrayList<T>();
		}else{
			list=list.subList(start,end);
		}
		logger.debug("分页查询返回的数据条数为:"+list.size());
		map.put("list", list);
		return map;
	}

}
