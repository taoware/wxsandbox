package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.CouponBatch;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CouponBatch entity.
 */
public interface CouponBatchRepository extends JpaRepository<CouponBatch,Long> {

}
