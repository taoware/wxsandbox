package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.UserExpandInfo;
import com.irengine.sandbox.repository.UserExpandInfoRepository;

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
 * Test class for the UserExpandInfoResource REST controller.
 *
 * @see UserExpandInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserExpandInfoResourceTest {

    private static final String DEFAULT_KEY = "SAMPLE_TEXT";
    private static final String UPDATED_KEY = "UPDATED_TEXT";
    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private UserExpandInfoRepository userExpandInfoRepository;

    private MockMvc restUserExpandInfoMockMvc;

    private UserExpandInfo userExpandInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserExpandInfoResource userExpandInfoResource = new UserExpandInfoResource();
        ReflectionTestUtils.setField(userExpandInfoResource, "userExpandInfoRepository", userExpandInfoRepository);
        this.restUserExpandInfoMockMvc = MockMvcBuilders.standaloneSetup(userExpandInfoResource).build();
    }

    @Before
    public void initTest() {
        userExpandInfo = new UserExpandInfo();
        userExpandInfo.setKey(DEFAULT_KEY);
        userExpandInfo.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createUserExpandInfo() throws Exception {
        int databaseSizeBeforeCreate = userExpandInfoRepository.findAll().size();

        // Create the UserExpandInfo
        restUserExpandInfoMockMvc.perform(post("/api/userExpandInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userExpandInfo)))
                .andExpect(status().isCreated());

        // Validate the UserExpandInfo in the database
        List<UserExpandInfo> userExpandInfos = userExpandInfoRepository.findAll();
        assertThat(userExpandInfos).hasSize(databaseSizeBeforeCreate + 1);
        UserExpandInfo testUserExpandInfo = userExpandInfos.get(userExpandInfos.size() - 1);
        assertThat(testUserExpandInfo.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testUserExpandInfo.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(userExpandInfoRepository.findAll()).hasSize(0);
        // set the field null
        userExpandInfo.setKey(null);

        // Create the UserExpandInfo, which fails.
        restUserExpandInfoMockMvc.perform(post("/api/userExpandInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userExpandInfo)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<UserExpandInfo> userExpandInfos = userExpandInfoRepository.findAll();
        assertThat(userExpandInfos).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllUserExpandInfos() throws Exception {
        // Initialize the database
        userExpandInfoRepository.saveAndFlush(userExpandInfo);

        // Get all the userExpandInfos
        restUserExpandInfoMockMvc.perform(get("/api/userExpandInfos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userExpandInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getUserExpandInfo() throws Exception {
        // Initialize the database
        userExpandInfoRepository.saveAndFlush(userExpandInfo);

        // Get the userExpandInfo
        restUserExpandInfoMockMvc.perform(get("/api/userExpandInfos/{id}", userExpandInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userExpandInfo.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserExpandInfo() throws Exception {
        // Get the userExpandInfo
        restUserExpandInfoMockMvc.perform(get("/api/userExpandInfos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserExpandInfo() throws Exception {
        // Initialize the database
        userExpandInfoRepository.saveAndFlush(userExpandInfo);

		int databaseSizeBeforeUpdate = userExpandInfoRepository.findAll().size();

        // Update the userExpandInfo
        userExpandInfo.setKey(UPDATED_KEY);
        userExpandInfo.setValue(UPDATED_VALUE);
        restUserExpandInfoMockMvc.perform(put("/api/userExpandInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userExpandInfo)))
                .andExpect(status().isOk());

        // Validate the UserExpandInfo in the database
        List<UserExpandInfo> userExpandInfos = userExpandInfoRepository.findAll();
        assertThat(userExpandInfos).hasSize(databaseSizeBeforeUpdate);
        UserExpandInfo testUserExpandInfo = userExpandInfos.get(userExpandInfos.size() - 1);
        assertThat(testUserExpandInfo.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testUserExpandInfo.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteUserExpandInfo() throws Exception {
        // Initialize the database
        userExpandInfoRepository.saveAndFlush(userExpandInfo);

		int databaseSizeBeforeDelete = userExpandInfoRepository.findAll().size();

        // Get the userExpandInfo
        restUserExpandInfoMockMvc.perform(delete("/api/userExpandInfos/{id}", userExpandInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserExpandInfo> userExpandInfos = userExpandInfoRepository.findAll();
        assertThat(userExpandInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
