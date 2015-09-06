package com.irengine.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.irengine.sandbox.domain.Supplier;

/**
 * Spring Data JPA repository for the Supplier entity.
 */
public interface SupplierRepository extends JpaRepository<Supplier,Long>,JpaSpecificationExecutor<Supplier> {

}
