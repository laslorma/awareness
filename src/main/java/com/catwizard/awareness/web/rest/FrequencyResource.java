package com.catwizard.awareness.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catwizard.awareness.domain.Frequency;
import com.catwizard.awareness.repository.FrequencyRepository;
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
 * REST controller for managing Frequency.
 */
@RestController
@RequestMapping("/api")
public class FrequencyResource {

    private final Logger log = LoggerFactory.getLogger(FrequencyResource.class);

    @Inject
    private FrequencyRepository frequencyRepository;

    /**
     * POST  /frequencys -> Create a new frequency.
     */
    @RequestMapping(value = "/frequencys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Frequency frequency) throws URISyntaxException {
        log.debug("REST request to save Frequency : {}", frequency);
        if (frequency.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new frequency cannot already have an ID").build();
        }
        frequencyRepository.save(frequency);
        return ResponseEntity.created(new URI("/api/frequencys/" + frequency.getId())).build();
    }

    /**
     * PUT  /frequencys -> Updates an existing frequency.
     */
    @RequestMapping(value = "/frequencys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Frequency frequency) throws URISyntaxException {
        log.debug("REST request to update Frequency : {}", frequency);
        if (frequency.getId() == null) {
            return create(frequency);
        }
        frequencyRepository.save(frequency);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /frequencys -> get all the frequencys.
     */
    @RequestMapping(value = "/frequencys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Frequency> getAll() {
        log.debug("REST request to get all Frequencys");
        return frequencyRepository.findAll();
    }

    /**
     * GET  /frequencys/:id -> get the "id" frequency.
     */
    @RequestMapping(value = "/frequencys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Frequency> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Frequency : {}", id);
        Frequency frequency = frequencyRepository.findOne(id);
        if (frequency == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(frequency, HttpStatus.OK);
    }

    /**
     * DELETE  /frequencys/:id -> delete the "id" frequency.
     */
    @RequestMapping(value = "/frequencys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Frequency : {}", id);
        frequencyRepository.delete(id);
    }
}
