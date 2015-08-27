package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.CouponBatch;
import com.irengine.sandbox.repository.CouponBatchRepository;
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
}
