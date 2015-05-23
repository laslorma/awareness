package com.catwizard.awareness.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catwizard.awareness.domain.Type;
import com.catwizard.awareness.repository.TypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Type.
 */
@RestController
@RequestMapping("/api")
public class TypeResource {

    private final Logger log = LoggerFactory.getLogger(TypeResource.class);

    @Inject
    private TypeRepository typeRepository;

    /**
     * POST  /types -> Create a new type.
     */
    @RequestMapping(value = "/types",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Type type) throws URISyntaxException {
        log.debug("REST request to save Type : {}", type);
        if (type.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new type cannot already have an ID").build();
        }
        typeRepository.save(type);
        return ResponseEntity.created(new URI("/api/types/" + type.getId())).build();
    }

    /**
     * PUT  /types -> Updates an existing type.
     */
    @RequestMapping(value = "/types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Type type) throws URISyntaxException {
        log.debug("REST request to update Type : {}", type);
        if (type.getId() == null) {
            return create(type);
        }
        typeRepository.save(type);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /types -> get all the types.
     */
    @RequestMapping(value = "/types",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Type> getAll() {
        log.debug("REST request to get all Types");
        return typeRepository.findAll();
    }

    /**
     * GET  /types/:id -> get the "id" type.
     */
    @RequestMapping(value = "/types/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Type> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Type : {}", id);
        Type type = typeRepository.findOne(id);
        if (type == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(type, HttpStatus.OK);
    }

    /**
     * DELETE  /types/:id -> delete the "id" type.
     */
    @RequestMapping(value = "/types/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Type : {}", id);
        typeRepository.delete(id);
    }
}
