package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.WCUser;
import com.irengine.sandbox.repository.WCUserRepository;
import com.irengine.sandbox.service.WCUserService;
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
 * REST controller for managing WCUser.
 */
@RestController
@RequestMapping("/api")
public class WCUserResource {

    private final Logger log = LoggerFactory.getLogger(WCUserResource.class);

    @Inject
    private WCUserService wcUserService;

	@RequestMapping(value = "/openId/{openId}/mobile", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String updateMobile(@PathVariable("openId") String openId,
			@RequestParam("mobile") String mobile) {
		WCUser wcUser=wcUserService.findOneByOpenId(openId);
		wcUser.setMobile(mobile);
		wcUserService.save(wcUser);
		return "success";
	}
	
    /**
     * POST  /wCUsers -> Create a new wCUser.
     */
    @RequestMapping(value = "/wCUsers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody WCUser wCUser) throws URISyntaxException {
        log.debug("REST request to save WCUser : {}", wCUser);
        if (wCUser.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new wCUser cannot already have an ID").build();
        }
        wcUserService.save(wCUser);
        return ResponseEntity.created(new URI("/api/wCUsers/" + wCUser.getId())).build();
    }

    /**
     * PUT  /wCUsers -> Updates an existing wCUser.
     */
    @RequestMapping(value = "/wCUsers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody WCUser wCUser) throws URISyntaxException {
        log.debug("REST request to update WCUser : {}", wCUser);
        if (wCUser.getId() == null) {
            return create(wCUser);
        }
        wcUserService.save(wCUser);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /wCUsers -> get all the wCUsers.
     */
    @RequestMapping(value = "/wCUsers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WCUser>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<WCUser> page = wcUserService.findAll(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wCUsers", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wCUsers/:id -> get the "id" wCUser.
     */
    @RequestMapping(value = "/wCUsers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WCUser> get(@PathVariable Long id) {
        log.debug("REST request to get WCUser : {}", id);
        return Optional.ofNullable(wcUserService.findOne(id))
            .map(wCUser -> new ResponseEntity<>(
                wCUser,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /wCUsers/:id -> delete the "id" wCUser.
     */
    @RequestMapping(value = "/wCUsers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete WCUser : {}", id);
        wcUserService.delete(id);
    }
}
