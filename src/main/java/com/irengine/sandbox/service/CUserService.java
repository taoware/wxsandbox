package com.irengine.sandbox.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.sandbox.domain.CUser;
import com.irengine.sandbox.domain.Coupon;
import com.irengine.sandbox.repository.CUserRepository;
import com.irengine.sandbox.repository.CouponRepository;

@Service
@Transactional
public class CUserService {

	@Inject
	private CUserRepository cUserRepository; 
	
	@Inject
	private CouponRepository couponRepository;
	
	/**验证该用户是否已经存在,如果存在返回false,不存在返回true(是否领取过提货码)*/
	public boolean verfiyMobileAndOpenId(String mobile, String openId) {
		
		List<CUser> mobileUsers = cUserRepository.findByMobile(mobile);
		List<CUser> openIdUsers = cUserRepository.findByMobile(mobile);
		if (mobileUsers.size() > 0 || openIdUsers.size() > 0)
			return false;
		return true;
	}
	
	/**注册用户(记录下该用户,防止重复领取提货码),并返回一个提货码*/
	public Coupon registerActivity(String mobile, String openId) throws Exception {
		
		CUser user = new CUser(mobile, openId);
		/*取一个unused状态的提货码给user,并把这个提货码标注为used*/
		List<Coupon> coupons = couponRepository.findByCategoryAndStatus(1L, Coupon.STATUS.Unused); 
		/*可用的提货码已空,抛异常*/
		if (0 == coupons.size()) throw new Exception("code unavailable");
		/*得到查找到的第一个提货码*/
		Coupon coupon = coupons.get(0);
		/*多对多关系*/
		user.getCoupons().add(coupon);
		cUserRepository.save(user);
		/*该条提货码状态改为"已被使用"*/
		coupon.setStatus(Coupon.STATUS.Used);
		couponRepository.save(coupon);

		return coupon;
	}
	
	/**返回该用户的提货码*/
	public Coupon queryActivity(String openId) {
		
		List<CUser> users = cUserRepository.findByOpenId(openId);
		
		if (users.size() > 0) {
			CUser user = users.get(0);
			if (null != user.getCoupons() && user.getCoupons().size() > 0)
				return user.getCoupons().get(0);
		}
		
		return null;
	}

	public CUser findOneByOpenId(String openId) {
		CUser user=null;
		List<CUser> users=cUserRepository.findByOpenId(openId);
		if(users.size()>0){
			user=users.get(0);
		}
		return user;
	}
	
}
