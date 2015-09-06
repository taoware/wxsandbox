package com.irengine.sandbox.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irengine.sandbox.domain.SupplierActivity;
import com.irengine.sandbox.repository.SupplierActivityRepository;
import com.irengine.sandbox.web.rest.util.Filter;
import com.irengine.sandbox.web.rest.util.FilterUtil;
import com.irengine.sandbox.web.rest.util.OperatorUtil;
import com.irengine.sandbox.web.rest.util.PaginationUtil;

/**
 * REST controller for managing SupplierActivity.
 */
@RestController
@RequestMapping("/api")
public class SupplierActivityResource {

    private final Logger log = LoggerFactory.getLogger(SupplierActivityResource.class);

    @Inject
    private SupplierActivityRepository supplierActivityRepository;

    /**
     * POST  /supplierActivitys -> Create a new supplierActivity.
     */
    @RequestMapping(value = "/supplierActivitys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody SupplierActivity supplierActivity) throws URISyntaxException {
        log.debug("REST request to save SupplierActivity : {}", supplierActivity);
        if (supplierActivity.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new supplierActivity cannot already have an ID").build();
        }
        supplierActivityRepository.save(supplierActivity);
        return ResponseEntity.created(new URI("/api/supplierActivitys/" + supplierActivity.getId())).build();
    }

    /**
     * PUT  /supplierActivitys -> Updates an existing supplierActivity.
     */
    @RequestMapping(value = "/supplierActivitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody SupplierActivity supplierActivity) throws URISyntaxException {
        log.debug("REST request to update SupplierActivity : {}", supplierActivity);
        if (supplierActivity.getId() == null) {
            return create(supplierActivity);
        }
        supplierActivityRepository.save(supplierActivity);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /supplierActivitys -> get all the supplierActivitys.
     */
    @RequestMapping(value = "/supplierActivitys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SupplierActivity>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<SupplierActivity> page = supplierActivityRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/supplierActivitys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /supplierActivitys/:id -> get the "id" supplierActivity.
     */
    @RequestMapping(value = "/supplierActivitys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SupplierActivity> get(@PathVariable Long id) {
        log.debug("REST request to get SupplierActivity : {}", id);
        return Optional.ofNullable(supplierActivityRepository.findOne(id))
            .map(supplierActivity -> new ResponseEntity<>(
                supplierActivity,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /supplierActivitys/:id -> delete the "id" supplierActivity.
     */
    @RequestMapping(value = "/supplierActivitys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete SupplierActivity : {}", id);
        supplierActivityRepository.delete(id);
    }

    private static final String LIMIT = "per_page";
    private static final String OFFSET = "page";
    private static final String SORTING = "sort";
    private static final String FILTERING = "filter";

    @RequestMapping(value = "/supplierActivitys/q",
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
            	String[] numberType=new String[]{"id"};
            	String[] textType=new String[]{"code","name","enabled"};
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
            Specification<SupplierActivity> specification = FilterUtil.generateSpecifications(filters, SupplierActivity.class);

            Page<SupplierActivity> page = supplierActivityRepository.findAll(specification, pageable);
            return new ResponseEntity<>(page, HttpStatus.OK);
        }

        Page<SupplierActivity> page = supplierActivityRepository.findAll(pageable);
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
