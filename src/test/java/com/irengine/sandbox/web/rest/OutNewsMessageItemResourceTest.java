package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.OutNewsMessageItem;
import com.irengine.sandbox.repository.OutNewsMessageItemRepository;

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
 * Test class for the OutNewsMessageItemResource REST controller.
 *
 * @see OutNewsMessageItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OutNewsMessageItemResourceTest {

    private static final String DEFAULT_PIC_URL = "SAMPLE_TEXT";
    private static final String UPDATED_PIC_URL = "UPDATED_TEXT";
    private static final String DEFAULT_URL = "SAMPLE_TEXT";
    private static final String UPDATED_URL = "UPDATED_TEXT";
    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";

    @Inject
    private OutNewsMessageItemRepository outNewsMessageItemRepository;

    private MockMvc restOutNewsMessageItemMockMvc;

    private OutNewsMessageItem outNewsMessageItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OutNewsMessageItemResource outNewsMessageItemResource = new OutNewsMessageItemResource();
        ReflectionTestUtils.setField(outNewsMessageItemResource, "outNewsMessageItemRepository", outNewsMessageItemRepository);
        this.restOutNewsMessageItemMockMvc = MockMvcBuilders.standaloneSetup(outNewsMessageItemResource).build();
    }

    @Before
    public void initTest() {
        outNewsMessageItem = new OutNewsMessageItem();
        outNewsMessageItem.setPicUrl(DEFAULT_PIC_URL);
        outNewsMessageItem.setUrl(DEFAULT_URL);
        outNewsMessageItem.setContent(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createOutNewsMessageItem() throws Exception {
        int databaseSizeBeforeCreate = outNewsMessageItemRepository.findAll().size();

        // Create the OutNewsMessageItem
        restOutNewsMessageItemMockMvc.perform(post("/api/outNewsMessageItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outNewsMessageItem)))
                .andExpect(status().isCreated());

        // Validate the OutNewsMessageItem in the database
        List<OutNewsMessageItem> outNewsMessageItems = outNewsMessageItemRepository.findAll();
        assertThat(outNewsMessageItems).hasSize(databaseSizeBeforeCreate + 1);
        OutNewsMessageItem testOutNewsMessageItem = outNewsMessageItems.get(outNewsMessageItems.size() - 1);
        assertThat(testOutNewsMessageItem.getPicUrl()).isEqualTo(DEFAULT_PIC_URL);
        assertThat(testOutNewsMessageItem.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testOutNewsMessageItem.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void getAllOutNewsMessageItems() throws Exception {
        // Initialize the database
        outNewsMessageItemRepository.saveAndFlush(outNewsMessageItem);

        // Get all the outNewsMessageItems
        restOutNewsMessageItemMockMvc.perform(get("/api/outNewsMessageItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(outNewsMessageItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].picUrl").value(hasItem(DEFAULT_PIC_URL.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getOutNewsMessageItem() throws Exception {
        // Initialize the database
        outNewsMessageItemRepository.saveAndFlush(outNewsMessageItem);

        // Get the outNewsMessageItem
        restOutNewsMessageItemMockMvc.perform(get("/api/outNewsMessageItems/{id}", outNewsMessageItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(outNewsMessageItem.getId().intValue()))
            .andExpect(jsonPath("$.picUrl").value(DEFAULT_PIC_URL.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOutNewsMessageItem() throws Exception {
        // Get the outNewsMessageItem
        restOutNewsMessageItemMockMvc.perform(get("/api/outNewsMessageItems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutNewsMessageItem() throws Exception {
        // Initialize the database
        outNewsMessageItemRepository.saveAndFlush(outNewsMessageItem);

		int databaseSizeBeforeUpdate = outNewsMessageItemRepository.findAll().size();

        // Update the outNewsMessageItem
        outNewsMessageItem.setPicUrl(UPDATED_PIC_URL);
        outNewsMessageItem.setUrl(UPDATED_URL);
        outNewsMessageItem.setContent(UPDATED_CONTENT);
        restOutNewsMessageItemMockMvc.perform(put("/api/outNewsMessageItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outNewsMessageItem)))
                .andExpect(status().isOk());

        // Validate the OutNewsMessageItem in the database
        List<OutNewsMessageItem> outNewsMessageItems = outNewsMessageItemRepository.findAll();
        assertThat(outNewsMessageItems).hasSize(databaseSizeBeforeUpdate);
        OutNewsMessageItem testOutNewsMessageItem = outNewsMessageItems.get(outNewsMessageItems.size() - 1);
        assertThat(testOutNewsMessageItem.getPicUrl()).isEqualTo(UPDATED_PIC_URL);
        assertThat(testOutNewsMessageItem.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testOutNewsMessageItem.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void deleteOutNewsMessageItem() throws Exception {
        // Initialize the database
        outNewsMessageItemRepository.saveAndFlush(outNewsMessageItem);

		int databaseSizeBeforeDelete = outNewsMessageItemRepository.findAll().size();

        // Get the outNewsMessageItem
        restOutNewsMessageItemMockMvc.perform(delete("/api/outNewsMessageItems/{id}", outNewsMessageItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OutNewsMessageItem> outNewsMessageItems = outNewsMessageItemRepository.findAll();
        assertThat(outNewsMessageItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
