package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.Coupon;
import com.irengine.sandbox.domain.Coupon.STATUS;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Coupon entity.
 */
public interface CouponRepository extends JpaRepository<Coupon,Long> {

	public Coupon findOneByCode(String code);
	
	public List<Coupon> findByCategoryAndStatus(long category, STATUS status);
	
}
