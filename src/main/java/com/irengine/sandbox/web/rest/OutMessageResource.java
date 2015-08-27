package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.OutMessage;
import com.irengine.sandbox.repository.OutMessageRepository;
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
 * REST controller for managing OutMessage.
 */
@RestController
@RequestMapping("/api")
public class OutMessageResource {

    private final Logger log = LoggerFactory.getLogger(OutMessageResource.class);

    @Inject
    private OutMessageRepository outMessageRepository;

    /**
     * POST  /outMessages -> Create a new outMessage.
     */
    @RequestMapping(value = "/outMessages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody OutMessage outMessage) throws URISyntaxException {
        log.debug("REST request to save OutMessage : {}", outMessage);
        if (outMessage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new outMessage cannot already have an ID").build();
        }
        outMessageRepository.save(outMessage);
        return ResponseEntity.created(new URI("/api/outMessages/" + outMessage.getId())).build();
    }

    /**
     * PUT  /outMessages -> Updates an existing outMessage.
     */
    @RequestMapping(value = "/outMessages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody OutMessage outMessage) throws URISyntaxException {
        log.debug("REST request to update OutMessage : {}", outMessage);
        if (outMessage.getId() == null) {
            return create(outMessage);
        }
        outMessageRepository.save(outMessage);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /outMessages -> get all the outMessages.
     */
    @RequestMapping(value = "/outMessages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<OutMessage>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<OutMessage> page = outMessageRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/outMessages", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /outMessages/:id -> get the "id" outMessage.
     */
    @RequestMapping(value = "/outMessages/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OutMessage> get(@PathVariable Long id) {
        log.debug("REST request to get OutMessage : {}", id);
        return Optional.ofNullable(outMessageRepository.findOneWithEagerRelationships(id))
            .map(outMessage -> new ResponseEntity<>(
                outMessage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /outMessages/:id -> delete the "id" outMessage.
     */
    @RequestMapping(value = "/outMessages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete OutMessage : {}", id);
        outMessageRepository.delete(id);
    }
}
