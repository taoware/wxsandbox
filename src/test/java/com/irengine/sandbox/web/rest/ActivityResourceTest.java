package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.Activity;
import com.irengine.sandbox.repository.ActivityRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ActivityResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final Boolean DEFAULT_DISABLE = false;
    private static final Boolean UPDATED_DISABLE = true;
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_INDEX_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_INDEX_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_FOLDER_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_FOLDER_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_URL = "SAMPLE_TEXT";
    private static final String UPDATED_URL = "UPDATED_TEXT";

    private static final DateTime DEFAULT_START_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_START_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.print(DEFAULT_START_DATE);

    private static final DateTime DEFAULT_END_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_END_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.print(DEFAULT_END_DATE);

    @Inject
    private ActivityRepository activityRepository;

    private MockMvc restActivityMockMvc;

    private Activity activity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActivityResource activityResource = new ActivityResource();
        ReflectionTestUtils.setField(activityResource, "activityRepository", activityRepository);
        this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource).build();
    }

    @Before
    public void initTest() {
        activity = new Activity();
        activity.setDisable(DEFAULT_DISABLE);
        activity.setName(DEFAULT_NAME);
        activity.setIndexName(DEFAULT_INDEX_NAME);
        activity.setFolderName(DEFAULT_FOLDER_NAME);
        activity.setType(DEFAULT_TYPE);
        activity.setDescription(DEFAULT_DESCRIPTION);
        activity.setUrl(DEFAULT_URL);
        activity.setStartDate(DEFAULT_START_DATE);
        activity.setEndDate(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createActivity() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity
        restActivityMockMvc.perform(post("/api/activitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activity)))
                .andExpect(status().isCreated());

        // Validate the Activity in the database
        List<Activity> activitys = activityRepository.findAll();
        assertThat(activitys).hasSize(databaseSizeBeforeCreate + 1);
        Activity testActivity = activitys.get(activitys.size() - 1);
        assertThat(testActivity.getDisable()).isEqualTo(DEFAULT_DISABLE);
        assertThat(testActivity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActivity.getIndexName()).isEqualTo(DEFAULT_INDEX_NAME);
        assertThat(testActivity.getFolderName()).isEqualTo(DEFAULT_FOLDER_NAME);
        assertThat(testActivity.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testActivity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testActivity.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testActivity.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_START_DATE);
        assertThat(testActivity.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(activityRepository.findAll()).hasSize(0);
        // set the field null
        activity.setName(null);

        // Create the Activity, which fails.
        restActivityMockMvc.perform(post("/api/activitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activity)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Activity> activitys = activityRepository.findAll();
        assertThat(activitys).hasSize(0);
    }

    @Test
    @Transactional
    public void checkIndexNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(activityRepository.findAll()).hasSize(0);
        // set the field null
        activity.setIndexName(null);

        // Create the Activity, which fails.
        restActivityMockMvc.perform(post("/api/activitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activity)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Activity> activitys = activityRepository.findAll();
        assertThat(activitys).hasSize(0);
    }

    @Test
    @Transactional
    public void checkFolderNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(activityRepository.findAll()).hasSize(0);
        // set the field null
        activity.setFolderName(null);

        // Create the Activity, which fails.
        restActivityMockMvc.perform(post("/api/activitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activity)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Activity> activitys = activityRepository.findAll();
        assertThat(activitys).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllActivitys() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activitys
        restActivityMockMvc.perform(get("/api/activitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
                .andExpect(jsonPath("$.[*].disable").value(hasItem(DEFAULT_DISABLE.booleanValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].indexName").value(hasItem(DEFAULT_INDEX_NAME.toString())))
                .andExpect(jsonPath("$.[*].folderName").value(hasItem(DEFAULT_FOLDER_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }

    @Test
    @Transactional
    public void getActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc.perform(get("/api/activitys/{id}", activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.disable").value(DEFAULT_DISABLE.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.indexName").value(DEFAULT_INDEX_NAME.toString()))
            .andExpect(jsonPath("$.folderName").value(DEFAULT_FOLDER_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get("/api/activitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

		int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Update the activity
        activity.setDisable(UPDATED_DISABLE);
        activity.setName(UPDATED_NAME);
        activity.setIndexName(UPDATED_INDEX_NAME);
        activity.setFolderName(UPDATED_FOLDER_NAME);
        activity.setType(UPDATED_TYPE);
        activity.setDescription(UPDATED_DESCRIPTION);
        activity.setUrl(UPDATED_URL);
        activity.setStartDate(UPDATED_START_DATE);
        activity.setEndDate(UPDATED_END_DATE);
        restActivityMockMvc.perform(put("/api/activitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activity)))
                .andExpect(status().isOk());

        // Validate the Activity in the database
        List<Activity> activitys = activityRepository.findAll();
        assertThat(activitys).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activitys.get(activitys.size() - 1);
        assertThat(testActivity.getDisable()).isEqualTo(UPDATED_DISABLE);
        assertThat(testActivity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActivity.getIndexName()).isEqualTo(UPDATED_INDEX_NAME);
        assertThat(testActivity.getFolderName()).isEqualTo(UPDATED_FOLDER_NAME);
        assertThat(testActivity.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testActivity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testActivity.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testActivity.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_START_DATE);
        assertThat(testActivity.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

		int databaseSizeBeforeDelete = activityRepository.findAll().size();

        // Get the activity
        restActivityMockMvc.perform(delete("/api/activitys/{id}", activity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Activity> activitys = activityRepository.findAll();
        assertThat(activitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
