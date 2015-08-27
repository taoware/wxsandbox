package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.SupplierActivity;
import com.irengine.sandbox.repository.SupplierActivityRepository;

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
 * Test class for the SupplierActivityResource REST controller.
 *
 * @see SupplierActivityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SupplierActivityResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final DateTime DEFAULT_BEGIN_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_BEGIN_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_BEGIN_DATE_STR = dateTimeFormatter.print(DEFAULT_BEGIN_DATE);

    private static final DateTime DEFAULT_END_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_END_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.print(DEFAULT_END_DATE);

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final DateTime DEFAULT_CREATED_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_TIME_STR = dateTimeFormatter.print(DEFAULT_CREATED_TIME);

    private static final DateTime DEFAULT_MODIFIED_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_MODIFIED_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_MODIFIED_TIME_STR = dateTimeFormatter.print(DEFAULT_MODIFIED_TIME);

    @Inject
    private SupplierActivityRepository supplierActivityRepository;

    private MockMvc restSupplierActivityMockMvc;

    private SupplierActivity supplierActivity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SupplierActivityResource supplierActivityResource = new SupplierActivityResource();
        ReflectionTestUtils.setField(supplierActivityResource, "supplierActivityRepository", supplierActivityRepository);
        this.restSupplierActivityMockMvc = MockMvcBuilders.standaloneSetup(supplierActivityResource).build();
    }

    @Before
    public void initTest() {
        supplierActivity = new SupplierActivity();
        supplierActivity.setCode(DEFAULT_CODE);
        supplierActivity.setName(DEFAULT_NAME);
        supplierActivity.setDescription(DEFAULT_DESCRIPTION);
        supplierActivity.setBeginDate(DEFAULT_BEGIN_DATE);
        supplierActivity.setEndDate(DEFAULT_END_DATE);
        supplierActivity.setEnabled(DEFAULT_ENABLED);
        supplierActivity.setCreatedTime(DEFAULT_CREATED_TIME);
        supplierActivity.setModifiedTime(DEFAULT_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void createSupplierActivity() throws Exception {
        int databaseSizeBeforeCreate = supplierActivityRepository.findAll().size();

        // Create the SupplierActivity
        restSupplierActivityMockMvc.perform(post("/api/supplierActivitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplierActivity)))
                .andExpect(status().isCreated());

        // Validate the SupplierActivity in the database
        List<SupplierActivity> supplierActivitys = supplierActivityRepository.findAll();
        assertThat(supplierActivitys).hasSize(databaseSizeBeforeCreate + 1);
        SupplierActivity testSupplierActivity = supplierActivitys.get(supplierActivitys.size() - 1);
        assertThat(testSupplierActivity.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSupplierActivity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplierActivity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSupplierActivity.getBeginDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_BEGIN_DATE);
        assertThat(testSupplierActivity.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSupplierActivity.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testSupplierActivity.getCreatedTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testSupplierActivity.getModifiedTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(supplierActivityRepository.findAll()).hasSize(0);
        // set the field null
        supplierActivity.setName(null);

        // Create the SupplierActivity, which fails.
        restSupplierActivityMockMvc.perform(post("/api/supplierActivitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplierActivity)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<SupplierActivity> supplierActivitys = supplierActivityRepository.findAll();
        assertThat(supplierActivitys).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllSupplierActivitys() throws Exception {
        // Initialize the database
        supplierActivityRepository.saveAndFlush(supplierActivity);

        // Get all the supplierActivitys
        restSupplierActivityMockMvc.perform(get("/api/supplierActivitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(supplierActivity.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].beginDate").value(hasItem(DEFAULT_BEGIN_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME_STR)))
                .andExpect(jsonPath("$.[*].modifiedTime").value(hasItem(DEFAULT_MODIFIED_TIME_STR)));
    }

    @Test
    @Transactional
    public void getSupplierActivity() throws Exception {
        // Initialize the database
        supplierActivityRepository.saveAndFlush(supplierActivity);

        // Get the supplierActivity
        restSupplierActivityMockMvc.perform(get("/api/supplierActivitys/{id}", supplierActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(supplierActivity.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.beginDate").value(DEFAULT_BEGIN_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME_STR))
            .andExpect(jsonPath("$.modifiedTime").value(DEFAULT_MODIFIED_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSupplierActivity() throws Exception {
        // Get the supplierActivity
        restSupplierActivityMockMvc.perform(get("/api/supplierActivitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplierActivity() throws Exception {
        // Initialize the database
        supplierActivityRepository.saveAndFlush(supplierActivity);

		int databaseSizeBeforeUpdate = supplierActivityRepository.findAll().size();

        // Update the supplierActivity
        supplierActivity.setCode(UPDATED_CODE);
        supplierActivity.setName(UPDATED_NAME);
        supplierActivity.setDescription(UPDATED_DESCRIPTION);
        supplierActivity.setBeginDate(UPDATED_BEGIN_DATE);
        supplierActivity.setEndDate(UPDATED_END_DATE);
        supplierActivity.setEnabled(UPDATED_ENABLED);
        supplierActivity.setCreatedTime(UPDATED_CREATED_TIME);
        supplierActivity.setModifiedTime(UPDATED_MODIFIED_TIME);
        restSupplierActivityMockMvc.perform(put("/api/supplierActivitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplierActivity)))
                .andExpect(status().isOk());

        // Validate the SupplierActivity in the database
        List<SupplierActivity> supplierActivitys = supplierActivityRepository.findAll();
        assertThat(supplierActivitys).hasSize(databaseSizeBeforeUpdate);
        SupplierActivity testSupplierActivity = supplierActivitys.get(supplierActivitys.size() - 1);
        assertThat(testSupplierActivity.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSupplierActivity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplierActivity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSupplierActivity.getBeginDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_BEGIN_DATE);
        assertThat(testSupplierActivity.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_END_DATE);
        assertThat(testSupplierActivity.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSupplierActivity.getCreatedTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testSupplierActivity.getModifiedTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void deleteSupplierActivity() throws Exception {
        // Initialize the database
        supplierActivityRepository.saveAndFlush(supplierActivity);

		int databaseSizeBeforeDelete = supplierActivityRepository.findAll().size();

        // Get the supplierActivity
        restSupplierActivityMockMvc.perform(delete("/api/supplierActivitys/{id}", supplierActivity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SupplierActivity> supplierActivitys = supplierActivityRepository.findAll();
        assertThat(supplierActivitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
