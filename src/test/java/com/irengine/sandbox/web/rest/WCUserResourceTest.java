package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.WCUser;
import com.irengine.sandbox.repository.WCUserRepository;

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
 * Test class for the WCUserResource REST controller.
 *
 * @see WCUserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WCUserResourceTest {

    private static final String DEFAULT_OPEN_ID = "SAMPLE_TEXT";
    private static final String UPDATED_OPEN_ID = "UPDATED_TEXT";
    private static final String DEFAULT_NICKNAME = "SAMPLE_TEXT";
    private static final String UPDATED_NICKNAME = "UPDATED_TEXT";
    private static final String DEFAULT_SEX = "SAMPLE_TEXT";
    private static final String UPDATED_SEX = "UPDATED_TEXT";
    private static final String DEFAULT_CITY = "SAMPLE_TEXT";
    private static final String UPDATED_CITY = "UPDATED_TEXT";
    private static final String DEFAULT_PROVINCE = "SAMPLE_TEXT";
    private static final String UPDATED_PROVINCE = "UPDATED_TEXT";
    private static final String DEFAULT_COUNTRY = "SAMPLE_TEXT";
    private static final String UPDATED_COUNTRY = "UPDATED_TEXT";
    private static final String DEFAULT_UNION_ID = "SAMPLE_TEXT";
    private static final String UPDATED_UNION_ID = "UPDATED_TEXT";
    private static final String DEFAULT_MOBILE = "SAMPLE_TEXT";
    private static final String UPDATED_MOBILE = "UPDATED_TEXT";

    @Inject
    private WCUserRepository wCUserRepository;

    private MockMvc restWCUserMockMvc;

    private WCUser wCUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WCUserResource wCUserResource = new WCUserResource();
        ReflectionTestUtils.setField(wCUserResource, "wCUserRepository", wCUserRepository);
        this.restWCUserMockMvc = MockMvcBuilders.standaloneSetup(wCUserResource).build();
    }

    @Before
    public void initTest() {
        wCUser = new WCUser();
        wCUser.setOpenId(DEFAULT_OPEN_ID);
        wCUser.setNickname(DEFAULT_NICKNAME);
        wCUser.setSex(DEFAULT_SEX);
        wCUser.setCity(DEFAULT_CITY);
        wCUser.setProvince(DEFAULT_PROVINCE);
        wCUser.setCountry(DEFAULT_COUNTRY);
        wCUser.setUnionId(DEFAULT_UNION_ID);
        wCUser.setMobile(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    public void createWCUser() throws Exception {
        int databaseSizeBeforeCreate = wCUserRepository.findAll().size();

        // Create the WCUser
        restWCUserMockMvc.perform(post("/api/wCUsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wCUser)))
                .andExpect(status().isCreated());

        // Validate the WCUser in the database
        List<WCUser> wCUsers = wCUserRepository.findAll();
        assertThat(wCUsers).hasSize(databaseSizeBeforeCreate + 1);
        WCUser testWCUser = wCUsers.get(wCUsers.size() - 1);
        assertThat(testWCUser.getOpenId()).isEqualTo(DEFAULT_OPEN_ID);
        assertThat(testWCUser.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testWCUser.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testWCUser.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testWCUser.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testWCUser.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testWCUser.getUnionId()).isEqualTo(DEFAULT_UNION_ID);
        assertThat(testWCUser.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    public void getAllWCUsers() throws Exception {
        // Initialize the database
        wCUserRepository.saveAndFlush(wCUser);

        // Get all the wCUsers
        restWCUserMockMvc.perform(get("/api/wCUsers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(wCUser.getId().intValue())))
                .andExpect(jsonPath("$.[*].openId").value(hasItem(DEFAULT_OPEN_ID.toString())))
                .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
                .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].unionId").value(hasItem(DEFAULT_UNION_ID.toString())))
                .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())));
    }

    @Test
    @Transactional
    public void getWCUser() throws Exception {
        // Initialize the database
        wCUserRepository.saveAndFlush(wCUser);

        // Get the wCUser
        restWCUserMockMvc.perform(get("/api/wCUsers/{id}", wCUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(wCUser.getId().intValue()))
            .andExpect(jsonPath("$.openId").value(DEFAULT_OPEN_ID.toString()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.unionId").value(DEFAULT_UNION_ID.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWCUser() throws Exception {
        // Get the wCUser
        restWCUserMockMvc.perform(get("/api/wCUsers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWCUser() throws Exception {
        // Initialize the database
        wCUserRepository.saveAndFlush(wCUser);

		int databaseSizeBeforeUpdate = wCUserRepository.findAll().size();

        // Update the wCUser
        wCUser.setOpenId(UPDATED_OPEN_ID);
        wCUser.setNickname(UPDATED_NICKNAME);
        wCUser.setSex(UPDATED_SEX);
        wCUser.setCity(UPDATED_CITY);
        wCUser.setProvince(UPDATED_PROVINCE);
        wCUser.setCountry(UPDATED_COUNTRY);
        wCUser.setUnionId(UPDATED_UNION_ID);
        wCUser.setMobile(UPDATED_MOBILE);
        restWCUserMockMvc.perform(put("/api/wCUsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wCUser)))
                .andExpect(status().isOk());

        // Validate the WCUser in the database
        List<WCUser> wCUsers = wCUserRepository.findAll();
        assertThat(wCUsers).hasSize(databaseSizeBeforeUpdate);
        WCUser testWCUser = wCUsers.get(wCUsers.size() - 1);
        assertThat(testWCUser.getOpenId()).isEqualTo(UPDATED_OPEN_ID);
        assertThat(testWCUser.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testWCUser.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testWCUser.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testWCUser.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testWCUser.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testWCUser.getUnionId()).isEqualTo(UPDATED_UNION_ID);
        assertThat(testWCUser.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void deleteWCUser() throws Exception {
        // Initialize the database
        wCUserRepository.saveAndFlush(wCUser);

		int databaseSizeBeforeDelete = wCUserRepository.findAll().size();

        // Get the wCUser
        restWCUserMockMvc.perform(delete("/api/wCUsers/{id}", wCUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WCUser> wCUsers = wCUserRepository.findAll();
        assertThat(wCUsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
