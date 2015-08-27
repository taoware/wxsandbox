package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.Supplier;
import com.irengine.sandbox.repository.SupplierRepository;

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
 * Test class for the SupplierResource REST controller.
 *
 * @see SupplierResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SupplierResourceTest {

    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS = "UPDATED_TEXT";
    private static final String DEFAULT_CONTACT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTACT = "UPDATED_TEXT";

    @Inject
    private SupplierRepository supplierRepository;

    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SupplierResource supplierResource = new SupplierResource();
        ReflectionTestUtils.setField(supplierResource, "supplierRepository", supplierRepository);
        this.restSupplierMockMvc = MockMvcBuilders.standaloneSetup(supplierResource).build();
    }

    @Before
    public void initTest() {
        supplier = new Supplier();
        supplier.setCode(DEFAULT_CODE);
        supplier.setName(DEFAULT_NAME);
        supplier.setAddress(DEFAULT_ADDRESS);
        supplier.setContact(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    public void createSupplier() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();

        // Create the Supplier
        restSupplierMockMvc.perform(post("/api/suppliers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplier)))
                .andExpect(status().isCreated());

        // Validate the Supplier in the database
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeCreate + 1);
        Supplier testSupplier = suppliers.get(suppliers.size() - 1);
        assertThat(testSupplier.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSupplier.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplier.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSupplier.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(supplierRepository.findAll()).hasSize(0);
        // set the field null
        supplier.setName(null);

        // Create the Supplier, which fails.
        restSupplierMockMvc.perform(post("/api/suppliers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplier)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllSuppliers() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the suppliers
        restSupplierMockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())));
    }

    @Test
    @Transactional
    public void getSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

		int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier
        supplier.setCode(UPDATED_CODE);
        supplier.setName(UPDATED_NAME);
        supplier.setAddress(UPDATED_ADDRESS);
        supplier.setContact(UPDATED_CONTACT);
        restSupplierMockMvc.perform(put("/api/suppliers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplier)))
                .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = suppliers.get(suppliers.size() - 1);
        assertThat(testSupplier.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSupplier.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void deleteSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

		int databaseSizeBeforeDelete = supplierRepository.findAll().size();

        // Get the supplier
        restSupplierMockMvc.perform(delete("/api/suppliers/{id}", supplier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
