package com.irengine.sandbox.web.rest.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.irengine.sandbox.domain.Activity;
import com.irengine.sandbox.domain.OutMessage;

public class DealWithMenuJson {

//	private static String initMenu = "{\"menu\":{\"button\":[{\"name\":\"今日活动\",\"sub_button\":[add-activity]},{\"name\":\"推送信息\",\"sub_button\":[add-push]}]}}";
	private static String initMenu = "{\"menu\":{\"button\":[{\"name\":\"发财/活动\",\"sub_button\":[add-activity,add-push]},{\"name\":\"车享笑派卡\",\"sub_button\":[{\"type\":\"view\",\"name\":\"个人中心\",\"url\":\"http://180.166.29.246:8089/mediawap/\"},{\"type\":\"view\",\"name\":\"网点查询\",\"url\":\"http://bovps1.taoware.com/storecat/app/index.html\"}]}]}}";

	private static Logger logger = LoggerFactory
			.getLogger(DealWithMenuJson.class);

	public static String setActivity(List<Activity> activitys,
			List<OutMessage> messages) {
		/* 添加活动菜单 */
		logger.debug("生成菜单");
		String activityMenu = "";
		String menu = initMenu;
		if (activitys.size() > 0) {
			for (Activity activity : activitys) {
				/*判断活动是跳转url还是本地活动*/
				if(activity.getType().equals("url")){
					/*url活动*/
					activityMenu += "{\"type\":\"view\",\"name\":\""
							+ activity.getName()
							+ "\",\"url\":\""+activity.getUrl()+"\"},";
				}else{
					/*本地上传活动*/
					activityMenu += "{\"type\":\"view\",\"name\":\""
							+ activity.getName()
							+ "\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa1c8a107ae33becc&redirect_uri=http%3A%2F%2Fbovps1.taoware.com%2Ftoday%2F"
							+ activity.getId()
							+ "%2F&response_type=code&scope=snsapi_userinfo#wechat_redirect\"},";
				}
			}
			activityMenu = activityMenu.substring(0, activityMenu.length() - 1);
		}else{
			activityMenu="{\"type\":\"click\",\"name\":\"无活动\",\"key\":\"null\"}";
		}
		menu = menu.replaceAll("add-activity", activityMenu);
		/* 添加推送菜单 */
		String pushMenu = "";
		if (messages.size() > 0) {
			for (OutMessage message : messages) {
				pushMenu += "{\"type\":\"click\",\"name\":\""
						+ message.getMenuName() + "\",\"key\":\""
						+ message.getId() + "\"},";
			}
			pushMenu = pushMenu.substring(0, pushMenu.length() - 1);
			
		}else{
			pushMenu="{\"type\":\"click\",\"name\":\"无推送\",\"key\":\"null\"}";
		}
		menu = menu.replaceAll("add-push", pushMenu);
		logger.debug("----------menu:"+menu);
		return menu;
	}

}
