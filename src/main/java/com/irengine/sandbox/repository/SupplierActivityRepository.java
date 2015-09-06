package com.irengine.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.irengine.sandbox.domain.SupplierActivity;

/**
 * Spring Data JPA repository for the SupplierActivity entity.
 */
public interface SupplierActivityRepository extends JpaRepository<SupplierActivity,Long>,JpaSpecificationExecutor<SupplierActivity>  {

}
