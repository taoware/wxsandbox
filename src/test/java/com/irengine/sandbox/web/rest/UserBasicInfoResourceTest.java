package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.UserBasicInfo;
import com.irengine.sandbox.repository.UserBasicInfoRepository;

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
 * Test class for the UserBasicInfoResource REST controller.
 *
 * @see UserBasicInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserBasicInfoResourceTest {

    private static final String DEFAULT_OPEN_ID = "SAMPLE_TEXT";
    private static final String UPDATED_OPEN_ID = "UPDATED_TEXT";
    private static final String DEFAULT_MOBILE = "SAMPLE_TEXT";
    private static final String UPDATED_MOBILE = "UPDATED_TEXT";
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";

    @Inject
    private UserBasicInfoRepository userBasicInfoRepository;

    private MockMvc restUserBasicInfoMockMvc;

    private UserBasicInfo userBasicInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserBasicInfoResource userBasicInfoResource = new UserBasicInfoResource();
        ReflectionTestUtils.setField(userBasicInfoResource, "userBasicInfoRepository", userBasicInfoRepository);
        this.restUserBasicInfoMockMvc = MockMvcBuilders.standaloneSetup(userBasicInfoResource).build();
    }

    @Before
    public void initTest() {
        userBasicInfo = new UserBasicInfo();
        userBasicInfo.setOpenId(DEFAULT_OPEN_ID);
        userBasicInfo.setMobile(DEFAULT_MOBILE);
        userBasicInfo.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createUserBasicInfo() throws Exception {
        int databaseSizeBeforeCreate = userBasicInfoRepository.findAll().size();

        // Create the UserBasicInfo
        restUserBasicInfoMockMvc.perform(post("/api/userBasicInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userBasicInfo)))
                .andExpect(status().isCreated());

        // Validate the UserBasicInfo in the database
        List<UserBasicInfo> userBasicInfos = userBasicInfoRepository.findAll();
        assertThat(userBasicInfos).hasSize(databaseSizeBeforeCreate + 1);
        UserBasicInfo testUserBasicInfo = userBasicInfos.get(userBasicInfos.size() - 1);
        assertThat(testUserBasicInfo.getOpenId()).isEqualTo(DEFAULT_OPEN_ID);
        assertThat(testUserBasicInfo.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testUserBasicInfo.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkOpenIdIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(userBasicInfoRepository.findAll()).hasSize(0);
        // set the field null
        userBasicInfo.setOpenId(null);

        // Create the UserBasicInfo, which fails.
        restUserBasicInfoMockMvc.perform(post("/api/userBasicInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userBasicInfo)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<UserBasicInfo> userBasicInfos = userBasicInfoRepository.findAll();
        assertThat(userBasicInfos).hasSize(0);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(userBasicInfoRepository.findAll()).hasSize(0);
        // set the field null
        userBasicInfo.setStatus(null);

        // Create the UserBasicInfo, which fails.
        restUserBasicInfoMockMvc.perform(post("/api/userBasicInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userBasicInfo)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<UserBasicInfo> userBasicInfos = userBasicInfoRepository.findAll();
        assertThat(userBasicInfos).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllUserBasicInfos() throws Exception {
        // Initialize the database
        userBasicInfoRepository.saveAndFlush(userBasicInfo);

        // Get all the userBasicInfos
        restUserBasicInfoMockMvc.perform(get("/api/userBasicInfos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userBasicInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].openId").value(hasItem(DEFAULT_OPEN_ID.toString())))
                .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getUserBasicInfo() throws Exception {
        // Initialize the database
        userBasicInfoRepository.saveAndFlush(userBasicInfo);

        // Get the userBasicInfo
        restUserBasicInfoMockMvc.perform(get("/api/userBasicInfos/{id}", userBasicInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userBasicInfo.getId().intValue()))
            .andExpect(jsonPath("$.openId").value(DEFAULT_OPEN_ID.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserBasicInfo() throws Exception {
        // Get the userBasicInfo
        restUserBasicInfoMockMvc.perform(get("/api/userBasicInfos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserBasicInfo() throws Exception {
        // Initialize the database
        userBasicInfoRepository.saveAndFlush(userBasicInfo);

		int databaseSizeBeforeUpdate = userBasicInfoRepository.findAll().size();

        // Update the userBasicInfo
        userBasicInfo.setOpenId(UPDATED_OPEN_ID);
        userBasicInfo.setMobile(UPDATED_MOBILE);
        userBasicInfo.setStatus(UPDATED_STATUS);
        restUserBasicInfoMockMvc.perform(put("/api/userBasicInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userBasicInfo)))
                .andExpect(status().isOk());

        // Validate the UserBasicInfo in the database
        List<UserBasicInfo> userBasicInfos = userBasicInfoRepository.findAll();
        assertThat(userBasicInfos).hasSize(databaseSizeBeforeUpdate);
        UserBasicInfo testUserBasicInfo = userBasicInfos.get(userBasicInfos.size() - 1);
        assertThat(testUserBasicInfo.getOpenId()).isEqualTo(UPDATED_OPEN_ID);
        assertThat(testUserBasicInfo.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testUserBasicInfo.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteUserBasicInfo() throws Exception {
        // Initialize the database
        userBasicInfoRepository.saveAndFlush(userBasicInfo);

		int databaseSizeBeforeDelete = userBasicInfoRepository.findAll().size();

        // Get the userBasicInfo
        restUserBasicInfoMockMvc.perform(delete("/api/userBasicInfos/{id}", userBasicInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserBasicInfo> userBasicInfos = userBasicInfoRepository.findAll();
        assertThat(userBasicInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
