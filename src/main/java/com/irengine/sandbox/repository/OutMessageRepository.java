package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.OutMessage;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the OutMessage entity.
 */
public interface OutMessageRepository extends JpaRepository<OutMessage,Long> {

    @Query("select outMessage from OutMessage outMessage left join fetch outMessage.wcUserss where outMessage.id =:id")
    OutMessage findOneWithEagerRelationships(@Param("id") Long id);

	@Query("select o from OutMessage o where o.type=:type order by o.id desc")
	List<OutMessage> findAllByType(@Param("type") String type);
	
}
