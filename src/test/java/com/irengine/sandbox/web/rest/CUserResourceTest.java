package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.CUser;
import com.irengine.sandbox.repository.CUserRepository;

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
 * Test class for the CUserResource REST controller.
 *
 * @see CUserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CUserResourceTest {

    private static final String DEFAULT_MOBILE = "SAMPLE_TEXT";
    private static final String UPDATED_MOBILE = "UPDATED_TEXT";
    private static final String DEFAULT_OPENID = "SAMPLE_TEXT";
    private static final String UPDATED_OPENID = "UPDATED_TEXT";

    @Inject
    private CUserRepository cUserRepository;

    private MockMvc restCUserMockMvc;

    private CUser cUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CUserResource cUserResource = new CUserResource();
        ReflectionTestUtils.setField(cUserResource, "cUserRepository", cUserRepository);
        this.restCUserMockMvc = MockMvcBuilders.standaloneSetup(cUserResource).build();
    }

    @Before
    public void initTest() {
        cUser = new CUser();
        cUser.setMobile(DEFAULT_MOBILE);
        cUser.setOpenId(DEFAULT_OPENID);
    }

    @Test
    @Transactional
    public void createCUser() throws Exception {
        int databaseSizeBeforeCreate = cUserRepository.findAll().size();

        // Create the CUser
        restCUserMockMvc.perform(post("/api/cUsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cUser)))
                .andExpect(status().isCreated());

        // Validate the CUser in the database
        List<CUser> cUsers = cUserRepository.findAll();
        assertThat(cUsers).hasSize(databaseSizeBeforeCreate + 1);
        CUser testCUser = cUsers.get(cUsers.size() - 1);
        assertThat(testCUser.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testCUser.getOpenId()).isEqualTo(DEFAULT_OPENID);
    }

    @Test
    @Transactional
    public void getAllCUsers() throws Exception {
        // Initialize the database
        cUserRepository.saveAndFlush(cUser);

        // Get all the cUsers
        restCUserMockMvc.perform(get("/api/cUsers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cUser.getId().intValue())))
                .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
                .andExpect(jsonPath("$.[*].openId").value(hasItem(DEFAULT_OPENID.toString())));
    }

    @Test
    @Transactional
    public void getCUser() throws Exception {
        // Initialize the database
        cUserRepository.saveAndFlush(cUser);

        // Get the cUser
        restCUserMockMvc.perform(get("/api/cUsers/{id}", cUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cUser.getId().intValue()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.openId").value(DEFAULT_OPENID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCUser() throws Exception {
        // Get the cUser
        restCUserMockMvc.perform(get("/api/cUsers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCUser() throws Exception {
        // Initialize the database
        cUserRepository.saveAndFlush(cUser);

		int databaseSizeBeforeUpdate = cUserRepository.findAll().size();

        // Update the cUser
        cUser.setMobile(UPDATED_MOBILE);
        cUser.setOpenId(UPDATED_OPENID);
        restCUserMockMvc.perform(put("/api/cUsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cUser)))
                .andExpect(status().isOk());

        // Validate the CUser in the database
        List<CUser> cUsers = cUserRepository.findAll();
        assertThat(cUsers).hasSize(databaseSizeBeforeUpdate);
        CUser testCUser = cUsers.get(cUsers.size() - 1);
        assertThat(testCUser.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testCUser.getOpenId()).isEqualTo(UPDATED_OPENID);
    }

    @Test
    @Transactional
    public void deleteCUser() throws Exception {
        // Initialize the database
        cUserRepository.saveAndFlush(cUser);

		int databaseSizeBeforeDelete = cUserRepository.findAll().size();

        // Get the cUser
        restCUserMockMvc.perform(delete("/api/cUsers/{id}", cUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CUser> cUsers = cUserRepository.findAll();
        assertThat(cUsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
