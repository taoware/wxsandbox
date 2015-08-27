package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.SupplierActivity;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SupplierActivity entity.
 */
public interface SupplierActivityRepository extends JpaRepository<SupplierActivity,Long> {

}
