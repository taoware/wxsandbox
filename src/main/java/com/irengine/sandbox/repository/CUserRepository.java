package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.CUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the CUser entity.
 */
public interface CUserRepository extends JpaRepository<CUser,Long> {

    @Query("select cUser from CUser cUser left join fetch cUser.coupons where cUser.id =:id")
    CUser findOneWithEagerRelationships(@Param("id") Long id);

	List<CUser> findByMobile(String mobile);
	
	List<CUser> findByOpenId(String openId);
	
}
