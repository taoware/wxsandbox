package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.Coupon;
import com.irengine.sandbox.domain.Coupon.STATUS;
import com.irengine.sandbox.repository.CouponRepository;

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
 * Test class for the CouponResource REST controller.
 *
 * @see CouponResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CouponResourceTest {

    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";
    private static final String DEFAULT_PASSWORD = "SAMPLE_TEXT";
    private static final String UPDATED_PASSWORD = "UPDATED_TEXT";

    private static final Long DEFAULT_CATEGORY = 0L;
    private static final Long UPDATED_CATEGORY = 1L;
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";

    @Inject
    private CouponRepository couponRepository;

    private MockMvc restCouponMockMvc;

    private Coupon coupon;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CouponResource couponResource = new CouponResource();
        ReflectionTestUtils.setField(couponResource, "couponRepository", couponRepository);
        this.restCouponMockMvc = MockMvcBuilders.standaloneSetup(couponResource).build();
    }

    @Before
    public void initTest() {
        coupon = new Coupon();
        coupon.setCode(DEFAULT_CODE);
        coupon.setPassword(DEFAULT_PASSWORD);
        coupon.setCategory(DEFAULT_CATEGORY);
        coupon.setStatus(STATUS.Unused);
    }

    @Test
    @Transactional
    public void createCoupon() throws Exception {
        int databaseSizeBeforeCreate = couponRepository.findAll().size();

        // Create the Coupon
        restCouponMockMvc.perform(post("/api/coupons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coupon)))
                .andExpect(status().isCreated());

        // Validate the Coupon in the database
        List<Coupon> coupons = couponRepository.findAll();
        assertThat(coupons).hasSize(databaseSizeBeforeCreate + 1);
        Coupon testCoupon = coupons.get(coupons.size() - 1);
        assertThat(testCoupon.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCoupon.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testCoupon.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testCoupon.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(couponRepository.findAll()).hasSize(0);
        // set the field null
        coupon.setCode(null);

        // Create the Coupon, which fails.
        restCouponMockMvc.perform(post("/api/coupons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coupon)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Coupon> coupons = couponRepository.findAll();
        assertThat(coupons).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllCoupons() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

        // Get all the coupons
        restCouponMockMvc.perform(get("/api/coupons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coupon.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getCoupon() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

        // Get the coupon
        restCouponMockMvc.perform(get("/api/coupons/{id}", coupon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coupon.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoupon() throws Exception {
        // Get the coupon
        restCouponMockMvc.perform(get("/api/coupons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoupon() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

		int databaseSizeBeforeUpdate = couponRepository.findAll().size();

        // Update the coupon
        coupon.setCode(UPDATED_CODE);
        coupon.setPassword(UPDATED_PASSWORD);
        coupon.setCategory(UPDATED_CATEGORY);
        coupon.setStatus(STATUS.Unused);
        restCouponMockMvc.perform(put("/api/coupons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coupon)))
                .andExpect(status().isOk());

        // Validate the Coupon in the database
        List<Coupon> coupons = couponRepository.findAll();
        assertThat(coupons).hasSize(databaseSizeBeforeUpdate);
        Coupon testCoupon = coupons.get(coupons.size() - 1);
        assertThat(testCoupon.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCoupon.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testCoupon.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCoupon.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteCoupon() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

		int databaseSizeBeforeDelete = couponRepository.findAll().size();

        // Get the coupon
        restCouponMockMvc.perform(delete("/api/coupons/{id}", coupon.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Coupon> coupons = couponRepository.findAll();
        assertThat(coupons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
