package com.irengine.sandbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.irengine.sandbox.domain.NCoupon;
import com.irengine.sandbox.domain.NCoupon.COUPONSTATUS;

/**
 * Spring Data JPA repository for the NCoupon entity.
 */
public interface NCouponRepository extends JpaRepository<NCoupon,Long> ,JpaSpecificationExecutor<NCoupon>{

	@Query("select n from NCoupon n where n.couponBatch.id=:id and n.status=:status")
	List<NCoupon> findAllByCouponBatchAndStatus(@Param("id")Long id, @Param("status")COUPONSTATUS status);

}
