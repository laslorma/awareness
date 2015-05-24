package com.catwizard.awareness.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catwizard.awareness.domain.Habit;
import com.catwizard.awareness.repository.HabitRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Habit.
 */
@RestController
@RequestMapping("/api")
public class HabitResource {

    private final Logger log = LoggerFactory.getLogger(HabitResource.class);

    @Inject
    private HabitRepository habitRepository;

    /**
     * POST  /habits -> Create a new habit.
     */
    @RequestMapping(value = "/habits",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Habit habit) throws URISyntaxException {
        log.debug("REST request to save Habit : {}", habit);
        if (habit.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new habit cannot already have an ID").build();
        }
        habitRepository.save(habit);
        return ResponseEntity.created(new URI("/api/habits/" + habit.getId())).build();
    }

    /**
     * PUT  /habits -> Updates an existing habit.
     */
    @RequestMapping(value = "/habits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Habit habit) throws URISyntaxException {
        log.debug("REST request to update Habit : {}", habit);
        if (habit.getId() == null) {
            return create(habit);
        }
        habitRepository.save(habit);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /habits -> get all the habits.
     */
    @RequestMapping(value = "/habits",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Habit>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Habit> page = habitRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/habits", offset, limit);
        return new ResponseEntity<List<Habit>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /habits/:id -> get the "id" habit.
     */
    @RequestMapping(value = "/habits/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Habit> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Habit : {}", id);
        Habit habit = habitRepository.findOne(id);
        if (habit == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(habit, HttpStatus.OK);
    }

    /**
     * DELETE  /habits/:id -> delete the "id" habit.
     */
    @RequestMapping(value = "/habits/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Habit : {}", id);
        habitRepository.delete(id);
    }
}
