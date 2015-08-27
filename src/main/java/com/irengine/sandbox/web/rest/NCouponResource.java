package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.CouponBatch;
import com.irengine.sandbox.domain.NCoupon;
import com.irengine.sandbox.domain.NCoupon.COUPONSTATUS;
import com.irengine.sandbox.repository.CouponBatchRepository;
import com.irengine.sandbox.repository.NCouponRepository;
import com.irengine.sandbox.web.rest.util.PaginationUtil;
import com.irengine.sandbox.web.rest.util.SequenceGenerator;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
    
    /**
     * POST  /nCoupons -> Create a new nCoupon.
     */
    @RequestMapping(value = "/nCoupons",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody NCoupon nCoupon) throws URISyntaxException {
        log.debug("REST request to save NCoupon : {}", nCoupon);
        if (nCoupon.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new nCoupon cannot already have an ID").build();
        }
        CouponBatch couponBatch=couponBatchRepository.findOne(nCoupon.getCouponBatch().getId());
        if(couponBatch==null){
        	return ResponseEntity.badRequest().header("Failure", "A new nCoupon should have an CouponBatch").build();
        }
        if(couponBatch.getIsGenerated()!=null&&couponBatch.getIsGenerated()==true){
        	return ResponseEntity.ok().header("Failure", "该批次号已被使用").build();
        }
        String couponBatchCode=couponBatch.getCode();
        String size=String.format("%03d", couponBatch.getSize());
        log.debug("批次号:"+couponBatch.getCode()+".生成数量为"+couponBatch.getQuantity()+"的提货码");
        List<String> serialNum=SequenceGenerator.Populate(999999, couponBatch.getQuantity());
        //检验码:待补充逻辑
        String checkNum="1";
        DateTime sysTime=new DateTime();
        for(String str:serialNum){
        	NCoupon nCoupon2=new NCoupon();
            nCoupon2.setCode(couponBatchCode+size+str+checkNum);
            nCoupon2.setCreatedTime(sysTime);
            nCoupon2.setModifedTime(sysTime);
            nCoupon2.setStatus(COUPONSTATUS.UNUSED);
            nCouponRepository.save(nCoupon2);
        }
        couponBatch.setIsGenerated(true);
        couponBatchRepository.save(couponBatch);
        //return ResponseEntity.created(new URI("/api/nCoupons/" + nCoupon.getId())).build();
        return ResponseEntity.ok().build();
    }

    /**
     * PUT  /nCoupons -> Updates an existing nCoupon.
     */
    @RequestMapping(value = "/nCoupons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody NCoupon nCoupon) throws URISyntaxException {
        log.debug("REST request to update NCoupon : {}", nCoupon);
        if (nCoupon.getId() == null) {
            return create(nCoupon);
        }
        nCouponRepository.save(nCoupon);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /nCoupons -> get all the nCoupons.
     */
    @RequestMapping(value = "/nCoupons",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NCoupon>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<NCoupon> page = nCouponRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/nCoupons", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /nCoupons/:id -> get the "id" nCoupon.
     */
    @RequestMapping(value = "/nCoupons/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NCoupon> get(@PathVariable Long id) {
        log.debug("REST request to get NCoupon : {}", id);
        return Optional.ofNullable(nCouponRepository.findOne(id))
            .map(nCoupon -> new ResponseEntity<>(
                nCoupon,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /nCoupons/:id -> delete the "id" nCoupon.
     */
    @RequestMapping(value = "/nCoupons/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete NCoupon : {}", id);
        nCouponRepository.delete(id);
    }
}
