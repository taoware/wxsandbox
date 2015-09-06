package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irengine.sandbox.domain.CouponBatch;
import com.irengine.sandbox.repository.CouponBatchRepository;
import com.irengine.sandbox.web.rest.util.Filter;
import com.irengine.sandbox.web.rest.util.FilterUtil;
import com.irengine.sandbox.web.rest.util.OperatorUtil;
import com.irengine.sandbox.web.rest.util.PaginationUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

/**
 * REST controller for managing CouponBatch.
 */
@RestController
@RequestMapping("/api")
public class CouponBatchResource {

    private final Logger log = LoggerFactory.getLogger(CouponBatchResource.class);

    @Inject
    private CouponBatchRepository couponBatchRepository;

    //code前缀
    private static String prefix="";
    //code后缀
    private static String suffix="";
    
    /**
     * POST  /couponBatchs -> Create a new couponBatch.
     */
    @RequestMapping(value = "/couponBatchs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody CouponBatch couponBatch) throws URISyntaxException {
        log.debug("REST request to save CouponBatch : {}", couponBatch);
        if (couponBatch.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new couponBatch cannot already have an ID").build();
        }
        couponBatch.setEnabled(true);
        couponBatch.setIsGenerated(false);
        couponBatch.setCode(prefix+couponBatch.getCode()+suffix);
        couponBatchRepository.save(couponBatch);
        return ResponseEntity.created(new URI("/api/couponBatchs/" + couponBatch.getId())).build();
    }

    /**
     * PUT  /couponBatchs -> Updates an existing couponBatch.
     */
    @RequestMapping(value = "/couponBatchs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody CouponBatch couponBatch) throws URISyntaxException {
        log.debug("REST request to update CouponBatch : {}", couponBatch);
        if (couponBatch.getId() == null) {
            return create(couponBatch);
        }
        couponBatchRepository.save(couponBatch);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /couponBatchs -> get all the couponBatchs.
     */
    @RequestMapping(value = "/couponBatchs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CouponBatch>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<CouponBatch> page = couponBatchRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/couponBatchs", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /couponBatchs/:id -> get the "id" couponBatch.
     */
    @RequestMapping(value = "/couponBatchs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CouponBatch> get(@PathVariable Long id) {
        log.debug("REST request to get CouponBatch : {}", id);
        return Optional.ofNullable(couponBatchRepository.findOne(id))
            .map(couponBatch -> new ResponseEntity<>(
                couponBatch,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /couponBatchs/:id -> delete the "id" couponBatch.
     */
    @RequestMapping(value = "/couponBatchs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete CouponBatch : {}", id);
        couponBatchRepository.delete(id);
    }
    
    private static final String LIMIT = "per_page";
    private static final String OFFSET = "page";
    private static final String SORTING = "sort";
    private static final String FILTERING = "filter";

    @RequestMapping(value = "/couponBatchs/q",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> query(@RequestParam Map<String, Object> params) {

        // pagination
        Pageable pageable;
        Integer offset = Integer.parseInt(params.get(OFFSET).toString());
        Integer limit = Integer.parseInt(params.get(LIMIT).toString());

        // sorting
        if (params.containsKey(SORTING) && !params.get(SORTING).toString().isEmpty()) {

            Map<String, String> sort = new HashMap<>();
            //ObjectMapper:json解析功能
            ObjectMapper mapper = new ObjectMapper();

            try {
                sort = mapper.readValue(params.get(SORTING).toString(), new TypeReference<Map<String, String>>(){});
            } catch (Exception e) {
                e.printStackTrace();
            }

            log.debug("request with pagination and sorting");
            pageable = PaginationUtil.generatePageRequest(offset, limit, sort.get("field"), sort.get("sort"));
        }
        else {
            log.debug("request with pagination");
            pageable = PaginationUtil.generatePageRequest(offset, limit);
        }

        // filtering support angular grid filtering number and text
        if (params.containsKey(FILTERING) && !params.get(FILTERING).toString().isEmpty()) {

            Map<String, Map<String, String>> filter = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();

            try {
            	//获得filter
                filter = mapper.readValue(params.get(FILTERING).toString(), new TypeReference<Map<String, Map<String, String>>>(){});
                log.debug(filter.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // sample filtering field,operator,value
            log.debug("request with filtering");

            List<Filter> filters = new ArrayList<>();
            for(Entry<String, Map<String, String>> filterParam:filter.entrySet()){
            	String[] numberType=new String[]{"id","code","size","quantity"};
            	String[] textType=new String[]{};
            	if(contains(filterParam.getKey(), numberType)){
            		log.debug("判断为number类型");
            		filters.add(new Filter(filterParam.getKey(), OperatorUtil.getNumberFilter(filterParam.getValue().get("type")), filterParam.getValue().get("filter")));
            	}
            	if(contains(filterParam.getKey(), textType)){
            		log.debug("判断为text类型");
            		filters.add(new Filter(filterParam.getKey(), OperatorUtil.getTextFilter(filterParam.getValue().get("type")), filterParam.getValue().get("filter")));
            	}
            }
            //filters.add(new Filter("id", Filter.Operator.EQ, "2"));
            //filters.add(filter);
            Specification<CouponBatch> specification = FilterUtil.generateSpecifications(filters, CouponBatch.class);

            Page<CouponBatch> page = couponBatchRepository.findAll(specification, pageable);
            return new ResponseEntity<>(page, HttpStatus.OK);
        }

        Page<CouponBatch> page = couponBatchRepository.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    private boolean contains(String str,String[] strs){
    	for(String str2:strs){
    		if(StringUtils.equals(str, str2)){
    			return true;
    		}
    	}
    	return false;
    }
    
}
