package com.catwizard.awareness.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catwizard.awareness.domain.Measurement;
import com.catwizard.awareness.repository.MeasurementRepository;
import com.catwizard.awareness.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Measurement.
 */
@RestController
@RequestMapping("/api")
public class MeasurementResource {

    private final Logger log = LoggerFactory.getLogger(MeasurementResource.class);

    @Inject
    private MeasurementRepository measurementRepository;

    /**
     * POST  /measurements -> Create a new measurement.
     */
    @RequestMapping(value = "/measurements",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Measurement measurement) throws URISyntaxException {
        log.debug("REST request to save Measurement : {}", measurement);
        if (measurement.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new measurement cannot already have an ID").build();
        }
        measurementRepository.save(measurement);
        return ResponseEntity.created(new URI("/api/measurements/" + measurement.getId())).build();
    }

    /**
     * PUT  /measurements -> Updates an existing measurement.
     */
    @RequestMapping(value = "/measurements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Measurement measurement) throws URISyntaxException {
        log.debug("REST request to update Measurement : {}", measurement);
        if (measurement.getId() == null) {
            return create(measurement);
        }
        measurementRepository.save(measurement);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /measurements -> get all the measurements.
     */
    @RequestMapping(value = "/measurements",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Measurement>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Measurement> page = measurementRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/measurements", offset, limit);
        return new ResponseEntity<List<Measurement>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /measurements/:id -> get the "id" measurement.
     */
    @RequestMapping(value = "/measurements/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Measurement> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Measurement : {}", id);
        Measurement measurement = measurementRepository.findOne(id);
        if (measurement == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(measurement, HttpStatus.OK);
    }

    /**
     * DELETE  /measurements/:id -> delete the "id" measurement.
     */
    @RequestMapping(value = "/measurements/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Measurement : {}", id);
        measurementRepository.delete(id);
    }
}
