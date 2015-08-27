package com.irengine.sandbox.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.sandbox.WeChatConnector;
import com.irengine.sandbox.domain.Activity;
import com.irengine.sandbox.domain.OutMessage;
import com.irengine.sandbox.repository.ActivityRepository;
import com.irengine.sandbox.web.rest.util.DealWithMenuJson;

@Service
@Transactional
public class ActivityService {

	private static final Logger logger = LoggerFactory
			.getLogger(ActivityService.class);

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private OutMessageService outMessageService;

	public Activity save(Activity activity) {
		Activity returnActivity = activityRepository.save(activity);
		return returnActivity;
	}

	public List<Activity> findAll() {
		return (List<Activity>) activityRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
	}

	public Activity findOneById(Long id) {
		return activityRepository.findOne(id);
	}

	/** 每天早上00:05自动更新一次菜单 */
	@Scheduled(cron = "0 5 0 * * ?")
	public void updateMenu() throws UnsupportedEncodingException {
		logger.debug("自动生成菜单并且更新");
		/* 检测所有活动 */
		List<Activity> activitys = findAll();
		List<OutMessage> messages = outMessageService.findAll();
		/* 时间符合即更新到第一个以及菜单按钮 */
		Date now = new Date();
		// 待添加的活动
		List<Activity> addActivitys = new ArrayList<Activity>();
		List<OutMessage> addMessages = new ArrayList<OutMessage>();
		/* 查找要正在实行的活动 */
		for (Activity activity : activitys) {
			// 结束日期加一天
			Date endDate = activity.getEndDate().toDate();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(endDate);
			calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			endDate = calendar.getTime();
			if (!activity.getDisable()
					&& (now.after(activity.getStartDate().toDate()) || now
							.equals(activity.getStartDate().toDate()))
					&& (now.before(endDate) || now.equals(endDate))) {
				addActivitys.add(activity);
			}
		}
		/* 查找正在实行的推送 */
		for (OutMessage message : messages) {
			// 结束日期加一天
			Date endDate = message.getEndDate().toDate();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(endDate);
			calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			endDate = calendar.getTime();
			if (!message.getDisable()
					&& (now.after(message.getStartDate().toDate()) || now.equals(message
							.getStartDate().toDate()))
					&& (now.before(endDate) || now.equals(endDate))) {
				addMessages.add(message);
			}
		}
		/*限制最多生成5个二级菜单*/
		if(addActivitys.size()>5){
			addActivitys=addActivitys.subList(0, 5);
		}
		if(addMessages.size()>5){
			addMessages=addMessages.subList(0, 5);
		}
		/* 生成菜单文件 */
		String menuJson = DealWithMenuJson.setActivity(addActivitys,
				addMessages);
		/* 更新菜单 */
		InputStream isMenu = new ByteArrayInputStream(
				menuJson.getBytes("UTF-8"));
		try {
			WeChatConnector.getMpService().menuDelete();
			WxMenu menu = WxMenu.fromJson(isMenu);
			WeChatConnector.getMpService().menuCreate(menu);
			logger.info("创建菜单成功");
		} catch (WxErrorException e) {
			logger.error("创建菜单失败");
		}
	}

	public List<Activity> findAllByType(String type) {
		return activityRepository.findAllByType(type);
	}

	public long count() {
		return activityRepository.count();
	}

	public void deleteOneById(Long id) {
		activityRepository.delete(id);
	}
	
}
