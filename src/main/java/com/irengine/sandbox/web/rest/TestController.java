package com.irengine.sandbox.web.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.exception.WxErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.irengine.sandbox.service.ActivityService;

@Controller
@RequestMapping("/testC")
public class TestController {

	private static Logger logger = LoggerFactory
			.getLogger(TestController.class);
	
	@Autowired
	private ActivityService activityService; 
	
	@RequestMapping("/send8")
	public String testSend(@RequestParam("url") String url, HttpServletRequest request) throws WxErrorException {
		return url;
	}
	
	@RequestMapping("/send")
	public void send(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		logger.debug("test:调用转发接口");
		request.getRequestDispatcher("/html/index.html").forward(request, response);
		/*html页面中读取css等静态文件的代码换成/css/style.css能成功*/
	}
	
	@RequestMapping("/send2")
	public String send2(){
		return "redirect:/html/index.html";
	}
	
	@RequestMapping("/send3")
	public String send3(){
		return "html/index";
	}
	
	@RequestMapping("/send4")
	public String send4(){
		return "forward:/views/html/index.html";
	}
	
	@RequestMapping("/send5")
	public String send5(RedirectAttributes attr){
		attr.addAttribute("msg", "i got it!");
		return "redirect:/html/index.html";
	}
	
	@RequestMapping("/send6")
	public String send6(RedirectAttributes attr){
		attr.addFlashAttribute("msg", "i got it!");
		return "redirect:/html/index.html";
	}
	
	@RequestMapping("/send7")
	public String send7(RedirectAttributes attr){
		attr.addFlashAttribute("msg", "i got it!");
		return "redirect:/views/html/test1.jsp";
	}
	
	@RequestMapping("/menu")
	public void updateMenu(HttpServletResponse response) throws IOException{
		activityService.updateMenu();
		response.getWriter().println("立即刷新菜单菜单");
	}
	
}
