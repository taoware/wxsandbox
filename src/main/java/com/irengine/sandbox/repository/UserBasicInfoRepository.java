package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.UserBasicInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UserBasicInfo entity.
 */
public interface UserBasicInfoRepository extends JpaRepository<UserBasicInfo,Long> {

	@Query("select u from UserBasicInfo u where u.openId=:openId")
	UserBasicInfo findOneByOpenId(@Param("openId") String openId);

}
