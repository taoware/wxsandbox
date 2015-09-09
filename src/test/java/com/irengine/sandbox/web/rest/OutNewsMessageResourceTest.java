package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.OutNewsMessage;
import com.irengine.sandbox.repository.OutNewsMessageRepository;

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
 * Test class for the OutNewsMessageResource REST controller.
 *
 * @see OutNewsMessageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OutNewsMessageResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_MENU_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_MENU_NAME = "UPDATED_TEXT";

    private static final DateTime DEFAULT_START_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_START_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.print(DEFAULT_START_DATE);

    private static final DateTime DEFAULT_END_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_END_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.print(DEFAULT_END_DATE);

    @Inject
    private OutNewsMessageRepository outNewsMessageRepository;

    private MockMvc restOutNewsMessageMockMvc;

    private OutNewsMessage outNewsMessage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OutNewsMessageResource outNewsMessageResource = new OutNewsMessageResource();
        ReflectionTestUtils.setField(outNewsMessageResource, "outNewsMessageRepository", outNewsMessageRepository);
        this.restOutNewsMessageMockMvc = MockMvcBuilders.standaloneSetup(outNewsMessageResource).build();
    }

    @Before
    public void initTest() {
        outNewsMessage = new OutNewsMessage();
        outNewsMessage.setMenuName(DEFAULT_MENU_NAME);
        outNewsMessage.setStartDate(DEFAULT_START_DATE);
        outNewsMessage.setEndDate(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createOutNewsMessage() throws Exception {
        int databaseSizeBeforeCreate = outNewsMessageRepository.findAll().size();

        // Create the OutNewsMessage
        restOutNewsMessageMockMvc.perform(post("/api/outNewsMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outNewsMessage)))
                .andExpect(status().isCreated());

        // Validate the OutNewsMessage in the database
        List<OutNewsMessage> outNewsMessages = outNewsMessageRepository.findAll();
        assertThat(outNewsMessages).hasSize(databaseSizeBeforeCreate + 1);
        OutNewsMessage testOutNewsMessage = outNewsMessages.get(outNewsMessages.size() - 1);
        assertThat(testOutNewsMessage.getMenuName()).isEqualTo(DEFAULT_MENU_NAME);
        assertThat(testOutNewsMessage.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOutNewsMessage.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void checkMenuNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(outNewsMessageRepository.findAll()).hasSize(0);
        // set the field null
        outNewsMessage.setMenuName(null);

        // Create the OutNewsMessage, which fails.
        restOutNewsMessageMockMvc.perform(post("/api/outNewsMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outNewsMessage)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<OutNewsMessage> outNewsMessages = outNewsMessageRepository.findAll();
        assertThat(outNewsMessages).hasSize(0);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(outNewsMessageRepository.findAll()).hasSize(0);
        // set the field null
        outNewsMessage.setStartDate(null);

        // Create the OutNewsMessage, which fails.
        restOutNewsMessageMockMvc.perform(post("/api/outNewsMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outNewsMessage)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<OutNewsMessage> outNewsMessages = outNewsMessageRepository.findAll();
        assertThat(outNewsMessages).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllOutNewsMessages() throws Exception {
        // Initialize the database
        outNewsMessageRepository.saveAndFlush(outNewsMessage);

        // Get all the outNewsMessages
        restOutNewsMessageMockMvc.perform(get("/api/outNewsMessages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(outNewsMessage.getId().intValue())))
                .andExpect(jsonPath("$.[*].menuName").value(hasItem(DEFAULT_MENU_NAME.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }

    @Test
    @Transactional
    public void getOutNewsMessage() throws Exception {
        // Initialize the database
        outNewsMessageRepository.saveAndFlush(outNewsMessage);

        // Get the outNewsMessage
        restOutNewsMessageMockMvc.perform(get("/api/outNewsMessages/{id}", outNewsMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(outNewsMessage.getId().intValue()))
            .andExpect(jsonPath("$.menuName").value(DEFAULT_MENU_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingOutNewsMessage() throws Exception {
        // Get the outNewsMessage
        restOutNewsMessageMockMvc.perform(get("/api/outNewsMessages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutNewsMessage() throws Exception {
        // Initialize the database
        outNewsMessageRepository.saveAndFlush(outNewsMessage);

		int databaseSizeBeforeUpdate = outNewsMessageRepository.findAll().size();

        // Update the outNewsMessage
        outNewsMessage.setMenuName(UPDATED_MENU_NAME);
        outNewsMessage.setStartDate(UPDATED_START_DATE);
        outNewsMessage.setEndDate(UPDATED_END_DATE);
        restOutNewsMessageMockMvc.perform(put("/api/outNewsMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outNewsMessage)))
                .andExpect(status().isOk());

        // Validate the OutNewsMessage in the database
        List<OutNewsMessage> outNewsMessages = outNewsMessageRepository.findAll();
        assertThat(outNewsMessages).hasSize(databaseSizeBeforeUpdate);
        OutNewsMessage testOutNewsMessage = outNewsMessages.get(outNewsMessages.size() - 1);
        assertThat(testOutNewsMessage.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testOutNewsMessage.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_START_DATE);
        assertThat(testOutNewsMessage.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteOutNewsMessage() throws Exception {
        // Initialize the database
        outNewsMessageRepository.saveAndFlush(outNewsMessage);

		int databaseSizeBeforeDelete = outNewsMessageRepository.findAll().size();

        // Get the outNewsMessage
        restOutNewsMessageMockMvc.perform(delete("/api/outNewsMessages/{id}", outNewsMessage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OutNewsMessage> outNewsMessages = outNewsMessageRepository.findAll();
        assertThat(outNewsMessages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
