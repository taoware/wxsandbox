package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.CouponBatch;
import com.irengine.sandbox.repository.CouponBatchRepository;

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
 * Test class for the CouponBatchResource REST controller.
 *
 * @see CouponBatchResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CouponBatchResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";

    private static final Integer DEFAULT_SIZE = 0;
    private static final Integer UPDATED_SIZE = 1;

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final DateTime DEFAULT_BEGIN_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_BEGIN_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_BEGIN_DATE_STR = dateTimeFormatter.print(DEFAULT_BEGIN_DATE);

    private static final DateTime DEFAULT_END_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_END_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.print(DEFAULT_END_DATE);

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Boolean DEFAULT_IS_GENERATED = false;
    private static final Boolean UPDATED_IS_GENERATED = true;

    @Inject
    private CouponBatchRepository couponBatchRepository;

    private MockMvc restCouponBatchMockMvc;

    private CouponBatch couponBatch;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CouponBatchResource couponBatchResource = new CouponBatchResource();
        ReflectionTestUtils.setField(couponBatchResource, "couponBatchRepository", couponBatchRepository);
        this.restCouponBatchMockMvc = MockMvcBuilders.standaloneSetup(couponBatchResource).build();
    }

    @Before
    public void initTest() {
        couponBatch = new CouponBatch();
        couponBatch.setCode(DEFAULT_CODE);
        couponBatch.setSize(DEFAULT_SIZE);
        couponBatch.setQuantity(DEFAULT_QUANTITY);
        couponBatch.setBeginDate(DEFAULT_BEGIN_DATE);
        couponBatch.setEndDate(DEFAULT_END_DATE);
        couponBatch.setEnabled(DEFAULT_ENABLED);
        couponBatch.setIsGenerated(DEFAULT_IS_GENERATED);
    }

    @Test
    @Transactional
    public void createCouponBatch() throws Exception {
        int databaseSizeBeforeCreate = couponBatchRepository.findAll().size();

        // Create the CouponBatch
        restCouponBatchMockMvc.perform(post("/api/couponBatchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(couponBatch)))
                .andExpect(status().isCreated());

        // Validate the CouponBatch in the database
        List<CouponBatch> couponBatchs = couponBatchRepository.findAll();
        assertThat(couponBatchs).hasSize(databaseSizeBeforeCreate + 1);
        CouponBatch testCouponBatch = couponBatchs.get(couponBatchs.size() - 1);
        assertThat(testCouponBatch.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCouponBatch.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testCouponBatch.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testCouponBatch.getBeginDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_BEGIN_DATE);
        assertThat(testCouponBatch.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCouponBatch.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testCouponBatch.getIsGenerated()).isEqualTo(DEFAULT_IS_GENERATED);
    }

    @Test
    @Transactional
    public void checkSizeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(couponBatchRepository.findAll()).hasSize(0);
        // set the field null
        couponBatch.setSize(null);

        // Create the CouponBatch, which fails.
        restCouponBatchMockMvc.perform(post("/api/couponBatchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(couponBatch)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<CouponBatch> couponBatchs = couponBatchRepository.findAll();
        assertThat(couponBatchs).hasSize(0);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(couponBatchRepository.findAll()).hasSize(0);
        // set the field null
        couponBatch.setQuantity(null);

        // Create the CouponBatch, which fails.
        restCouponBatchMockMvc.perform(post("/api/couponBatchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(couponBatch)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<CouponBatch> couponBatchs = couponBatchRepository.findAll();
        assertThat(couponBatchs).hasSize(0);
    }

    @Test
    @Transactional
    public void checkBeginDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(couponBatchRepository.findAll()).hasSize(0);
        // set the field null
        couponBatch.setBeginDate(null);

        // Create the CouponBatch, which fails.
        restCouponBatchMockMvc.perform(post("/api/couponBatchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(couponBatch)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<CouponBatch> couponBatchs = couponBatchRepository.findAll();
        assertThat(couponBatchs).hasSize(0);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(couponBatchRepository.findAll()).hasSize(0);
        // set the field null
        couponBatch.setEndDate(null);

        // Create the CouponBatch, which fails.
        restCouponBatchMockMvc.perform(post("/api/couponBatchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(couponBatch)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<CouponBatch> couponBatchs = couponBatchRepository.findAll();
        assertThat(couponBatchs).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllCouponBatchs() throws Exception {
        // Initialize the database
        couponBatchRepository.saveAndFlush(couponBatch);

        // Get all the couponBatchs
        restCouponBatchMockMvc.perform(get("/api/couponBatchs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(couponBatch.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].beginDate").value(hasItem(DEFAULT_BEGIN_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].isGenerated").value(hasItem(DEFAULT_IS_GENERATED.booleanValue())));
    }

    @Test
    @Transactional
    public void getCouponBatch() throws Exception {
        // Initialize the database
        couponBatchRepository.saveAndFlush(couponBatch);

        // Get the couponBatch
        restCouponBatchMockMvc.perform(get("/api/couponBatchs/{id}", couponBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(couponBatch.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.beginDate").value(DEFAULT_BEGIN_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.isGenerated").value(DEFAULT_IS_GENERATED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCouponBatch() throws Exception {
        // Get the couponBatch
        restCouponBatchMockMvc.perform(get("/api/couponBatchs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCouponBatch() throws Exception {
        // Initialize the database
        couponBatchRepository.saveAndFlush(couponBatch);

		int databaseSizeBeforeUpdate = couponBatchRepository.findAll().size();

        // Update the couponBatch
        couponBatch.setCode(UPDATED_CODE);
        couponBatch.setSize(UPDATED_SIZE);
        couponBatch.setQuantity(UPDATED_QUANTITY);
        couponBatch.setBeginDate(UPDATED_BEGIN_DATE);
        couponBatch.setEndDate(UPDATED_END_DATE);
        couponBatch.setEnabled(UPDATED_ENABLED);
        couponBatch.setIsGenerated(UPDATED_IS_GENERATED);
        restCouponBatchMockMvc.perform(put("/api/couponBatchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(couponBatch)))
                .andExpect(status().isOk());

        // Validate the CouponBatch in the database
        List<CouponBatch> couponBatchs = couponBatchRepository.findAll();
        assertThat(couponBatchs).hasSize(databaseSizeBeforeUpdate);
        CouponBatch testCouponBatch = couponBatchs.get(couponBatchs.size() - 1);
        assertThat(testCouponBatch.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCouponBatch.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testCouponBatch.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCouponBatch.getBeginDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_BEGIN_DATE);
        assertThat(testCouponBatch.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_END_DATE);
        assertThat(testCouponBatch.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testCouponBatch.getIsGenerated()).isEqualTo(UPDATED_IS_GENERATED);
    }

    @Test
    @Transactional
    public void deleteCouponBatch() throws Exception {
        // Initialize the database
        couponBatchRepository.saveAndFlush(couponBatch);

		int databaseSizeBeforeDelete = couponBatchRepository.findAll().size();

        // Get the couponBatch
        restCouponBatchMockMvc.perform(delete("/api/couponBatchs/{id}", couponBatch.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CouponBatch> couponBatchs = couponBatchRepository.findAll();
        assertThat(couponBatchs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
