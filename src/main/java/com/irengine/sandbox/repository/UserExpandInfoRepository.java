package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.UserExpandInfo;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserExpandInfo entity.
 */
public interface UserExpandInfoRepository extends JpaRepository<UserExpandInfo,Long> {

}
