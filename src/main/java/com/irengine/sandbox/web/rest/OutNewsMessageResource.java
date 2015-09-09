package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.OutNewsMessage;
import com.irengine.sandbox.repository.OutNewsMessageRepository;
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
 * REST controller for managing OutNewsMessage.
 */
@RestController
@RequestMapping("/api")
public class OutNewsMessageResource {

    private final Logger log = LoggerFactory.getLogger(OutNewsMessageResource.class);

    @Inject
    private OutNewsMessageRepository outNewsMessageRepository;

    /**
     * POST  /outNewsMessages -> Create a new outNewsMessage.
     */
    @RequestMapping(value = "/outNewsMessages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody OutNewsMessage outNewsMessage) throws URISyntaxException {
        log.debug("REST request to save OutNewsMessage : {}", outNewsMessage);
        if (outNewsMessage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new outNewsMessage cannot already have an ID").build();
        }
        outNewsMessageRepository.save(outNewsMessage);
        return ResponseEntity.created(new URI("/api/outNewsMessages/" + outNewsMessage.getId())).build();
    }

    /**
     * PUT  /outNewsMessages -> Updates an existing outNewsMessage.
     */
    @RequestMapping(value = "/outNewsMessages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody OutNewsMessage outNewsMessage) throws URISyntaxException {
        log.debug("REST request to update OutNewsMessage : {}", outNewsMessage);
        if (outNewsMessage.getId() == null) {
            return create(outNewsMessage);
        }
        outNewsMessageRepository.save(outNewsMessage);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /outNewsMessages -> get all the outNewsMessages.
     */
    @RequestMapping(value = "/outNewsMessages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<OutNewsMessage>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<OutNewsMessage> page = outNewsMessageRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/outNewsMessages", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /outNewsMessages/:id -> get the "id" outNewsMessage.
     */
    @RequestMapping(value = "/outNewsMessages/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OutNewsMessage> get(@PathVariable Long id) {
        log.debug("REST request to get OutNewsMessage : {}", id);
        return Optional.ofNullable(outNewsMessageRepository.findOne(id))
            .map(outNewsMessage -> new ResponseEntity<>(
                outNewsMessage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /outNewsMessages/:id -> delete the "id" outNewsMessage.
     */
    @RequestMapping(value = "/outNewsMessages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete OutNewsMessage : {}", id);
        outNewsMessageRepository.delete(id);
    }
}
