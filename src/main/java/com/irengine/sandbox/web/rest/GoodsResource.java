package com.irengine.sandbox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.Goods;
import com.irengine.sandbox.repository.GoodsRepository;
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
 * REST controller for managing Goods.
 */
@RestController
@RequestMapping("/api")
public class GoodsResource {

    private final Logger log = LoggerFactory.getLogger(GoodsResource.class);

    @Inject
    private GoodsRepository goodsRepository;

    /**
     * POST  /goodss -> Create a new goods.
     */
    @RequestMapping(value = "/goodss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Goods goods) throws URISyntaxException {
        log.debug("REST request to save Goods : {}", goods);
        if (goods.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new goods cannot already have an ID").build();
        }
        goodsRepository.save(goods);
        return ResponseEntity.created(new URI("/api/goodss/" + goods.getId())).build();
    }

    /**
     * PUT  /goodss -> Updates an existing goods.
     */
    @RequestMapping(value = "/goodss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Goods goods) throws URISyntaxException {
        log.debug("REST request to update Goods : {}", goods);
        if (goods.getId() == null) {
            return create(goods);
        }
        goodsRepository.save(goods);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /goodss -> get all the goodss.
     */
    @RequestMapping(value = "/goodss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Goods>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Goods> page = goodsRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/goodss", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /goodss/:id -> get the "id" goods.
     */
    @RequestMapping(value = "/goodss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Goods> get(@PathVariable Long id) {
        log.debug("REST request to get Goods : {}", id);
        return Optional.ofNullable(goodsRepository.findOne(id))
            .map(goods -> new ResponseEntity<>(
                goods,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /goodss/:id -> delete the "id" goods.
     */
    @RequestMapping(value = "/goodss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Goods : {}", id);
        goodsRepository.delete(id);
    }
}
