package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.Coupon;
import com.irengine.sandbox.repository.CouponRepository;
import com.irengine.sandbox.web.rest.util.PaginationUtil;
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
 * REST controller for managing Coupon.
 */
@RestController
@RequestMapping("/api")
public class CouponResource {

    private final Logger log = LoggerFactory.getLogger(CouponResource.class);

    @Inject
    private CouponRepository couponRepository;

    /**
     * POST  /coupons -> Create a new coupon.
     */
    @RequestMapping(value = "/coupons",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Coupon coupon) throws URISyntaxException {
        log.debug("REST request to save Coupon : {}", coupon);
        if (coupon.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new coupon cannot already have an ID").build();
        }
        couponRepository.save(coupon);
        return ResponseEntity.created(new URI("/api/coupons/" + coupon.getId())).build();
    }

    /**
     * PUT  /coupons -> Updates an existing coupon.
     */
    @RequestMapping(value = "/coupons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Coupon coupon) throws URISyntaxException {
        log.debug("REST request to update Coupon : {}", coupon);
        if (coupon.getId() == null) {
            return create(coupon);
        }
        couponRepository.save(coupon);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /coupons -> get all the coupons.
     */
    @RequestMapping(value = "/coupons",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Coupon>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Coupon> page = couponRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coupons", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coupons/:id -> get the "id" coupon.
     */
    @RequestMapping(value = "/coupons/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Coupon> get(@PathVariable Long id) {
        log.debug("REST request to get Coupon : {}", id);
        return Optional.ofNullable(couponRepository.findOne(id))
            .map(coupon -> new ResponseEntity<>(
                coupon,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coupons/:id -> delete the "id" coupon.
     */
    @RequestMapping(value = "/coupons/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Coupon : {}", id);
        couponRepository.delete(id);
    }
}
