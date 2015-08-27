package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.WCUser;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WCUser entity.
 */
public interface WCUserRepository extends JpaRepository<WCUser,Long> {

	WCUser findOneByOpenId(String openId);
	
}
