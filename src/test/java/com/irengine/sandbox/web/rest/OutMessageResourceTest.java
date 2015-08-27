package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.OutMessage;
import com.irengine.sandbox.repository.OutMessageRepository;

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
 * Test class for the OutMessageResource REST controller.
 *
 * @see OutMessageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OutMessageResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";
    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";
    private static final String DEFAULT_URL = "SAMPLE_TEXT";
    private static final String UPDATED_URL = "UPDATED_TEXT";
    private static final String DEFAULT_PIC_URL = "SAMPLE_TEXT";
    private static final String UPDATED_PIC_URL = "UPDATED_TEXT";
    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_MENU_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_MENU_NAME = "UPDATED_TEXT";

    private static final DateTime DEFAULT_START_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_START_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.print(DEFAULT_START_DATE);

    private static final DateTime DEFAULT_END_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_END_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.print(DEFAULT_END_DATE);

    private static final Boolean DEFAULT_DISABLE = false;
    private static final Boolean UPDATED_DISABLE = true;

    @Inject
    private OutMessageRepository outMessageRepository;

    private MockMvc restOutMessageMockMvc;

    private OutMessage outMessage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OutMessageResource outMessageResource = new OutMessageResource();
        ReflectionTestUtils.setField(outMessageResource, "outMessageRepository", outMessageRepository);
        this.restOutMessageMockMvc = MockMvcBuilders.standaloneSetup(outMessageResource).build();
    }

    @Before
    public void initTest() {
        outMessage = new OutMessage();
        outMessage.setType(DEFAULT_TYPE);
        outMessage.setContent(DEFAULT_CONTENT);
        outMessage.setUrl(DEFAULT_URL);
        outMessage.setPicUrl(DEFAULT_PIC_URL);
        outMessage.setTitle(DEFAULT_TITLE);
        outMessage.setMenuName(DEFAULT_MENU_NAME);
        outMessage.setStartDate(DEFAULT_START_DATE);
        outMessage.setEndDate(DEFAULT_END_DATE);
        outMessage.setDisable(DEFAULT_DISABLE);
    }

    @Test
    @Transactional
    public void createOutMessage() throws Exception {
        int databaseSizeBeforeCreate = outMessageRepository.findAll().size();

        // Create the OutMessage
        restOutMessageMockMvc.perform(post("/api/outMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outMessage)))
                .andExpect(status().isCreated());

        // Validate the OutMessage in the database
        List<OutMessage> outMessages = outMessageRepository.findAll();
        assertThat(outMessages).hasSize(databaseSizeBeforeCreate + 1);
        OutMessage testOutMessage = outMessages.get(outMessages.size() - 1);
        assertThat(testOutMessage.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOutMessage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testOutMessage.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testOutMessage.getPicUrl()).isEqualTo(DEFAULT_PIC_URL);
        assertThat(testOutMessage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOutMessage.getMenuName()).isEqualTo(DEFAULT_MENU_NAME);
        assertThat(testOutMessage.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOutMessage.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_DATE);
        assertThat(testOutMessage.getDisable()).isEqualTo(DEFAULT_DISABLE);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(outMessageRepository.findAll()).hasSize(0);
        // set the field null
        outMessage.setType(null);

        // Create the OutMessage, which fails.
        restOutMessageMockMvc.perform(post("/api/outMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outMessage)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<OutMessage> outMessages = outMessageRepository.findAll();
        assertThat(outMessages).hasSize(0);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(outMessageRepository.findAll()).hasSize(0);
        // set the field null
        outMessage.setContent(null);

        // Create the OutMessage, which fails.
        restOutMessageMockMvc.perform(post("/api/outMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outMessage)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<OutMessage> outMessages = outMessageRepository.findAll();
        assertThat(outMessages).hasSize(0);
    }

    @Test
    @Transactional
    public void checkMenuNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(outMessageRepository.findAll()).hasSize(0);
        // set the field null
        outMessage.setMenuName(null);

        // Create the OutMessage, which fails.
        restOutMessageMockMvc.perform(post("/api/outMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outMessage)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<OutMessage> outMessages = outMessageRepository.findAll();
        assertThat(outMessages).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllOutMessages() throws Exception {
        // Initialize the database
        outMessageRepository.saveAndFlush(outMessage);

        // Get all the outMessages
        restOutMessageMockMvc.perform(get("/api/outMessages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(outMessage.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].picUrl").value(hasItem(DEFAULT_PIC_URL.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].menuName").value(hasItem(DEFAULT_MENU_NAME.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].disable").value(hasItem(DEFAULT_DISABLE.booleanValue())));
    }

    @Test
    @Transactional
    public void getOutMessage() throws Exception {
        // Initialize the database
        outMessageRepository.saveAndFlush(outMessage);

        // Get the outMessage
        restOutMessageMockMvc.perform(get("/api/outMessages/{id}", outMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(outMessage.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.picUrl").value(DEFAULT_PIC_URL.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.menuName").value(DEFAULT_MENU_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.disable").value(DEFAULT_DISABLE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOutMessage() throws Exception {
        // Get the outMessage
        restOutMessageMockMvc.perform(get("/api/outMessages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutMessage() throws Exception {
        // Initialize the database
        outMessageRepository.saveAndFlush(outMessage);

		int databaseSizeBeforeUpdate = outMessageRepository.findAll().size();

        // Update the outMessage
        outMessage.setType(UPDATED_TYPE);
        outMessage.setContent(UPDATED_CONTENT);
        outMessage.setUrl(UPDATED_URL);
        outMessage.setPicUrl(UPDATED_PIC_URL);
        outMessage.setTitle(UPDATED_TITLE);
        outMessage.setMenuName(UPDATED_MENU_NAME);
        outMessage.setStartDate(UPDATED_START_DATE);
        outMessage.setEndDate(UPDATED_END_DATE);
        outMessage.setDisable(UPDATED_DISABLE);
        restOutMessageMockMvc.perform(put("/api/outMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outMessage)))
                .andExpect(status().isOk());

        // Validate the OutMessage in the database
        List<OutMessage> outMessages = outMessageRepository.findAll();
        assertThat(outMessages).hasSize(databaseSizeBeforeUpdate);
        OutMessage testOutMessage = outMessages.get(outMessages.size() - 1);
        assertThat(testOutMessage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOutMessage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testOutMessage.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testOutMessage.getPicUrl()).isEqualTo(UPDATED_PIC_URL);
        assertThat(testOutMessage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOutMessage.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testOutMessage.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_START_DATE);
        assertThat(testOutMessage.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_END_DATE);
        assertThat(testOutMessage.getDisable()).isEqualTo(UPDATED_DISABLE);
    }

    @Test
    @Transactional
    public void deleteOutMessage() throws Exception {
        // Initialize the database
        outMessageRepository.saveAndFlush(outMessage);

		int databaseSizeBeforeDelete = outMessageRepository.findAll().size();

        // Get the outMessage
        restOutMessageMockMvc.perform(delete("/api/outMessages/{id}", outMessage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OutMessage> outMessages = outMessageRepository.findAll();
        assertThat(outMessages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
