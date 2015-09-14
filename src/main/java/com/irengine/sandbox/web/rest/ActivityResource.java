package com.irengine.sandbox.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.SmsHelper;
import com.irengine.sandbox.WeChatConnector;
import com.irengine.sandbox.domain.Activity;
import com.irengine.sandbox.domain.CUser;
import com.irengine.sandbox.domain.Coupon;
import com.irengine.sandbox.domain.WCUser;
import com.irengine.sandbox.repository.ActivityRepository;
import com.irengine.sandbox.service.ActivityService;
import com.irengine.sandbox.service.CUserService;
import com.irengine.sandbox.service.WCUserService;
import com.irengine.sandbox.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Activity.
 */
@RestController
@RequestMapping("/api")
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);

    @Inject
    private ActivityRepository activityRepository;
    
    @Inject
    private CUserService cUserService;

    @Inject
    private ActivityService activityService;
    
    @Inject
    private WCUserService wcUserService;
    /**
     * POST  /activitys -> Create a new activity.
     */
    @RequestMapping(value = "/activitys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to save Activity : {}", activity);
        if (activity.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new activity cannot already have an ID").build();
        }
        activityRepository.save(activity);
//		try {
//			activityService.updateMenu();
//		} catch (UnsupportedEncodingException e) {
//			log.debug("更新菜单出错");
//			e.printStackTrace();
//		}
        return ResponseEntity.created(new URI("/api/activitys/" + activity.getId())).build();
    }

    /**
     * PUT  /activitys -> Updates an existing activity.
     */
    @RequestMapping(value = "/activitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to update Activity : {}", activity);
        if (activity.getId() == null) {
            return create(activity);
        }
        activityRepository.save(activity);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /activitys -> get all the activitys.
     */
    @RequestMapping(value = "/activitys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Activity>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Activity> page = activityRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activitys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activitys/:id -> get the "id" activity.
     */
    @RequestMapping(value = "/activitys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> get(@PathVariable Long id) {
        log.debug("REST request to get Activity : {}", id);
        return Optional.ofNullable(activityRepository.findOneWithEagerRelationships(id))
            .map(activity -> new ResponseEntity<>(
                activity,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /activitys/:id -> delete the "id" activity.
     */
    @RequestMapping(value = "/activitys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Activity : {}", id);
        activityRepository.delete(id);
    }
    
    /** 得到提货码 */
	@RequestMapping("/coupon")
	public String getCoupon(HttpServletRequest request, Model model)
			throws Exception {

		String openId = request.getParameter("openid");
		log.debug("获取提货码 openId:" + openId);
		/* 判断是否已被注册 */
		CUser user = cUserService.findOneByOpenId(openId);
		if (user != null) {
			/* 已被注册 */
			log.debug("------userId:" + user.getId());
			model.addAttribute("msg", "您已领取过提货码:");
			model.addAttribute("coupon", user.getCoupons().get(0).getCode());
			return "1436858491142/success";
		} else {
			/* 没被注册,绑定一个提货码 */
			Coupon coupon = cUserService.registerActivity(
					"" + System.currentTimeMillis(), openId);
			log.debug("------coupon:" + coupon.getCode());
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

		log.info("mobile: " + mobile + " openId: " + openId);
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
		String result = SmsHelper.send(mobile, "您的短信验证码为:"+message);

		response.getWriter().println(result);
	}

	@RequestMapping("/today/{id}")
	public void today(@PathVariable("id") Long id, HttpServletRequest request,
			HttpServletResponse response) throws IOException, WxErrorException {
		log.debug("跳转活动,活动id=" + id);
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
						log.debug("该用户已经浏览过该活动");
						j = false;
						break;
					}
				}
			}
			if (j == true) {
				log.debug("该活动人数加1");
				activity.getWcUserss().add(user);
				activityService.save(activity);
			}
			response.sendRedirect("http://vps1.taoware.com/web/"+activity.getFolderName()+"/send");
		} else {
			log.debug("该活动不存在");
		}

	}

}
