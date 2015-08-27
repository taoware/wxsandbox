package com.irengine.sandbox.web.rest;

import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.irengine.sandbox.WeChatConnector;
import com.irengine.sandbox.commons.MessageUtil;
import com.irengine.sandbox.domain.OutMessage;
import com.irengine.sandbox.service.OutMessageService;

@Controller
public class EndPointController {

	private static Logger logger = LoggerFactory
			.getLogger(EndPointController.class);

	@Autowired
	private OutMessageService outMessageService;

	/*
	 * http://wenku.baidu.com/link?url=Z6AsEXjrbIRt-5V6wurFBXdSgQOCTRXtaR09HLdnwjTZ
	 * -WQH4GMq-_9fhS7abwGFYX2XwfXnIbupNxbrJa7KwB6_UUkgefR43Lnh5kr6YPa
	 */
	/** 此逻辑为了确定请求来自微信服务器 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

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

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, DocumentException,
			WxErrorException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		Map<String, String> requestMap = MessageUtil.parseXml(request);
		String toUserName = requestMap.get("FromUserName");
		String fromUserName = requestMap.get("ToUserName");
		String msgType = requestMap.get("MsgType");
		if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
			String eventType = requestMap.get("Event");
			if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
				// 事件KEY值，与创建自定义菜单时指定的KEY值对应
				String eventKey = requestMap.get("EventKey");
				 if (eventKey.equals("1")) {
				 WxMpXmlOutNewsMessage.Item item = new
				 WxMpXmlOutNewsMessage.Item();
				 item.setUrl("http://mp.weixin.qq.com/s?__biz=MzAxNTExNzYwOQ==&mid=210883625&idx=1&sn=1cedaeff317a9289e85443699bcb6b06#rd");
				 item.setPicUrl("http://mmbiz.qpic.cn/mmbiz/iaYUVZTQrW9BqjvI93vJQiaEtia2TPefjol5IBI0feqgSrTdfcxkLIKM7qT0AaRDatvd7iaYDg0b8JtA8bkPL1EV7Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5");
				 item.setDescription("诚邀大学生创业团队参展,优秀创业团队免收摊位费.");
				 item.setTitle("首届全国高校校园商贸（教育超市）联合采购展览洽谈会大学生志愿者报名须知");
				
				 WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS()
				 .fromUser(fromUserName).toUser(toUserName)
				 .addArticle(item).build();
				
				 logger.info(m.toXml());
				
				 response.getWriter().write(m.toXml());
				 response.getWriter().close();
				 }
				OutMessage message = outMessageService.findOneById(Long
						.parseLong(eventKey));
				if(message==null){
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
					WxMpXmlOutTextMessage text = WxMpXmlOutMessage
							.TEXT().fromUser(fromUserName).toUser(toUserName)
							.content(message.getContent()).build();
					response.getWriter().write(text.toXml());
					response.getWriter().close();
				}
			}
		}
	}
}
