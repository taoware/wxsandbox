package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.OutNewsMessage;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OutNewsMessage entity.
 */
public interface OutNewsMessageRepository extends JpaRepository<OutNewsMessage,Long> {

}
