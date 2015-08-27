package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.Activity;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
public interface ActivityRepository extends JpaRepository<Activity,Long> {

    @Query("select activity from Activity activity left join fetch activity.wcUserss where activity.id =:id")
    Activity findOneWithEagerRelationships(@Param("id") Long id);

	@Query("select a from Activity a where a.type=:type order by a.id desc")
	List<Activity> findAllByType(@Param("type") String type);
	
}
