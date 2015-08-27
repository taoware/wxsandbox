package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.SupplierActivity;
import com.irengine.sandbox.repository.SupplierActivityRepository;
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
}
