package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.OutNewsMessageItem;
import com.irengine.sandbox.repository.OutNewsMessageItemRepository;
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
 * REST controller for managing OutNewsMessageItem.
 */
@RestController
@RequestMapping("/api")
public class OutNewsMessageItemResource {

    private final Logger log = LoggerFactory.getLogger(OutNewsMessageItemResource.class);

    @Inject
    private OutNewsMessageItemRepository outNewsMessageItemRepository;

    /**
     * POST  /outNewsMessageItems -> Create a new outNewsMessageItem.
     */
    @RequestMapping(value = "/outNewsMessageItems",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody OutNewsMessageItem outNewsMessageItem) throws URISyntaxException {
        log.debug("REST request to save OutNewsMessageItem : {}", outNewsMessageItem);
        if (outNewsMessageItem.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new outNewsMessageItem cannot already have an ID").build();
        }
        outNewsMessageItemRepository.save(outNewsMessageItem);
        return ResponseEntity.created(new URI("/api/outNewsMessageItems/" + outNewsMessageItem.getId())).build();
    }

    /**
     * PUT  /outNewsMessageItems -> Updates an existing outNewsMessageItem.
     */
    @RequestMapping(value = "/outNewsMessageItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody OutNewsMessageItem outNewsMessageItem) throws URISyntaxException {
        log.debug("REST request to update OutNewsMessageItem : {}", outNewsMessageItem);
        if (outNewsMessageItem.getId() == null) {
            return create(outNewsMessageItem);
        }
        outNewsMessageItemRepository.save(outNewsMessageItem);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /outNewsMessageItems -> get all the outNewsMessageItems.
     */
    @RequestMapping(value = "/outNewsMessageItems",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<OutNewsMessageItem>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<OutNewsMessageItem> page = outNewsMessageItemRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/outNewsMessageItems", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /outNewsMessageItems/:id -> get the "id" outNewsMessageItem.
     */
    @RequestMapping(value = "/outNewsMessageItems/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OutNewsMessageItem> get(@PathVariable Long id) {
        log.debug("REST request to get OutNewsMessageItem : {}", id);
        return Optional.ofNullable(outNewsMessageItemRepository.findOne(id))
            .map(outNewsMessageItem -> new ResponseEntity<>(
                outNewsMessageItem,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /outNewsMessageItems/:id -> delete the "id" outNewsMessageItem.
     */
    @RequestMapping(value = "/outNewsMessageItems/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete OutNewsMessageItem : {}", id);
        outNewsMessageItemRepository.delete(id);
    }
}
