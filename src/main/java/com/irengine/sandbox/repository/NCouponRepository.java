package com.irengine.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.irengine.sandbox.domain.NCoupon;

/**
 * Spring Data JPA repository for the NCoupon entity.
 */
public interface NCouponRepository extends JpaRepository<NCoupon,Long> ,JpaSpecificationExecutor<NCoupon>{

}
