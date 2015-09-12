package com.irengine.sandbox.web.rest.util;

import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestTemplateTest {

	private static final String Url_Validate = "http://180.166.29.246:8089/mediawap/customer/wx_validate.xhtml";
	private static final String Url_Report = "http://180.166.29.246:8089/mediawap/customer/wx_cardlost.xhtml";
	private static final String Url_Charge = "http://180.166.29.246:8089/payweb/product/reprepay.xhtml";

	@Test
	public void testValidate() {
		String wxOpenId = "yhq2015010001";
		String mobileNo = "13601234567";
		String sign = "653209bc6235b48c72f08921d514dbb5";
		
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("wxOpenId", wxOpenId);
		map.add("mobileNo", mobileNo);
		map.add("sign", sign);
		String result = restTemplate.postForObject(Url_Validate, map, String.class);
		System.out.println(result);
	}
	
	@Test
	public void testReport() {
		String wxOpenId = "yhq2015010001";
		String mobileNo = "13601234567";
		String sign = "653209bc6235b48c72f08921d514dbb5";
		
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("wxOpenId", wxOpenId);
		map.add("mobileNo", mobileNo);
		map.add("sign", sign);
		String result = restTemplate.postForObject(Url_Report, map, String.class);
		System.out.println(result);
	}
	
	@Test
	public void testCharge() {
		String merchantNo = "252002148160025";
		String userName = "13601234567";
		String orderNo = "yhq20150100002";
		String amount = "500";
		String sign = "TrphUqE4VP0IA1SJFKLm0g2UM168ujQUujuVEl1QNMHNBSV3uCj637dfMTVwthXQra%2FORx5%2FRrbf%0AT6x0U%2F2%2FueYb6dU5C%2B8Hp%2FuQqQf4DdUlARZSRXsNUsk8B4bVrkdiNsYJE%2FeKxMDdN4M5SA5AdZQj%0Ab6LSlntmyCIR1ch1298%3D";

		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("merchantNo", merchantNo);
		map.add("userName", userName);
		map.add("orderNo", orderNo);
		map.add("amount", amount);
		map.add("sign", sign);
		String result = restTemplate.postForObject(Url_Charge, map, String.class);
		System.out.println(result);
	}

}
