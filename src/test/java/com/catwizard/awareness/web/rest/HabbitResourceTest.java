package com.catwizard.awareness.web.rest;

import com.catwizard.awareness.Application;
import com.catwizard.awareness.domain.Habbit;
import com.catwizard.awareness.repository.HabbitRepository;

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
 * Test class for the HabbitResource REST controller.
 *
 * @see HabbitResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HabbitResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";
    private static final String DEFAULT_FREQUENCY = "SAMPLE_TEXT";
    private static final String UPDATED_FREQUENCY = "UPDATED_TEXT";

    @Inject
    private HabbitRepository habbitRepository;

    private MockMvc restHabbitMockMvc;

    private Habbit habbit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HabbitResource habbitResource = new HabbitResource();
        ReflectionTestUtils.setField(habbitResource, "habbitRepository", habbitRepository);
        this.restHabbitMockMvc = MockMvcBuilders.standaloneSetup(habbitResource).build();
    }

    @Before
    public void initTest() {
        habbit = new Habbit();
        habbit.setDescription(DEFAULT_DESCRIPTION);
        habbit.setType(DEFAULT_TYPE);
        habbit.setFrequency(DEFAULT_FREQUENCY);
    }

    @Test
    @Transactional
    public void createHabbit() throws Exception {
        int databaseSizeBeforeCreate = habbitRepository.findAll().size();

        // Create the Habbit
        restHabbitMockMvc.perform(post("/api/habbits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(habbit)))
                .andExpect(status().isCreated());

        // Validate the Habbit in the database
        List<Habbit> habbits = habbitRepository.findAll();
        assertThat(habbits).hasSize(databaseSizeBeforeCreate + 1);
        Habbit testHabbit = habbits.get(habbits.size() - 1);
        assertThat(testHabbit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHabbit.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHabbit.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
    }

    @Test
    @Transactional
    public void getAllHabbits() throws Exception {
        // Initialize the database
        habbitRepository.saveAndFlush(habbit);

        // Get all the habbits
        restHabbitMockMvc.perform(get("/api/habbits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(habbit.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())));
    }

    @Test
    @Transactional
    public void getHabbit() throws Exception {
        // Initialize the database
        habbitRepository.saveAndFlush(habbit);

        // Get the habbit
        restHabbitMockMvc.perform(get("/api/habbits/{id}", habbit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(habbit.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHabbit() throws Exception {
        // Get the habbit
        restHabbitMockMvc.perform(get("/api/habbits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHabbit() throws Exception {
        // Initialize the database
        habbitRepository.saveAndFlush(habbit);

		int databaseSizeBeforeUpdate = habbitRepository.findAll().size();

        // Update the habbit
        habbit.setDescription(UPDATED_DESCRIPTION);
        habbit.setType(UPDATED_TYPE);
        habbit.setFrequency(UPDATED_FREQUENCY);
        restHabbitMockMvc.perform(put("/api/habbits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(habbit)))
                .andExpect(status().isOk());

        // Validate the Habbit in the database
        List<Habbit> habbits = habbitRepository.findAll();
        assertThat(habbits).hasSize(databaseSizeBeforeUpdate);
        Habbit testHabbit = habbits.get(habbits.size() - 1);
        assertThat(testHabbit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHabbit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHabbit.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void deleteHabbit() throws Exception {
        // Initialize the database
        habbitRepository.saveAndFlush(habbit);

		int databaseSizeBeforeDelete = habbitRepository.findAll().size();

        // Get the habbit
        restHabbitMockMvc.perform(delete("/api/habbits/{id}", habbit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Habbit> habbits = habbitRepository.findAll();
        assertThat(habbits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
