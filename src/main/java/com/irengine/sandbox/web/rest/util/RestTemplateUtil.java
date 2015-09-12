package com.irengine.sandbox.web.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import com.irengine.sandbox.WeChatConnectorInMemoryConfigStorage;
import com.irengine.sandbox.domain.TextInfo;

public class RestTemplateUtil {

	//得到token
	private static String getAccessToken="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";
	//输入订单得到订单详情
	private static String getOrderInfo="https://api.weixin.qq.com/merchant/order/getbyid?access_token={ACCESS_TOKEN}";
	//发送模版消息
	private static String sendTemplateInfo="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";
	
	/**
	 * 获得token
	 * @return
	 * @throws IOException
	 */
	public static String getAccessToken() throws IOException {
		RestTemplate restTemplate = new RestTemplate();
	    InputStream is = new ClassPathResource("wechat-connector.xml").getInputStream();
	    WeChatConnectorInMemoryConfigStorage config = WeChatConnectorInMemoryConfigStorage.fromXml(is);
	    getAccessToken=getAccessToken.replace("{APPID}", config.getAppId());
	    getAccessToken=getAccessToken.replace("{APPSECRET}", config.getSecret());
	    //System.out.println(getAccessToken);
		@SuppressWarnings("unchecked")
		Map<String,String> result = restTemplate.getForObject(getAccessToken, Map.class);
		//System.out.println(result.get("access_token"));
		return result.get("access_token");
	}
	
	/**
	 * 输入订单号得到订单详情
	 * @param order_id
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getOrderInfo(String order_id) throws IOException {
		String accessToken = getAccessToken();
		getOrderInfo=getOrderInfo.replace("{ACCESS_TOKEN}", accessToken);
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", order_id);
		System.out.println(getOrderInfo);
		//System.out.println(map);
		//String result = restTemplate.postForObject(getOrderInfo, "{\"order_id\": \"14057567885757129933\"}", String.class);
		String result = restTemplate.postForObject(getOrderInfo, map, String.class);
		result =new String(result.getBytes("ISO-8859-1"),"UTF-8");
		//System.out.println(result);
		JSONObject  jasonObject = JSONObject.fromObject(result);
		Map<String,Object> returnMap = (Map<String,Object>)jasonObject;
		Map<String,Object> orderInfo=(Map<String,Object>)(returnMap.get("order"));
//		System.out.println("product_count:"+orderInfo.get("product_count"));
//		System.out.println("product_name:"+orderInfo.get("product_name"));
		return orderInfo;
	}
	/**
	 * 发送模版消息
	 */
	public static String sendTemplateInfo(String toUser,String url,String money,String couponsInfo) throws IOException{
		String accessToken = getAccessToken();
		sendTemplateInfo=sendTemplateInfo.replace("{ACCESS_TOKEN}", accessToken);
		System.out.println(accessToken);
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("touser", toUser);
		map.put("template_id", "WIr110lsBwDltd3R9jJANKuvdbA5ez17Mi4_Y7g2zpc");
		map.put("url", url);
		map.put("topcolor","#FF0000");
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("first", new TextInfo("恭喜您购买成功！电子码见商品信息,也可点击'我的电子码'菜单查询", "#173177"));
		data.put("orderMoneySum", new TextInfo(money, "#173177"));
		data.put("orderProductName", new TextInfo(couponsInfo, "#173177"));
		map.put("data", data);
		String result=restTemplate.postForObject(sendTemplateInfo, map, String.class);
		return result;
	}
	
	@Test
	public void testSendTemplateInfo() throws IOException{
		System.out.println(sendTemplateInfo("og4MWwzPcHSBc8Jmxt6ZIebNuCTI", "https://www.baidu.com/", "0.2", "81654616"));
	}
	
	@Test
	public void testInterface() throws IOException{
		getOrderInfo("14057567885757129933");
	}
	
}
