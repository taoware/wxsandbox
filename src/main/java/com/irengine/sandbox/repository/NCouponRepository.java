package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.NCoupon;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NCoupon entity.
 */
public interface NCouponRepository extends JpaRepository<NCoupon,Long> {

}
