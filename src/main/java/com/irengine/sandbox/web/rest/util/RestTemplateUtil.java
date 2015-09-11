package com.irengine.sandbox.web.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.irengine.sandbox.WeChatConnectorInMemoryConfigStorage;

public class RestTemplateUtil {

	//得到token
	private String getAccessToken="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";
	//输入订单得到订单详情
	private String getOrderInfo="https://api.weixin.qq.com/merchant/order/getbyid?access_token={ACCESS_TOKEN}";

	public String getAccessToken() throws IOException {
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
	
	@Test
	public void getOrderInfo() throws IOException {
		String accessToken = getAccessToken();
		String order_id="14057567885757129933";
		getOrderInfo=getOrderInfo.replace("{ACCESS_TOKEN}", accessToken);
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", order_id);
		System.out.println(getOrderInfo);
		System.out.println(map);
		//String result = restTemplate.postForObject(getOrderInfo, "{\"order_id\": \"14057567885757129933\"}", String.class);
		String result = restTemplate.postForObject(getOrderInfo, map, String.class);
		System.out.println(result);
	}
	
}
