package com.catwizard.awareness.web.rest;

import com.catwizard.awareness.Application;
import com.catwizard.awareness.domain.Habit;
import com.catwizard.awareness.repository.HabitRepository;

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
 * Test class for the HabitResource REST controller.
 *
 * @see HabitResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HabitResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private HabitRepository habitRepository;

    private MockMvc restHabitMockMvc;

    private Habit habit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HabitResource habitResource = new HabitResource();
        ReflectionTestUtils.setField(habitResource, "habitRepository", habitRepository);
        this.restHabitMockMvc = MockMvcBuilders.standaloneSetup(habitResource).build();
    }

    @Before
    public void initTest() {
        habit = new Habit();
        habit.setName(DEFAULT_NAME);
        habit.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createHabit() throws Exception {
        int databaseSizeBeforeCreate = habitRepository.findAll().size();

        // Create the Habit
        restHabitMockMvc.perform(post("/api/habits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(habit)))
                .andExpect(status().isCreated());

        // Validate the Habit in the database
        List<Habit> habits = habitRepository.findAll();
        assertThat(habits).hasSize(databaseSizeBeforeCreate + 1);
        Habit testHabit = habits.get(habits.size() - 1);
        assertThat(testHabit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHabit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(habitRepository.findAll()).hasSize(0);
        // set the field null
        habit.setName(null);

        // Create the Habit, which fails.
        restHabitMockMvc.perform(post("/api/habits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(habit)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Habit> habits = habitRepository.findAll();
        assertThat(habits).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllHabits() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habits
        restHabitMockMvc.perform(get("/api/habits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(habit.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getHabit() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get the habit
        restHabitMockMvc.perform(get("/api/habits/{id}", habit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(habit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHabit() throws Exception {
        // Get the habit
        restHabitMockMvc.perform(get("/api/habits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHabit() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

		int databaseSizeBeforeUpdate = habitRepository.findAll().size();

        // Update the habit
        habit.setName(UPDATED_NAME);
        habit.setDescription(UPDATED_DESCRIPTION);
        restHabitMockMvc.perform(put("/api/habits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(habit)))
                .andExpect(status().isOk());

        // Validate the Habit in the database
        List<Habit> habits = habitRepository.findAll();
        assertThat(habits).hasSize(databaseSizeBeforeUpdate);
        Habit testHabit = habits.get(habits.size() - 1);
        assertThat(testHabit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHabit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteHabit() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

		int databaseSizeBeforeDelete = habitRepository.findAll().size();

        // Get the habit
        restHabitMockMvc.perform(delete("/api/habits/{id}", habit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Habit> habits = habitRepository.findAll();
        assertThat(habits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
