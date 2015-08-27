package com.irengine.sandbox.web.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.irengine.sandbox.domain.Activity;
import com.irengine.sandbox.domain.WCUser;
import com.irengine.sandbox.service.ActivityService;
import com.irengine.sandbox.service.OutMessageService;
import com.irengine.sandbox.web.rest.util.PageUtil;
import com.irengine.sandbox.web.rest.util.UploadFileUtil;

@Controller
@RequestMapping("/activity")
public class ActivityWebContorller {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private OutMessageService outMessageService;

	private static Logger logger = LoggerFactory
			.getLogger(ActivityWebContorller.class);

	/**
	 * 查询活动参与人的详细信息 GET ->/activity/{activity_id}/wcUsers
	 */
	@RequestMapping("/{activity_id}/wcUsers")
	public ResponseEntity<?> getWCUser(@PathVariable("activity_id") Long id,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "limit", required = false) Integer limit) {
		List<WCUser> users = new ArrayList<WCUser>();
		users = activityService.findOneById(id).getWcUserss();
		Collections.sort(users, new Comparator<WCUser>() {
			@Override
			public int compare(WCUser o1, WCUser o2) {
				// TODO Auto-generated method stub
				return o2.getId().compareTo(o1.getId());
			}
		});
		if (offset != null && limit != null) {
			Map<String, Object> map = PageUtil.pagequery(users, offset, limit);
			return new ResponseEntity<>(map, HttpStatus.OK);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	/**
	 * 查询活动 GET ->/activity
	 */
	@RequestMapping("")
	public ResponseEntity<?> getAll(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "limit", required = false) Integer limit) {
		List<Activity> activitys = new ArrayList<Activity>();
		if (id != null) {
			logger.debug("按id查询活动,id=" + id);
			Activity activity = activityService.findOneById(id);
			return new ResponseEntity<>(activity, HttpStatus.OK);
		} else if (type != null) {
			logger.debug("按type查询活动,type=" + type);
			activitys = activityService.findAllByType(type);
		} else {
			logger.debug("查询全部活动");
			activitys = activityService.findAll();
		}
		if (offset != null && limit != null) {
			Map<String, Object> map = PageUtil.pagequery(activitys, offset,
					limit);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(activitys, HttpStatus.OK);
		}
	}

	/**
	 * 禁用活动 GET ->/activity/{id}/disable
	 */
	@RequestMapping("/{id}/disable")
	public ResponseEntity<?> disable(@PathVariable("id") Long id,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("调用禁用/启用活动接口,id=" + id);
		/* 获取当前页数 */
		String num = request.getParameter("num");
		logger.debug("----当前页数为:" + num);
		Activity activity = activityService.findOneById(id);
		if (activity.getDisable()) {
			activity.setDisable(false);
		} else {
			activity.setDisable(true);
		}
		activityService.save(activity);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 新建活动 POST ->/activity
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String save(
			@RequestParam("name") String name,
			@RequestParam("type") String type,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam("startDate") String startDateString,
			@RequestParam("endDate") String endDateString,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "url", required = false) String url,
			HttpServletRequest request, Model model) {
		logger.debug("调用新建活动接口");
		/* 处理日期 */
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime startDate;
		DateTime endDate;
		startDate = format.parseDateTime(startDateString);
		endDate = format.parseDateTime(endDateString);
		/* 上传文件 */
		try {
			if ("url".equals(type)) {
				Activity activity = new Activity(false, name, "", "", type,
						description, url, startDate, endDate);
				activityService.save(activity);
			} else {
				/* 得到解压后文件名,不能重名 */
				String folderName = UploadFileUtil.uploadAndExtractFile(file,
						request).get("name");
				Activity activity = new Activity(false, name, "index",
						folderName, type, description, url, startDate, endDate);
				activityService.save(activity);
			}
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("msg", "上传并解压文件失败");
			return "error";
		}
		/* 调用更新菜单接口 */
		try {
			activityService.updateMenu();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			model.addAttribute("msg", "更新菜单失败");
			return "error";
		}
		model.addAttribute("msg", "i got it!");
		return "listads";
	}

	/**
	 * 新建活动 POST ->/activity/test
	 */
	@RequestMapping(value = "/test", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String test(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) {
		logger.debug("调用新建活动接口");
		/* 上传文件并解压到指定路径 */
		try {
			UploadFileUtil.uploadAndExtractFile(file, request);
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
	}

	@RequestMapping(value = "/listCount")
	public ResponseEntity<?> getListCount() {
		long count = activityService.count();
		logger.debug("查询数据条数,count=" + count);
		return new ResponseEntity<>(count, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteOneById(@PathVariable("id") Long id) {
		logger.debug("删除id为" + id + "的活动");
		activityService.deleteOneById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
