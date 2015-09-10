package com.irengine.sandbox.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.OutNewsMessageItem;

@Controller
@RequestMapping("/web")
public class WebController {

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
	
	/**\
	 *验证是否绑定手机号接口
	 * @param url
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/verify")
	public void verifyMobile(@RequestParam("url") String url,HttpServletResponse response) throws IOException{
		logger.debug("验证该用户是否绑定手机号");
		logger.debug("url:"+url);
		
		response.sendRedirect(url);
	}
	
}
