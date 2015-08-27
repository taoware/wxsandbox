package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.Order;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Order entity.
 */
public interface OrderRepository extends JpaRepository<Order,Long> {

}
