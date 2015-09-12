package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.NCoupon;
import com.irengine.sandbox.domain.NCoupon.COUPONSTATUS;
import com.irengine.sandbox.repository.NCouponRepository;

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
 * Test class for the NCouponResource REST controller.
 *
 * @see NCouponResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NCouponResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CREATED_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_TIME_STR = dateTimeFormatter.print(DEFAULT_CREATED_TIME);

    private static final DateTime DEFAULT_MODIFED_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_MODIFED_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_MODIFED_TIME_STR = dateTimeFormatter.print(DEFAULT_MODIFED_TIME);

    @Inject
    private NCouponRepository nCouponRepository;

    private MockMvc restNCouponMockMvc;

    private NCoupon nCoupon;

    @Test
    @Transactional
    public void testFindAllByCouponBatchAndStatus(){
    	List<NCoupon> nCoupons=nCouponRepository.findAllByCouponBatchAndStatus(1L, COUPONSTATUS.UNUSED);
    	//System.out.println(nCoupons.get(0));
    	System.out.println(nCoupons.size());
    }
    
    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NCouponResource nCouponResource = new NCouponResource();
        ReflectionTestUtils.setField(nCouponResource, "nCouponRepository", nCouponRepository);
        this.restNCouponMockMvc = MockMvcBuilders.standaloneSetup(nCouponResource).build();
    }

    @Before
    public void initTest() {
        nCoupon = new NCoupon();
        nCoupon.setCode(DEFAULT_CODE);
        nCoupon.setStatus(COUPONSTATUS.USED);
        nCoupon.setCreatedTime(DEFAULT_CREATED_TIME);
        nCoupon.setModifedTime(DEFAULT_MODIFED_TIME);
    }

    @Test
    @Transactional
    public void createNCoupon() throws Exception {
        int databaseSizeBeforeCreate = nCouponRepository.findAll().size();

        // Create the NCoupon
        restNCouponMockMvc.perform(post("/api/nCoupons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nCoupon)))
                .andExpect(status().isCreated());

        // Validate the NCoupon in the database
        List<NCoupon> nCoupons = nCouponRepository.findAll();
        assertThat(nCoupons).hasSize(databaseSizeBeforeCreate + 1);
        NCoupon testNCoupon = nCoupons.get(nCoupons.size() - 1);
        assertThat(testNCoupon.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testNCoupon.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNCoupon.getCreatedTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testNCoupon.getModifedTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_MODIFED_TIME);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(nCouponRepository.findAll()).hasSize(0);
        // set the field null
        nCoupon.setStatus(null);

        // Create the NCoupon, which fails.
        restNCouponMockMvc.perform(post("/api/nCoupons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nCoupon)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<NCoupon> nCoupons = nCouponRepository.findAll();
        assertThat(nCoupons).hasSize(0);
    }

    @Test
    @Transactional
    public void checkCreatedTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(nCouponRepository.findAll()).hasSize(0);
        // set the field null
        nCoupon.setCreatedTime(null);

        // Create the NCoupon, which fails.
        restNCouponMockMvc.perform(post("/api/nCoupons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nCoupon)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<NCoupon> nCoupons = nCouponRepository.findAll();
        assertThat(nCoupons).hasSize(0);
    }

    @Test
    @Transactional
    public void checkModifedTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(nCouponRepository.findAll()).hasSize(0);
        // set the field null
        nCoupon.setModifedTime(null);

        // Create the NCoupon, which fails.
        restNCouponMockMvc.perform(post("/api/nCoupons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nCoupon)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<NCoupon> nCoupons = nCouponRepository.findAll();
        assertThat(nCoupons).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllNCoupons() throws Exception {
        // Initialize the database
        nCouponRepository.saveAndFlush(nCoupon);

        // Get all the nCoupons
        restNCouponMockMvc.perform(get("/api/nCoupons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(nCoupon.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME_STR)))
                .andExpect(jsonPath("$.[*].modifedTime").value(hasItem(DEFAULT_MODIFED_TIME_STR)));
    }

    @Test
    @Transactional
    public void getNCoupon() throws Exception {
        // Initialize the database
        nCouponRepository.saveAndFlush(nCoupon);

        // Get the nCoupon
        restNCouponMockMvc.perform(get("/api/nCoupons/{id}", nCoupon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(nCoupon.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME_STR))
            .andExpect(jsonPath("$.modifedTime").value(DEFAULT_MODIFED_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingNCoupon() throws Exception {
        // Get the nCoupon
        restNCouponMockMvc.perform(get("/api/nCoupons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNCoupon() throws Exception {
        // Initialize the database
        nCouponRepository.saveAndFlush(nCoupon);

		int databaseSizeBeforeUpdate = nCouponRepository.findAll().size();

        // Update the nCoupon
        nCoupon.setCode(UPDATED_CODE);
        nCoupon.setStatus(COUPONSTATUS.USED);
        nCoupon.setCreatedTime(UPDATED_CREATED_TIME);
        nCoupon.setModifedTime(UPDATED_MODIFED_TIME);
        restNCouponMockMvc.perform(put("/api/nCoupons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nCoupon)))
                .andExpect(status().isOk());

        // Validate the NCoupon in the database
        List<NCoupon> nCoupons = nCouponRepository.findAll();
        assertThat(nCoupons).hasSize(databaseSizeBeforeUpdate);
        NCoupon testNCoupon = nCoupons.get(nCoupons.size() - 1);
        assertThat(testNCoupon.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testNCoupon.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNCoupon.getCreatedTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testNCoupon.getModifedTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_MODIFED_TIME);
    }

    @Test
    @Transactional
    public void deleteNCoupon() throws Exception {
        // Initialize the database
        nCouponRepository.saveAndFlush(nCoupon);

		int databaseSizeBeforeDelete = nCouponRepository.findAll().size();

        // Get the nCoupon
        restNCouponMockMvc.perform(delete("/api/nCoupons/{id}", nCoupon.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NCoupon> nCoupons = nCouponRepository.findAll();
        assertThat(nCoupons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
