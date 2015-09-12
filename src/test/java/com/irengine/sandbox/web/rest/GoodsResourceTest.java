package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.Goods;
import com.irengine.sandbox.repository.GoodsRepository;

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
 * Test class for the GoodsResource REST controller.
 *
 * @see GoodsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GoodsResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private GoodsRepository goodsRepository;

    private MockMvc restGoodsMockMvc;

    private Goods goods;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GoodsResource goodsResource = new GoodsResource();
        ReflectionTestUtils.setField(goodsResource, "goodsRepository", goodsRepository);
        this.restGoodsMockMvc = MockMvcBuilders.standaloneSetup(goodsResource).build();
    }

    @Before
    public void initTest() {
        goods = new Goods();
        goods.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createGoods() throws Exception {
        int databaseSizeBeforeCreate = goodsRepository.findAll().size();

        // Create the Goods
        restGoodsMockMvc.perform(post("/api/goodss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(goods)))
                .andExpect(status().isCreated());

        // Validate the Goods in the database
        List<Goods> goodss = goodsRepository.findAll();
        assertThat(goodss).hasSize(databaseSizeBeforeCreate + 1);
        Goods testGoods = goodss.get(goodss.size() - 1);
        assertThat(testGoods.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(goodsRepository.findAll()).hasSize(0);
        // set the field null
        goods.setName(null);

        // Create the Goods, which fails.
        restGoodsMockMvc.perform(post("/api/goodss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(goods)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Goods> goodss = goodsRepository.findAll();
        assertThat(goodss).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllGoodss() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodss
        restGoodsMockMvc.perform(get("/api/goodss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(goods.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getGoods() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get the goods
        restGoodsMockMvc.perform(get("/api/goodss/{id}", goods.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(goods.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGoods() throws Exception {
        // Get the goods
        restGoodsMockMvc.perform(get("/api/goodss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoods() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

		int databaseSizeBeforeUpdate = goodsRepository.findAll().size();

        // Update the goods
        goods.setName(UPDATED_NAME);
        restGoodsMockMvc.perform(put("/api/goodss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(goods)))
                .andExpect(status().isOk());

        // Validate the Goods in the database
        List<Goods> goodss = goodsRepository.findAll();
        assertThat(goodss).hasSize(databaseSizeBeforeUpdate);
        Goods testGoods = goodss.get(goodss.size() - 1);
        assertThat(testGoods.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteGoods() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

		int databaseSizeBeforeDelete = goodsRepository.findAll().size();

        // Get the goods
        restGoodsMockMvc.perform(delete("/api/goodss/{id}", goods.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Goods> goodss = goodsRepository.findAll();
        assertThat(goodss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
