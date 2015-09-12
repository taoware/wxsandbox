package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.UserExpandInfo;
import com.irengine.sandbox.repository.UserExpandInfoRepository;
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
 * REST controller for managing UserExpandInfo.
 */
@RestController
@RequestMapping("/api")
public class UserExpandInfoResource {

    private final Logger log = LoggerFactory.getLogger(UserExpandInfoResource.class);

    @Inject
    private UserExpandInfoRepository userExpandInfoRepository;

    /**
     * POST  /userExpandInfos -> Create a new userExpandInfo.
     */
    @RequestMapping(value = "/userExpandInfos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody UserExpandInfo userExpandInfo) throws URISyntaxException {
        log.debug("REST request to save UserExpandInfo : {}", userExpandInfo);
        if (userExpandInfo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userExpandInfo cannot already have an ID").build();
        }
        userExpandInfoRepository.save(userExpandInfo);
        return ResponseEntity.created(new URI("/api/userExpandInfos/" + userExpandInfo.getId())).build();
    }

    /**
     * PUT  /userExpandInfos -> Updates an existing userExpandInfo.
     */
    @RequestMapping(value = "/userExpandInfos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody UserExpandInfo userExpandInfo) throws URISyntaxException {
        log.debug("REST request to update UserExpandInfo : {}", userExpandInfo);
        if (userExpandInfo.getId() == null) {
            return create(userExpandInfo);
        }
        userExpandInfoRepository.save(userExpandInfo);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /userExpandInfos -> get all the userExpandInfos.
     */
    @RequestMapping(value = "/userExpandInfos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserExpandInfo>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<UserExpandInfo> page = userExpandInfoRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userExpandInfos", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userExpandInfos/:id -> get the "id" userExpandInfo.
     */
    @RequestMapping(value = "/userExpandInfos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserExpandInfo> get(@PathVariable Long id) {
        log.debug("REST request to get UserExpandInfo : {}", id);
        return Optional.ofNullable(userExpandInfoRepository.findOne(id))
            .map(userExpandInfo -> new ResponseEntity<>(
                userExpandInfo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userExpandInfos/:id -> delete the "id" userExpandInfo.
     */
    @RequestMapping(value = "/userExpandInfos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete UserExpandInfo : {}", id);
        userExpandInfoRepository.delete(id);
    }
}
