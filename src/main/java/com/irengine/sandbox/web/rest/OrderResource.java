package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irengine.sandbox.domain.Order;
import com.irengine.sandbox.repository.OrderRepository;
import com.irengine.sandbox.web.rest.util.Filter;
import com.irengine.sandbox.web.rest.util.FilterUtil;
import com.irengine.sandbox.web.rest.util.HeaderUtil;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * REST controller for managing Order.
 */
@RestController
@RequestMapping("/api")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    @Inject
    private OrderRepository orderRepository;

    /**
     * POST  /orders -> Create a new order.
     */
    @RequestMapping(value = "/orders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Order> create(@RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new order cannot already have an ID").body(null);
        }
        Order result = orderRepository.save(order);
        return ResponseEntity.created(new URI("/api/orders/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("order", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /orders -> Updates an existing order.
     */
    @RequestMapping(value = "/orders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Order> update(@RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to update Order : {}", order);
        if (order.getId() == null) {
            return create(order);
        }
        Order result = orderRepository.save(order);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("order", order.getId().toString()))
                .body(result);
    }

    /**
     * GET  /orders -> get all the orders.
     */
    @RequestMapping(value = "/orders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Order>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Order> page = orderRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private static final String LIMIT = "per_page";
    private static final String OFFSET = "page";
    private static final String SORTING = "sort";
    private static final String FILTERING = "filter";

    /**
     * GET  /orders/q -> query orders.
     */
    @RequestMapping(value = "/orders/q",
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
            	String[] numberType=new String[]{"id","myInteger","myLong","myFloat","myDouble","myDecimal","myDate"};
            	String[] textType=new String[]{"myString"};
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
            Specification<Order> specification = FilterUtil.generateSpecifications(filters, Order.class);

            Page<Order> page = orderRepository.findAll(specification, pageable);
            return new ResponseEntity<>(page, HttpStatus.OK);
        }

        Page<Order> page = orderRepository.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /orders/:id -> get the "id" order.
     */
    @RequestMapping(value = "/orders/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Order> get(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        return Optional.ofNullable(orderRepository.findOne(id))
            .map(order -> new ResponseEntity<>(
                order,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /orders/:id -> delete the "id" order.
     */
    @RequestMapping(value = "/orders/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Order : {}", id);
        orderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("order", id.toString())).build();
    }

    /*test*/
    @RequestMapping("/test2")
    	public String test2() {
    		String str="";
    		try {
    			String encoding = "UTF-8";
    			File file = new File("json.txt");
    			if (file.isFile() && file.exists()) { // 判断文件是否存在
    				InputStreamReader read = new InputStreamReader(
    						new FileInputStream(file), encoding);// 考虑到编码格式
    				BufferedReader bufferedReader = new BufferedReader(read);
    				String lineTxt = null;
    				while ((lineTxt = bufferedReader.readLine()) != null) {
    					System.out.println(lineTxt);
    					str+=lineTxt;
    				}
    				read.close();
    			} else {
    				str="找不到指定的文件";
    			}
    		} catch (Exception e) {
    			str="读取文件内容出错";
    			e.printStackTrace();
    		}
    		return str;
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
