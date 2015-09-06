package com.irengine.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.irengine.sandbox.domain.CouponBatch;

/**
 * Spring Data JPA repository for the CouponBatch entity.
 */
public interface CouponBatchRepository extends JpaRepository<CouponBatch,Long>,JpaSpecificationExecutor<CouponBatch> {

}
