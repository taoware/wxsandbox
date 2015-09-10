package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.UserInfo;
import com.irengine.sandbox.repository.UserInfoRepository;

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
 * Test class for the UserInfoResource REST controller.
 *
 * @see UserInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserInfoResourceTest {

    private static final String DEFAULT_OPEN_ID = "SAMPLE_TEXT";
    private static final String UPDATED_OPEN_ID = "UPDATED_TEXT";
    private static final String DEFAULT_NICKNAME = "SAMPLE_TEXT";
    private static final String UPDATED_NICKNAME = "UPDATED_TEXT";
    private static final String DEFAULT_COUNTRY = "SAMPLE_TEXT";
    private static final String UPDATED_COUNTRY = "UPDATED_TEXT";
    private static final String DEFAULT_CITY = "SAMPLE_TEXT";
    private static final String UPDATED_CITY = "UPDATED_TEXT";
    private static final String DEFAULT_PROVINCE = "SAMPLE_TEXT";
    private static final String UPDATED_PROVINCE = "UPDATED_TEXT";
    private static final String DEFAULT_MOBILE = "SAMPLE_TEXT";
    private static final String UPDATED_MOBILE = "UPDATED_TEXT";
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_COLLEGE = "SAMPLE_TEXT";
    private static final String UPDATED_COLLEGE = "UPDATED_TEXT";
    private static final String DEFAULT_SPECIALTY = "SAMPLE_TEXT";
    private static final String UPDATED_SPECIALTY = "UPDATED_TEXT";

    private static final Integer DEFAULT_TH = 0;
    private static final Integer UPDATED_TH = 1;

    private static final Integer DEFAULT_GENDER = 0;
    private static final Integer UPDATED_GENDER = 1;

    @Inject
    private UserInfoRepository userInfoRepository;

    private MockMvc restUserInfoMockMvc;

    private UserInfo userInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserInfoResource userInfoResource = new UserInfoResource();
        ReflectionTestUtils.setField(userInfoResource, "userInfoRepository", userInfoRepository);
        this.restUserInfoMockMvc = MockMvcBuilders.standaloneSetup(userInfoResource).build();
    }

    @Before
    public void initTest() {
        userInfo = new UserInfo();
        userInfo.setOpenId(DEFAULT_OPEN_ID);
        userInfo.setNickname(DEFAULT_NICKNAME);
        userInfo.setCountry(DEFAULT_COUNTRY);
        userInfo.setCity(DEFAULT_CITY);
        userInfo.setProvince(DEFAULT_PROVINCE);
        userInfo.setMobile(DEFAULT_MOBILE);
        userInfo.setName(DEFAULT_NAME);
        userInfo.setCollege(DEFAULT_COLLEGE);
        userInfo.setSpecialty(DEFAULT_SPECIALTY);
        userInfo.setTh(DEFAULT_TH);
        userInfo.setGender(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    public void createUserInfo() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo
        restUserInfoMockMvc.perform(post("/api/userInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfo)))
                .andExpect(status().isCreated());

        // Validate the UserInfo in the database
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeCreate + 1);
        UserInfo testUserInfo = userInfos.get(userInfos.size() - 1);
        assertThat(testUserInfo.getOpenId()).isEqualTo(DEFAULT_OPEN_ID);
        assertThat(testUserInfo.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testUserInfo.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserInfo.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserInfo.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testUserInfo.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testUserInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserInfo.getCollege()).isEqualTo(DEFAULT_COLLEGE);
        assertThat(testUserInfo.getSpecialty()).isEqualTo(DEFAULT_SPECIALTY);
        assertThat(testUserInfo.getTh()).isEqualTo(DEFAULT_TH);
        assertThat(testUserInfo.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    public void checkOpenIdIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(userInfoRepository.findAll()).hasSize(0);
        // set the field null
        userInfo.setOpenId(null);

        // Create the UserInfo, which fails.
        restUserInfoMockMvc.perform(post("/api/userInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfo)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllUserInfos() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfos
        restUserInfoMockMvc.perform(get("/api/userInfos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].openId").value(hasItem(DEFAULT_OPEN_ID.toString())))
                .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())))
                .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].college").value(hasItem(DEFAULT_COLLEGE.toString())))
                .andExpect(jsonPath("$.[*].specialty").value(hasItem(DEFAULT_SPECIALTY.toString())))
                .andExpect(jsonPath("$.[*].th").value(hasItem(DEFAULT_TH)))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)));
    }

    @Test
    @Transactional
    public void getUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/userInfos/{id}", userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userInfo.getId().intValue()))
            .andExpect(jsonPath("$.openId").value(DEFAULT_OPEN_ID.toString()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.college").value(DEFAULT_COLLEGE.toString()))
            .andExpect(jsonPath("$.specialty").value(DEFAULT_SPECIALTY.toString()))
            .andExpect(jsonPath("$.th").value(DEFAULT_TH))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER));
    }

    @Test
    @Transactional
    public void getNonExistingUserInfo() throws Exception {
        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/userInfos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

		int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo
        userInfo.setOpenId(UPDATED_OPEN_ID);
        userInfo.setNickname(UPDATED_NICKNAME);
        userInfo.setCountry(UPDATED_COUNTRY);
        userInfo.setCity(UPDATED_CITY);
        userInfo.setProvince(UPDATED_PROVINCE);
        userInfo.setMobile(UPDATED_MOBILE);
        userInfo.setName(UPDATED_NAME);
        userInfo.setCollege(UPDATED_COLLEGE);
        userInfo.setSpecialty(UPDATED_SPECIALTY);
        userInfo.setTh(UPDATED_TH);
        userInfo.setGender(UPDATED_GENDER);
        restUserInfoMockMvc.perform(put("/api/userInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfo)))
                .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfos.get(userInfos.size() - 1);
        assertThat(testUserInfo.getOpenId()).isEqualTo(UPDATED_OPEN_ID);
        assertThat(testUserInfo.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testUserInfo.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserInfo.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserInfo.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testUserInfo.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testUserInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserInfo.getCollege()).isEqualTo(UPDATED_COLLEGE);
        assertThat(testUserInfo.getSpecialty()).isEqualTo(UPDATED_SPECIALTY);
        assertThat(testUserInfo.getTh()).isEqualTo(UPDATED_TH);
        assertThat(testUserInfo.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void deleteUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

		int databaseSizeBeforeDelete = userInfoRepository.findAll().size();

        // Get the userInfo
        restUserInfoMockMvc.perform(delete("/api/userInfos/{id}", userInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
