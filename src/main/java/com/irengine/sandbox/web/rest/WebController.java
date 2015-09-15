package com.irengine.sandbox.web.rest;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.irengine.sandbox.WeChatConnector;
import com.irengine.sandbox.commons.MessageUtil;
import com.irengine.sandbox.domain.OutNewsMessageItem;
import com.irengine.sandbox.domain.UserBasicInfo;
import com.irengine.sandbox.domain.UserBasicInfo.USERSTATUS;
import com.irengine.sandbox.repository.OutNewsMessageItemRepository;
import com.irengine.sandbox.repository.OutNewsMessageRepository;
import com.irengine.sandbox.service.UserBasicInfoService;
import com.irengine.sandbox.web.rest.util.RestTemplateUtil;

@Controller
@RequestMapping("/web")
public class WebController {

	@Inject
	private UserBasicInfoService userBasicInfoService; 
	
	@Inject
	private OutNewsMessageItemRepository outNewsMessageItemRepository; 
	
	private static Logger logger = LoggerFactory.getLogger(WebController.class);

	/** 解决css读取不出问题的转发 */
	@RequestMapping("/{folderName}/send")
	public String send(@PathVariable("folderName") String folderName,
			HttpServletRequest request) {
		logger.debug("转发到" + folderName + "/index.html页面");
		return folderName + "/index";
	}

	@RequestMapping("/home")
	public String home() {
		return "login";
	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/ads")
	public String ads(){
		return "ads";
	}
	
	@RequestMapping("/draw")
	public String draw(){
		return "draw";
	}
	
	@RequestMapping("/game")
	public String game(){
		return "game";
	}
	
	@RequestMapping("/imgtext")
	public String imgtext(){
		return "imgtext";
	}
	
	@RequestMapping("/listads")
	public String listads(){
		return "listads";
	}
	
	@RequestMapping("/listdraw")
	public String listdraw(){
		return "listdraw";
	}
	
	@RequestMapping("/listgame")
	public String listgame(){
		return "listgame";
	}
	
	@RequestMapping("/listimgtext")
	public String listimgtext(){
		return "listimgtext";
	}
	
	@RequestMapping("/listtext")
	public String listtext(){
		return "listtext";
	}
	
	@RequestMapping("/userads")
	public String userads(){
		return "userads";
	}
	
	@RequestMapping("/text")
	public String text(){
		return "text";
	}
	
//	@RequestMapping("/verify")
//	public ResponseEntity<Void> verifyMobile(@RequestParam("url") String url) throws URISyntaxException{
//		logger.debug("验证该用户是否绑定手机号");
//		logger.debug("url:"+url);
//		return ResponseEntity.created(new URI(url)).build();
//	}
	
	/**
	 * 验证点击子图文活动是否绑定手机号
	 * 
	 * @param url
	 * @param response
	 * @throws IOException
	 * @throws WxErrorException
	 * @throws DocumentException
	 */
	@RequestMapping("/verify/{id}/send")
	public void verifyMobile(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) throws IOException, WxErrorException, DocumentException{
		logger.debug("验证该用户是否绑定手机号");
		logger.debug("子图文id:"+id);
		String code = request.getParameter("code");
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = WeChatConnector
				.getMpService().oauth2getAccessToken(code);
		WxMpUser wxMpUser = WeChatConnector.getMpService().oauth2getUserInfo(
				wxMpOAuth2AccessToken, null);
		String openId = wxMpUser.getOpenId();
		logger.debug("openId:"+openId);
		UserBasicInfo userBasicInfo=userBasicInfoService.findOneByOpenId(openId);
		//userBasicInfoService.dealWithOpenId(openId);
		/*检测openId是否被注册:已注册->跳转到活动,未注册->跳转到绑定页面*/
		if(userBasicInfo==null||userBasicInfo.getStatus()==USERSTATUS.unregistered){
			/*未注册金诚通*/
			/*储存该用户,只记录openId,返回id*/
			if(userBasicInfo==null){
				userBasicInfo=userBasicInfoService.save(new UserBasicInfo(null,openId,null,USERSTATUS.unregistered));
			}
			if("-1".equals(id)){
				/*点击的是个人中心*/
				logger.debug("该用户点击的是个人中心");
			}
			response.sendRedirect("/Nphone/app/index.html?id="+userBasicInfo.getId()+"&itemId="+id);
		}else{
			/*已注册金诚通,直接调转到登录后的个人中心*/
			OutNewsMessageItem outNewsMessageItem=null;
			if("-1".equals(id)){
				/*点击的是个人中心*/
				try {
					Map<String, String> returnMap = RestTemplateUtil.validate(userBasicInfo.getOpenId(), userBasicInfo.getMobile());
					logger.debug("注册/登录金诚通:returnMap:"+returnMap);
		    		logger.debug("跳转到个人中心");
		    		response.sendRedirect(returnMap.get("url"));
				} catch (Exception e) {
					logger.debug("登录金诚通失败");
					//处理逻辑(未写)
					
				}
			}else{
				/*点击的是子图文*/
				outNewsMessageItem=outNewsMessageItemRepository.findOne(Long.parseLong(id));
				if(outNewsMessageItem!=null){
					response.sendRedirect(outNewsMessageItem.getUrl());
				}
			}
		}
	}
	
}
