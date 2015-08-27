package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.Supplier;
import com.irengine.sandbox.repository.SupplierRepository;
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
 * REST controller for managing Supplier.
 */
@RestController
@RequestMapping("/api")
public class SupplierResource {

    private final Logger log = LoggerFactory.getLogger(SupplierResource.class);

    @Inject
    private SupplierRepository supplierRepository;

    /**
     * POST  /suppliers -> Create a new supplier.
     */
    @RequestMapping(value = "/suppliers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to save Supplier : {}", supplier);
        if (supplier.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new supplier cannot already have an ID").build();
        }
        supplierRepository.save(supplier);
        return ResponseEntity.created(new URI("/api/suppliers/" + supplier.getId())).build();
    }

    /**
     * PUT  /suppliers -> Updates an existing supplier.
     */
    @RequestMapping(value = "/suppliers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to update Supplier : {}", supplier);
        if (supplier.getId() == null) {
            return create(supplier);
        }
        supplierRepository.save(supplier);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /suppliers -> get all the suppliers.
     */
    @RequestMapping(value = "/suppliers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Supplier>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Supplier> page = supplierRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/suppliers", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /suppliers/:id -> get the "id" supplier.
     */
    @RequestMapping(value = "/suppliers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Supplier> get(@PathVariable Long id) {
        log.debug("REST request to get Supplier : {}", id);
        return Optional.ofNullable(supplierRepository.findOne(id))
            .map(supplier -> new ResponseEntity<>(
                supplier,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /suppliers/:id -> delete the "id" supplier.
     */
    @RequestMapping(value = "/suppliers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Supplier : {}", id);
        supplierRepository.delete(id);
    }
}
