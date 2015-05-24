package com.catwizard.awareness.web.rest;

import com.catwizard.awareness.Application;
import com.catwizard.awareness.domain.Measurement;
import com.catwizard.awareness.repository.MeasurementRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MeasurementResource REST controller.
 *
 * @see MeasurementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MeasurementResourceTest {


    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(1);

    private static final LocalDate DEFAULT_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE = new LocalDate();

    @Inject
    private MeasurementRepository measurementRepository;

    private MockMvc restMeasurementMockMvc;

    private Measurement measurement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeasurementResource measurementResource = new MeasurementResource();
        ReflectionTestUtils.setField(measurementResource, "measurementRepository", measurementRepository);
        this.restMeasurementMockMvc = MockMvcBuilders.standaloneSetup(measurementResource).build();
    }

    @Before
    public void initTest() {
        measurement = new Measurement();
        measurement.setValue(DEFAULT_VALUE);
        measurement.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createMeasurement() throws Exception {
        int databaseSizeBeforeCreate = measurementRepository.findAll().size();

        // Create the Measurement
        restMeasurementMockMvc.perform(post("/api/measurements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(measurement)))
                .andExpect(status().isCreated());

        // Validate the Measurement in the database
        List<Measurement> measurements = measurementRepository.findAll();
        assertThat(measurements).hasSize(databaseSizeBeforeCreate + 1);
        Measurement testMeasurement = measurements.get(measurements.size() - 1);
        assertThat(testMeasurement.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testMeasurement.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(measurementRepository.findAll()).hasSize(0);
        // set the field null
        measurement.setValue(null);

        // Create the Measurement, which fails.
        restMeasurementMockMvc.perform(post("/api/measurements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(measurement)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Measurement> measurements = measurementRepository.findAll();
        assertThat(measurements).hasSize(0);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(measurementRepository.findAll()).hasSize(0);
        // set the field null
        measurement.setDate(null);

        // Create the Measurement, which fails.
        restMeasurementMockMvc.perform(post("/api/measurements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(measurement)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Measurement> measurements = measurementRepository.findAll();
        assertThat(measurements).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllMeasurements() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurements
        restMeasurementMockMvc.perform(get("/api/measurements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get the measurement
        restMeasurementMockMvc.perform(get("/api/measurements/{id}", measurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(measurement.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMeasurement() throws Exception {
        // Get the measurement
        restMeasurementMockMvc.perform(get("/api/measurements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

		int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Update the measurement
        measurement.setValue(UPDATED_VALUE);
        measurement.setDate(UPDATED_DATE);
        restMeasurementMockMvc.perform(put("/api/measurements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(measurement)))
                .andExpect(status().isOk());

        // Validate the Measurement in the database
        List<Measurement> measurements = measurementRepository.findAll();
        assertThat(measurements).hasSize(databaseSizeBeforeUpdate);
        Measurement testMeasurement = measurements.get(measurements.size() - 1);
        assertThat(testMeasurement.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMeasurement.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

		int databaseSizeBeforeDelete = measurementRepository.findAll().size();

        // Get the measurement
        restMeasurementMockMvc.perform(delete("/api/measurements/{id}", measurement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Measurement> measurements = measurementRepository.findAll();
        assertThat(measurements).hasSize(databaseSizeBeforeDelete - 1);
    }
}
