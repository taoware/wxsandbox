package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.OutNewsMessageItem;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OutNewsMessageItem entity.
 */
public interface OutNewsMessageItemRepository extends JpaRepository<OutNewsMessageItem,Long> {

}
