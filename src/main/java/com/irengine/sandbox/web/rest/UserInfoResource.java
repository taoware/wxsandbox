package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.UserInfo;
import com.irengine.sandbox.repository.UserInfoRepository;
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
 * REST controller for managing UserInfo.
 */
@RestController
@RequestMapping("/api")
public class UserInfoResource {

    private final Logger log = LoggerFactory.getLogger(UserInfoResource.class);

    @Inject
    private UserInfoRepository userInfoRepository;

    /**
     * POST  /userInfos -> Create a new userInfo.
     */
    @RequestMapping(value = "/userInfos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody UserInfo userInfo) throws URISyntaxException {
        log.debug("REST request to save UserInfo : {}", userInfo);
        if (userInfo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userInfo cannot already have an ID").build();
        }
        userInfoRepository.save(userInfo);
        return ResponseEntity.created(new URI("/api/userInfos/" + userInfo.getId())).build();
    }

    /**
     * PUT  /userInfos -> Updates an existing userInfo.
     */
    @RequestMapping(value = "/userInfos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody UserInfo userInfo) throws URISyntaxException {
        log.debug("REST request to update UserInfo : {}", userInfo);
        if (userInfo.getId() == null) {
            return create(userInfo);
        }
        userInfoRepository.save(userInfo);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /userInfos -> get all the userInfos.
     */
    @RequestMapping(value = "/userInfos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserInfo>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<UserInfo> page = userInfoRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userInfos", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userInfos/:id -> get the "id" userInfo.
     */
    @RequestMapping(value = "/userInfos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserInfo> get(@PathVariable Long id) {
        log.debug("REST request to get UserInfo : {}", id);
        return Optional.ofNullable(userInfoRepository.findOne(id))
            .map(userInfo -> new ResponseEntity<>(
                userInfo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userInfos/:id -> delete the "id" userInfo.
     */
    @RequestMapping(value = "/userInfos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete UserInfo : {}", id);
        userInfoRepository.delete(id);
    }
}
