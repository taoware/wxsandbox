package com.irengine.sandbox.web.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irengine.sandbox.domain.CouponBatch;
import com.irengine.sandbox.domain.NCoupon;
import com.irengine.sandbox.domain.NCoupon.COUPONSTATUS;
import com.irengine.sandbox.repository.CouponBatchRepository;
import com.irengine.sandbox.repository.NCouponRepository;
import com.irengine.sandbox.service.NCouponService;
import com.irengine.sandbox.web.rest.util.Filter;
import com.irengine.sandbox.web.rest.util.FilterUtil;
import com.irengine.sandbox.web.rest.util.OperatorUtil;
import com.irengine.sandbox.web.rest.util.PaginationUtil;
import com.irengine.sandbox.web.rest.util.SequenceGenerator;

/**
 * REST controller for managing NCoupon.
 */
@RestController
@RequestMapping("/api")
public class NCouponResource {

	private final Logger log = LoggerFactory.getLogger(NCouponResource.class);

	@Inject
	private NCouponRepository nCouponRepository;

	@Inject
	private CouponBatchRepository couponBatchRepository;

	@Inject
	private NCouponService nCouponService;
	
	private static final String LIMIT = "per_page";
	private static final String OFFSET = "page";
	private static final String SORTING = "sort";
	private static final String FILTERING = "filter";

	@RequestMapping(value = "/nCoupons/q", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> query(@RequestParam Map<String, Object> params) {

		// pagination
		Pageable pageable;
		Integer offset = Integer.parseInt(params.get(OFFSET).toString());
		Integer limit = Integer.parseInt(params.get(LIMIT).toString());

		// sorting
		if (params.containsKey(SORTING)
				&& !params.get(SORTING).toString().isEmpty()) {

			Map<String, String> sort = new HashMap<>();
			// ObjectMapper:json解析功能
			ObjectMapper mapper = new ObjectMapper();

			try {
				sort = mapper.readValue(params.get(SORTING).toString(),
						new TypeReference<Map<String, String>>() {
						});
			} catch (Exception e) {
				e.printStackTrace();
			}

			log.debug("request with pagination and sorting");
			pageable = PaginationUtil.generatePageRequest(offset, limit,
					sort.get("field"), sort.get("sort"));
		} else {
			log.debug("request with pagination");
			pageable = PaginationUtil.generatePageRequest(offset, limit);
		}

		// filtering support angular grid filtering number and text
		if (params.containsKey(FILTERING)
				&& !params.get(FILTERING).toString().isEmpty()) {

			Map<String, Map<String, String>> filter = new HashMap<>();
			ObjectMapper mapper = new ObjectMapper();

			try {
				// 获得filter
				filter = mapper.readValue(params.get(FILTERING).toString(),
						new TypeReference<Map<String, Map<String, String>>>() {
						});
				log.debug(filter.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			// sample filtering field,operator,value
			log.debug("request with filtering");

			List<Filter> filters = new ArrayList<>();
			for (Entry<String, Map<String, String>> filterParam : filter
					.entrySet()) {
				String[] numberType = new String[] { "id" };
				String[] textType = new String[] { "code", "status" };
				if (contains(filterParam.getKey(), numberType)) {
					log.debug("判断为number类型");
					filters.add(new Filter(filterParam.getKey(),
							OperatorUtil.getNumberFilter(filterParam.getValue()
									.get("type")), filterParam.getValue().get(
									"filter")));
				}
				if (contains(filterParam.getKey(), textType)) {
					log.debug("判断为text类型");
					filters.add(new Filter(filterParam.getKey(), OperatorUtil
							.getTextFilter(filterParam.getValue().get("type")),
							filterParam.getValue().get("filter")));
				}
			}
			// filters.add(new Filter("id", Filter.Operator.EQ, "2"));
			// filters.add(filter);
			Specification<NCoupon> specification = FilterUtil
					.generateSpecifications(filters, NCoupon.class);

			Page<NCoupon> page = nCouponRepository.findAll(specification,
					pageable);
			return new ResponseEntity<>(page, HttpStatus.OK);
		}

		Page<NCoupon> page = nCouponRepository.findAll(pageable);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	private boolean contains(String str, String[] strs) {
		for (String str2 : strs) {
			if (StringUtils.equals(str, str2)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * POST /nCoupons -> Create a new nCoupon.
	 */
	@RequestMapping(value = "/nCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> create(/* @Valid */@RequestBody NCoupon nCoupon)
			throws URISyntaxException {
		log.debug("REST request to save NCoupon : {}", nCoupon);
		if (nCoupon.getId() != null) {
			return ResponseEntity
					.badRequest()
					.header("Failure",
							"A new nCoupon cannot already have an ID").build();
		}
		CouponBatch couponBatch = couponBatchRepository.findOne(nCoupon
				.getCouponBatch().getId());
		if (couponBatch == null) {
			return ResponseEntity
					.badRequest()
					.header("Failure",
							"A new nCoupon should have an CouponBatch").build();
		}
		if (couponBatch.getIsGenerated() != null
				&& couponBatch.getIsGenerated() == true) {
			return ResponseEntity.ok().header("Failure", "该批次号已被使用").build();
		}
		String couponBatchCode = couponBatch.getCode();
		String size = String.format("%03d", couponBatch.getSize());
		log.debug("批次号:" + couponBatch.getCode() + ".生成数量为"
				+ couponBatch.getQuantity() + "的提货码");
		List<String> serialNum = SequenceGenerator.Populate(999999,
				couponBatch.getQuantity());
		// 检验码:待补充逻辑
		String checkNum = "1";
		DateTime sysTime = new DateTime();
		for (String str : serialNum) {
			NCoupon nCoupon2 = new NCoupon();
			nCoupon2.setCode(couponBatchCode + size + str + checkNum);
			nCoupon2.setCreatedTime(sysTime);
			nCoupon2.setModifedTime(sysTime);
			nCoupon2.setStatus(COUPONSTATUS.UNUSED);
			nCouponRepository.save(nCoupon2);
		}
		couponBatch.setIsGenerated(true);
		couponBatchRepository.save(couponBatch);
		// return ResponseEntity.created(new URI("/api/nCoupons/" +
		// nCoupon.getId())).build();
		return ResponseEntity.ok().build();
	}

	/**
	 * PUT /nCoupons -> Updates an existing nCoupon.
	 */
	@RequestMapping(value = "/nCoupons", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> update(@Valid @RequestBody NCoupon nCoupon)
			throws URISyntaxException {
		log.debug("REST request to update NCoupon : {}", nCoupon);
		if (nCoupon.getId() == null) {
			return create(nCoupon);
		}
		nCouponRepository.save(nCoupon);
		return ResponseEntity.ok().build();
	}

	/**
	 * GET /nCoupons -> get all the nCoupons.
	 */
	@RequestMapping(value = "/nCoupons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<NCoupon>> getAll(
			@RequestParam(value = "page", required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit)
			throws URISyntaxException {
		Page<NCoupon> page = nCouponRepository.findAll(PaginationUtil
				.generatePageRequest(offset, limit));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/nCoupons", offset, limit);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /nCoupons/:id -> get the "id" nCoupon.
	 */
	@RequestMapping(value = "/nCoupons/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<NCoupon> get(@PathVariable Long id) {
		log.debug("REST request to get NCoupon : {}", id);
		return Optional.ofNullable(nCouponRepository.findOne(id))
				.map(nCoupon -> new ResponseEntity<>(nCoupon, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /nCoupons/:id -> delete the "id" nCoupon.
	 */
	@RequestMapping(value = "/nCoupons/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete NCoupon : {}", id);
		nCouponRepository.delete(id);
	}
	
	/**
	 * 提货码导出excel
	 * GET /nCoupons/e 
	 */
	@RequestMapping(value = "/nCoupons/e", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> exporToExcel(HttpServletResponse response){
		/*查询结果*/
		List<NCoupon> nCoupons=nCouponRepository.findAll();
		/*导出excel*/
		try {
			nCouponService.createExcel(nCoupons,response);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("创建excel失败",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
