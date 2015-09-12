package com.irengine.sandbox.web.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.irengine.sandbox.SmsHelper;
import com.irengine.sandbox.WeChatConnector;
import com.irengine.sandbox.commons.Constant;
import com.irengine.sandbox.commons.MessageUtil;
import com.irengine.sandbox.domain.Activity;
import com.irengine.sandbox.domain.CUser;
import com.irengine.sandbox.domain.Coupon;
import com.irengine.sandbox.domain.Goods;
import com.irengine.sandbox.domain.NCoupon;
import com.irengine.sandbox.domain.NCoupon.COUPONSTATUS;
import com.irengine.sandbox.domain.OutMessage;
import com.irengine.sandbox.domain.OutNewsMessage;
import com.irengine.sandbox.domain.OutNewsMessageItem;
import com.irengine.sandbox.domain.WCUser;
import com.irengine.sandbox.repository.GoodsRepository;
import com.irengine.sandbox.repository.OutNewsMessageRepository;
import com.irengine.sandbox.service.ActivityService;
import com.irengine.sandbox.service.CUserService;
import com.irengine.sandbox.service.NCouponService;
import com.irengine.sandbox.service.OutMessageService;
import com.irengine.sandbox.service.WCUserService;
import com.irengine.sandbox.web.rest.util.RestTemplateUtil;

@Controller
// @RequestMapping("/api/wechat")
public class EndPointController {

	private static Logger logger = LoggerFactory
			.getLogger(EndPointController.class);

	@Autowired
	private OutMessageService outMessageService;

	@Autowired
	private CUserService cUserService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private WCUserService wcUserService;

	@Autowired
	private OutNewsMessageRepository outNewsMessageRepository;

	@Autowired
	private GoodsRepository goodsRepository;
	
	@Autowired
	private NCouponService nCouponService;
	/*
	 * http://wenku.baidu.com/link?url=Z6AsEXjrbIRt-5V6wurFBXdSgQOCTRXtaR09HLdnwjTZ
	 * -WQH4GMq-_9fhS7abwGFYX2XwfXnIbupNxbrJa7KwB6_UUkgefR43Lnh5kr6YPa
	 */
	/** 此逻辑为了确定请求来自微信服务器 */
	@RequestMapping(value = "/api/wechat", method = RequestMethod.GET)
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.debug("验证是否来自微信请求");
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");

		if (!WeChatConnector.getMpService().checkSignature(timestamp, nonce,
				signature)) {
			// 消息签名不正确，说明不是公众平台发过来的消息
			response.getWriter().println("非法请求");
			return;
		}

		String echostr = request.getParameter("echostr");
		if (StringUtils.isNotBlank(echostr)) {
			// 说明是一个仅仅用来验证的请求，回显echostr
			response.getWriter().println(echostr);
			return;
		}

		String encryptType = StringUtils.isBlank(request
				.getParameter("encrypt_type")) ? "raw" : request
				.getParameter("encrypt_type");

		if ("raw".equals(encryptType)) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request
					.getInputStream());
			WxMpXmlOutMessage outMessage = WeChatConnector.getMpMessageRouter()
					.route(inMessage);
			if (outMessage != null) {
				response.getWriter().write(outMessage.toXml());
			}
			return;
		}

		if ("aes".equals(encryptType)) {
			// 是aes加密的消息
			String msgSignature = request.getParameter("msg_signature");
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
					request.getInputStream(),
					WeChatConnector.getMpConfigStorage(), timestamp, nonce,
					msgSignature);
			WxMpXmlOutMessage outMessage = WeChatConnector.getMpMessageRouter()
					.route(inMessage);
			response.getWriter().write(
					outMessage.toEncryptedXml(WeChatConnector
							.getMpConfigStorage()));
			return;
		}

		response.getWriter().println("不可识别的加密类型");
		return;
	}

	/* 创建菜单界面 */
	@RequestMapping("/menu")
	public void setupMenu(HttpServletResponse response) throws IOException {

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		// setup menu
		InputStream isMenu = new ClassPathResource("wechat-connector-menu.json")
				.getInputStream();

		try {
			WeChatConnector.getMpService().menuDelete();
			WxMenu menu = WxMenu.fromJson(isMenu);
			WeChatConnector.getMpService().menuCreate(menu);
			logger.info("create menu succeed.");
		} catch (WxErrorException e) {
			logger.error("create menu failed.");
		}

		logger.info(WeChatConnector.getMpService().oauth2buildAuthorizationUrl(
				WxConsts.OAUTH2_SCOPE_BASE, null));

		response.getWriter().println("设置菜单");
	}

	@RequestMapping("/openid")
	public void getOpenId(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		String code = request.getParameter("code");
		try {
			response.getWriter().println("<h1>code</h1>");
			response.getWriter().println(code);
			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = WeChatConnector
					.getMpService().oauth2getAccessToken(code);
			WxMpUser wxMpUser = WeChatConnector.getMpService()
					.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
			response.getWriter().println("<h1>user open id</h1>");
			response.getWriter().println(wxMpUser.getOpenId());

		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		response.getWriter().flush();
		response.getWriter().close();
	}

	private String apiUrl = "/web/verify?url=";

	@RequestMapping(value = "/api/wechat/", method = RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, DocumentException,
			WxErrorException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		logger.debug("点击按钮");
		/* 获取openId */
		// String code = request.getParameter("code");
		// WxMpOAuth2AccessToken wxMpOAuth2AccessToken = WeChatConnector
		// .getMpService().oauth2getAccessToken(code);
		// WxMpUser wxMpUser = WeChatConnector.getMpService()
		// .oauth2getUserInfo(wxMpOAuth2AccessToken, null);
		// logger.debug("openId"+wxMpUser.getOpenId());
		Map<String, String> requestMap = MessageUtil.parseXml(request);
		logger.debug(requestMap.toString());
		String toUserName = requestMap.get("FromUserName");
		String fromUserName = requestMap.get("ToUserName");
		String msgType = requestMap.get("MsgType");
		// logger.debug("msgType:"+msgType);
		// logger.debug("ToUserName"+fromUserName);
		// logger.debug("FromUserName"+toUserName);
		// logger.debug("Event:"+requestMap.get("Event"));
		// logger.debug("OrderId:"+requestMap.get("OrderId"));
		// logger.debug("OrderStatus:"+requestMap.get("OrderStatus"));
		// logger.debug("ProductId:"+requestMap.get("ProductId"));
		// logger.debug("SkuInfo:"+requestMap.get("SkuInfo"));
		if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
			String eventType = requestMap.get("Event");
			if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
				// 事件KEY值，与创建自定义菜单时指定的KEY值对应
				String eventKey = requestMap.get("EventKey");
				if (eventKey.matches("activitys:\\d+")) {
					logger.debug("推送长图文消息");
					Long outNewsMessageId = Long
							.parseLong(eventKey.split(":")[1]);
					OutNewsMessage outNewsMessage = outNewsMessageRepository
							.findOne(outNewsMessageId);
					WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS()
							.fromUser(fromUserName).toUser(toUserName).build();
					if (outNewsMessage != null) {
						logger.debug("子图文size:"
								+ outNewsMessage.getOutNewsMessageItems()
										.size());
						logger.debug("遍历添加子图文");
						for (OutNewsMessageItem outNewsMessageItem : outNewsMessage
								.getOutNewsMessageItems()) {
							WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
							item.setUrl(Constant.url + apiUrl
									+ outNewsMessageItem.getUrl());
							item.setPicUrl(outNewsMessageItem.getPicUrl());
							item.setDescription(outNewsMessageItem.getContent());
							item.setTitle(outNewsMessageItem.getContent());
							m.addArticle(item);
						}
						// WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS()
						// .fromUser(fromUserName).toUser(toUserName)
						// .addArticle(item).build();
						logger.info(m.toXml());
						response.getWriter().write(m.toXml());
						response.getWriter().close();
					}
					return;
				}else{
					
					OutMessage message = outMessageService.findOneById(Long
							.parseLong(eventKey));
					if (message == null) {
						return;
					}
					if ("news".equals(message.getType())) {
						logger.debug("推送图文消息");
						WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
						item.setUrl(message.getUrl());
						item.setPicUrl(message.getPicUrl());
						item.setDescription(message.getContent());
						item.setTitle(message.getTitle());
						WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS()
								.fromUser(fromUserName).toUser(toUserName)
								.addArticle(item).build();
						logger.info(m.toXml());
						response.getWriter().write(m.toXml());
						response.getWriter().close();
					}
					if ("text".equals(message.getType())) {
						logger.debug("推送文本消息");
						WxMpXmlOutTextMessage text = WxMpXmlOutMessage.TEXT()
								.fromUser(fromUserName).toUser(toUserName)
								.content(message.getContent()).build();
						response.getWriter().write(text.toXml());
						response.getWriter().close();
					}
				}
			}else if(eventType.equals("merchant_order")){
				/*捕获微小店付款事件*/
				logger.debug("微小店付款");
				/*得到订单号*/
				String orderId=requestMap.get("OrderId");
				/*查看商品详情,得到商品名和商品数量*/
				Map<String,Object> orderInfo=RestTemplateUtil.getOrderInfo(orderId);
				Integer product_count=(Integer) orderInfo.get("product_count");
				String product_name=(String) orderInfo.get("product_name");
				Integer order_total_price=(Integer) orderInfo.get("order_total_price");
				String returnOrderPrice=""+(order_total_price*1.0/100)+"元";
				/*根据商品名确定是发哪个供应商的提货码,并且发同等数量*/
				Goods goods=goodsRepository.findOneByName(product_name);
				if(goods==null){
					logger.debug("商品:"+product_name+",未在管理台绑定对应的批次号");
					return;
				}
				List<NCoupon> nCoupons=nCouponService.getOneUnusedNCoupon(goods.getCouponBatch(),COUPONSTATUS.UNUSED);
				if(nCoupons==null||nCoupons.size()<product_count){
					logger.debug("提货码不足");
					return;
				}
				String couponsInfo="";
				for(int i=0;i<product_count;i++){
					NCoupon nCoupon=nCoupons.get(i);
					couponsInfo+=nCoupon.getCode()+",";
				}
				couponsInfo = couponsInfo.substring(0, couponsInfo.length() - 1);
				logger.debug("couponsInfo:"+couponsInfo);
				RestTemplateUtil.sendTemplateInfo(toUserName, "https://www.baidu.com/", returnOrderPrice, couponsInfo);
			}else{
				logger.debug("eventType:"+eventType);
			}
		}
	}

	/** 得到提货码 */
	@RequestMapping("/coupon")
	public String getCoupon(HttpServletRequest request, Model model)
			throws Exception {

		String openId = request.getParameter("openid");
		logger.debug("获取提货码 openId:" + openId);
		/* 判断是否已被注册 */
		CUser user = cUserService.findOneByOpenId(openId);
		if (user != null) {
			/* 已被注册 */
			logger.debug("------userId:" + user.getId());
			model.addAttribute("msg", "您已领取过提货码:");
			model.addAttribute("coupon", user.getCoupons().get(0).getCode());
			return "1436858491142/success";
		} else {
			/* 没被注册,绑定一个提货码 */
			Coupon coupon = cUserService.registerActivity(
					"" + System.currentTimeMillis(), openId);
			logger.debug("------coupon:" + coupon.getCode());
			model.addAttribute("msg", "恭喜，您获得一枚提取码:");
			model.addAttribute("coupon", coupon.getCode());
			return "1436858491142/success";
		}
	}

	/** 输入用户信息得到提货码 */
	@RequestMapping("/register")
	public String register(HttpServletRequest request, Model model)
			throws Exception {

		String mobile = request.getParameter("username");
		String openId = request.getParameter("openid");

		logger.info("mobile: " + mobile + " openId: " + openId);
		/* 判断是否已被注册 */
		if (!cUserService.verfiyMobileAndOpenId(mobile, openId))
			/* 已被注册 */
			return "fail";
		else {
			/* 没被注册,绑定一个提货码 */
			Coupon coupon = cUserService.registerActivity(mobile, openId);
			model.addAttribute("coupon", coupon.getCode());
			return "succeed";
		}
	}

	/**
	 * 查询赠卷? (查询赠卷按钮连接的url)
	 */
	@RequestMapping("/query")
	public String query(HttpServletRequest request, Model model)
			throws Exception {
		/* 通过code得到登录用户信息 */
		String code = request.getParameter("code");

		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = WeChatConnector
				.getMpService().oauth2getAccessToken(code);
		WxMpUser wxMpUser = WeChatConnector.getMpService().oauth2getUserInfo(
				wxMpOAuth2AccessToken, null);

		String openId = wxMpUser.getOpenId();

		Coupon coupon = cUserService.queryActivity(openId);
		if (null == coupon)
			return "fail";
		else {
			model.addAttribute("coupon", coupon.getCode());
			return "succeed";
		}
	}

	/**
	 * 短信验证 从前端得到正确的验证码和手机号,并向该手机发送验证短信
	 */
	@RequestMapping("/notify")
	public void notify(@RequestParam("mobile") String mobile,
			@RequestParam("message") String message,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		// 发送验证短信
		String result = SmsHelper.send(mobile, message);

		response.getWriter().println(result);
	}

	@RequestMapping("/today/{id}")
	public void today(@PathVariable("id") Long id, HttpServletRequest request,
			HttpServletResponse response) throws IOException, WxErrorException {
		logger.debug("跳转活动,活动id=" + id);
		/* 用code能取得accesstoken,然后用accesstoken得到登录用户信息? */
		String code = request.getParameter("code");
		// AccessToken:用户的访问令牌
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = WeChatConnector
				.getMpService().oauth2getAccessToken(code);
		// WxMpUser:微信用户信息
		WxMpUser wxMpUser = WeChatConnector.getMpService().oauth2getUserInfo(
				wxMpOAuth2AccessToken, null);
		Activity activity = activityService.findOneById(id);
		if (activity != null) {
			/* 保存wcUser并且不重复记录 */
			WCUser user = wcUserService.findOneByOpenId(wxMpUser.getOpenId());
			if (user == null) {
				WCUser wcUser = new WCUser(wxMpUser.getOpenId(),
						wxMpUser.getNickname(), wxMpUser.getSex(),
						wxMpUser.getCity(), wxMpUser.getProvince(),
						wxMpUser.getCountry(), wxMpUser.getUnionId());
				user = wcUserService.save(wcUser);
			}
			/* 检测该用户是否参加过该活动 */
			boolean j = true;
			if (activity.getWcUserss().size() > 0) {
				for (WCUser wcUser1 : activity.getWcUserss()) {
					if (wcUser1.getId() == user.getId()) {
						logger.debug("该用户已经浏览过该活动");
						j = false;
						break;
					}
				}
			}
			if (j == true) {
				logger.debug("该活动人数加1");
				activity.getWcUserss().add(user);
				activityService.save(activity);
			}
			response.sendRedirect("http://bovps1.taoware.com/web/"
					+ activity.getFolderName() + "/send");
		} else {
			logger.debug("该活动不存在");
		}

	}
}
