package com.catwizard.awareness.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catwizard.awareness.domain.Habbit;
import com.catwizard.awareness.repository.HabbitRepository;
import com.catwizard.awareness.web.rest.util.PaginationUtil;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Habbit.
 */
@RestController
@RequestMapping("/api")
public class HabbitResource {

    private final Logger log = LoggerFactory.getLogger(HabbitResource.class);

    @Inject
    private HabbitRepository habbitRepository;

    /**
     * POST  /habbits -> Create a new habbit.
     */
    @RequestMapping(value = "/habbits",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Habbit habbit) throws URISyntaxException {
        log.debug("REST request to save Habbit : {}", habbit);
        if (habbit.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new habbit cannot already have an ID").build();
        }
        habbitRepository.save(habbit);
        return ResponseEntity.created(new URI("/api/habbits/" + habbit.getId())).build();
    }

    /**
     * PUT  /habbits -> Updates an existing habbit.
     */
    @RequestMapping(value = "/habbits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Habbit habbit) throws URISyntaxException {
        log.debug("REST request to update Habbit : {}", habbit);
        if (habbit.getId() == null) {
            return create(habbit);
        }
        habbitRepository.save(habbit);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /habbits -> get all the habbits.
     */
    @RequestMapping(value = "/habbits",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Habbit>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Habbit> page = habbitRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/habbits", offset, limit);
        return new ResponseEntity<List<Habbit>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /habbits/:id -> get the "id" habbit.
     */
    @RequestMapping(value = "/habbits/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Habbit> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Habbit : {}", id);
        Habbit habbit = habbitRepository.findOne(id);
        if (habbit == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(habbit, HttpStatus.OK);
    }

    /**
     * DELETE  /habbits/:id -> delete the "id" habbit.
     */
    @RequestMapping(value = "/habbits/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Habbit : {}", id);
        habbitRepository.delete(id);
    }
}
