package com.irengine.sandbox.repository;

import com.irengine.sandbox.domain.Goods;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Goods entity.
 */
public interface GoodsRepository extends JpaRepository<Goods,Long> {

	
	Goods findOneByName(String product_name);

}
