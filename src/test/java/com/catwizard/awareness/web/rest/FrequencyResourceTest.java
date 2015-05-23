package com.catwizard.awareness.web.rest;

import com.catwizard.awareness.Application;
import com.catwizard.awareness.domain.Frequency;
import com.catwizard.awareness.repository.FrequencyRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FrequencyResource REST controller.
 *
 * @see FrequencyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FrequencyResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private FrequencyRepository frequencyRepository;

    private MockMvc restFrequencyMockMvc;

    private Frequency frequency;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FrequencyResource frequencyResource = new FrequencyResource();
        ReflectionTestUtils.setField(frequencyResource, "frequencyRepository", frequencyRepository);
        this.restFrequencyMockMvc = MockMvcBuilders.standaloneSetup(frequencyResource).build();
    }

    @Before
    public void initTest() {
        frequency = new Frequency();
        frequency.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFrequency() throws Exception {
        int databaseSizeBeforeCreate = frequencyRepository.findAll().size();

        // Create the Frequency
        restFrequencyMockMvc.perform(post("/api/frequencys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(frequency)))
                .andExpect(status().isCreated());

        // Validate the Frequency in the database
        List<Frequency> frequencys = frequencyRepository.findAll();
        assertThat(frequencys).hasSize(databaseSizeBeforeCreate + 1);
        Frequency testFrequency = frequencys.get(frequencys.size() - 1);
        assertThat(testFrequency.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(frequencyRepository.findAll()).hasSize(0);
        // set the field null
        frequency.setName(null);

        // Create the Frequency, which fails.
        restFrequencyMockMvc.perform(post("/api/frequencys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(frequency)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Frequency> frequencys = frequencyRepository.findAll();
        assertThat(frequencys).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllFrequencys() throws Exception {
        // Initialize the database
        frequencyRepository.saveAndFlush(frequency);

        // Get all the frequencys
        restFrequencyMockMvc.perform(get("/api/frequencys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(frequency.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFrequency() throws Exception {
        // Initialize the database
        frequencyRepository.saveAndFlush(frequency);

        // Get the frequency
        restFrequencyMockMvc.perform(get("/api/frequencys/{id}", frequency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(frequency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFrequency() throws Exception {
        // Get the frequency
        restFrequencyMockMvc.perform(get("/api/frequencys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFrequency() throws Exception {
        // Initialize the database
        frequencyRepository.saveAndFlush(frequency);

		int databaseSizeBeforeUpdate = frequencyRepository.findAll().size();

        // Update the frequency
        frequency.setName(UPDATED_NAME);
        restFrequencyMockMvc.perform(put("/api/frequencys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(frequency)))
                .andExpect(status().isOk());

        // Validate the Frequency in the database
        List<Frequency> frequencys = frequencyRepository.findAll();
        assertThat(frequencys).hasSize(databaseSizeBeforeUpdate);
        Frequency testFrequency = frequencys.get(frequencys.size() - 1);
        assertThat(testFrequency.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteFrequency() throws Exception {
        // Initialize the database
        frequencyRepository.saveAndFlush(frequency);

		int databaseSizeBeforeDelete = frequencyRepository.findAll().size();

        // Get the frequency
        restFrequencyMockMvc.perform(delete("/api/frequencys/{id}", frequency.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Frequency> frequencys = frequencyRepository.findAll();
        assertThat(frequencys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
