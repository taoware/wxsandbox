package com.irengine.sandbox.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.sandbox.domain.UserBasicInfo;
import com.irengine.sandbox.domain.UserBasicInfo.USERSTATUS;
import com.irengine.sandbox.repository.UserBasicInfoRepository;

@Service
@Transactional
public class UserBasicInfoService {
	
	@Inject
	private UserBasicInfoRepository userBasicInfoRepository;

	public boolean verifyOpenId(String openId){
		/*检测openId是否绑定手机号*/
		UserBasicInfo user=userBasicInfoRepository.findOneByOpenId(openId);
		if(user!=null&&user.getStatus()==USERSTATUS.registered){
			return true;
		}
		return false;
	}

	public UserBasicInfo save(UserBasicInfo userBasicInfo) {
		return userBasicInfoRepository.save(userBasicInfo);
	}
	
//	/**保存用户openId和mobile*/
//	public void saveBasicInfo(String openId,String mobile) {
//		/*查找是否存在该openId的用户信息:存在->检测mobile,不存在->储存openId和mobile*/
//		UserBasicInfo userBasicInfo=userBasicInfoRepository.findOneByOpenId(openId);
//		//if(userBasicInfo)
//	}
	
}
