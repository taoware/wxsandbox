package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.UserBasicInfo;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserBasicInfo entity.
 */
public interface UserBasicInfoRepository extends JpaRepository<UserBasicInfo,Long> {

}
