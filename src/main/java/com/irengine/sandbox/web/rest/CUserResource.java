package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.CUser;
import com.irengine.sandbox.repository.CUserRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CUser.
 */
@RestController
@RequestMapping("/api")
public class CUserResource {

    private final Logger log = LoggerFactory.getLogger(CUserResource.class);

    @Inject
    private CUserRepository cUserRepository;

    /**
     * POST  /cUsers -> Create a new cUser.
     */
    @RequestMapping(value = "/cUsers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody CUser cUser) throws URISyntaxException {
        log.debug("REST request to save CUser : {}", cUser);
        if (cUser.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new cUser cannot already have an ID").build();
        }
        cUserRepository.save(cUser);
        return ResponseEntity.created(new URI("/api/cUsers/" + cUser.getId())).build();
    }

    /**
     * PUT  /cUsers -> Updates an existing cUser.
     */
    @RequestMapping(value = "/cUsers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody CUser cUser) throws URISyntaxException {
        log.debug("REST request to update CUser : {}", cUser);
        if (cUser.getId() == null) {
            return create(cUser);
        }
        cUserRepository.save(cUser);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /cUsers -> get all the cUsers.
     */
    @RequestMapping(value = "/cUsers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CUser>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<CUser> page = cUserRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cUsers", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cUsers/:id -> get the "id" cUser.
     */
    @RequestMapping(value = "/cUsers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CUser> get(@PathVariable Long id) {
        log.debug("REST request to get CUser : {}", id);
        return Optional.ofNullable(cUserRepository.findOneWithEagerRelationships(id))
            .map(cUser -> new ResponseEntity<>(
                cUser,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cUsers/:id -> delete the "id" cUser.
     */
    @RequestMapping(value = "/cUsers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete CUser : {}", id);
        cUserRepository.delete(id);
    }
}
