package com.irengine.sandbox.web.rest;

import com.irengine.sandbox.Application;
import com.irengine.sandbox.domain.Order;
import com.irengine.sandbox.repository.OrderRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.irengine.sandbox.domain.enumeration.DataType;

/**
 * Test class for the OrderResource REST controller.
 *
 * @see OrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrderResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_MY_STRING = "SAMPLE_TEXT";
    private static final String UPDATED_MY_STRING = "UPDATED_TEXT";

    private static final Integer DEFAULT_MY_INTEGER = 1;
    private static final Integer UPDATED_MY_INTEGER = 2;

    private static final Long DEFAULT_MY_LONG = 1L;
    private static final Long UPDATED_MY_LONG = 2L;

    private static final Float DEFAULT_MY_FLOAT = 1F;
    private static final Float UPDATED_MY_FLOAT = 2F;

    private static final Double DEFAULT_MY_DOUBLE = 1D;
    private static final Double UPDATED_MY_DOUBLE = 2D;

    private static final BigDecimal DEFAULT_MY_DECIMAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_MY_DECIMAL = new BigDecimal(2);

    private static final LocalDate DEFAULT_MY_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_MY_DATE = new LocalDate();

    private static final DateTime DEFAULT_MY_DATE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_MY_DATE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_MY_DATE_TIME_STR = dateTimeFormatter.print(DEFAULT_MY_DATE_TIME);

    private static final Boolean DEFAULT_MY_BOOLEAN = false;
    private static final Boolean UPDATED_MY_BOOLEAN = true;

    private static final DataType DEFAULT_MY_ENUMERATION = DataType.String;
    private static final DataType UPDATED_MY_ENUMERATION = DataType.Decimal;

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restOrderMockMvc;

    private Order order;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderResource orderResource = new OrderResource();
        ReflectionTestUtils.setField(orderResource, "orderRepository", orderRepository);
        this.restOrderMockMvc = MockMvcBuilders.standaloneSetup(orderResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        order = new Order();
        order.setMyString(DEFAULT_MY_STRING);
        order.setMyInteger(DEFAULT_MY_INTEGER);
        order.setMyLong(DEFAULT_MY_LONG);
        order.setMyFloat(DEFAULT_MY_FLOAT);
        order.setMyDouble(DEFAULT_MY_DOUBLE);
        order.setMyDecimal(DEFAULT_MY_DECIMAL);
        order.setMyDate(DEFAULT_MY_DATE);
        order.setMyDateTime(DEFAULT_MY_DATE_TIME);
        order.setMyBoolean(DEFAULT_MY_BOOLEAN);
        order.setMyEnumeration(DEFAULT_MY_ENUMERATION);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order

        restOrderMockMvc.perform(post("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orders.get(orders.size() - 1);
        assertThat(testOrder.getMyString()).isEqualTo(DEFAULT_MY_STRING);
        assertThat(testOrder.getMyInteger()).isEqualTo(DEFAULT_MY_INTEGER);
        assertThat(testOrder.getMyLong()).isEqualTo(DEFAULT_MY_LONG);
        assertThat(testOrder.getMyFloat()).isEqualTo(DEFAULT_MY_FLOAT);
        assertThat(testOrder.getMyDouble()).isEqualTo(DEFAULT_MY_DOUBLE);
        assertThat(testOrder.getMyDecimal()).isEqualTo(DEFAULT_MY_DECIMAL);
        assertThat(testOrder.getMyDate()).isEqualTo(DEFAULT_MY_DATE);
        assertThat(testOrder.getMyDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_MY_DATE_TIME);
        assertThat(testOrder.getMyBoolean()).isEqualTo(DEFAULT_MY_BOOLEAN);
        assertThat(testOrder.getMyEnumeration()).isEqualTo(DEFAULT_MY_ENUMERATION);
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orders
        restOrderMockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
                .andExpect(jsonPath("$.[*].myString").value(hasItem(DEFAULT_MY_STRING.toString())))
                .andExpect(jsonPath("$.[*].myInteger").value(hasItem(DEFAULT_MY_INTEGER)))
                .andExpect(jsonPath("$.[*].myLong").value(hasItem(DEFAULT_MY_LONG.intValue())))
                .andExpect(jsonPath("$.[*].myFloat").value(hasItem(DEFAULT_MY_FLOAT.doubleValue())))
                .andExpect(jsonPath("$.[*].myDouble").value(hasItem(DEFAULT_MY_DOUBLE.doubleValue())))
                .andExpect(jsonPath("$.[*].myDecimal").value(hasItem(DEFAULT_MY_DECIMAL.intValue())))
                .andExpect(jsonPath("$.[*].myDate").value(hasItem(DEFAULT_MY_DATE.toString())))
                .andExpect(jsonPath("$.[*].myDateTime").value(hasItem(DEFAULT_MY_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].myBoolean").value(hasItem(DEFAULT_MY_BOOLEAN.booleanValue())))
                .andExpect(jsonPath("$.[*].myEnumeration").value(hasItem(DEFAULT_MY_ENUMERATION.toString())));
    }

    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.myString").value(DEFAULT_MY_STRING.toString()))
            .andExpect(jsonPath("$.myInteger").value(DEFAULT_MY_INTEGER))
            .andExpect(jsonPath("$.myLong").value(DEFAULT_MY_LONG.intValue()))
            .andExpect(jsonPath("$.myFloat").value(DEFAULT_MY_FLOAT.doubleValue()))
            .andExpect(jsonPath("$.myDouble").value(DEFAULT_MY_DOUBLE.doubleValue()))
            .andExpect(jsonPath("$.myDecimal").value(DEFAULT_MY_DECIMAL.intValue()))
            .andExpect(jsonPath("$.myDate").value(DEFAULT_MY_DATE.toString()))
            .andExpect(jsonPath("$.myDateTime").value(DEFAULT_MY_DATE_TIME_STR))
            .andExpect(jsonPath("$.myBoolean").value(DEFAULT_MY_BOOLEAN.booleanValue()))
            .andExpect(jsonPath("$.myEnumeration").value(DEFAULT_MY_ENUMERATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

		int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        order.setMyString(UPDATED_MY_STRING);
        order.setMyInteger(UPDATED_MY_INTEGER);
        order.setMyLong(UPDATED_MY_LONG);
        order.setMyFloat(UPDATED_MY_FLOAT);
        order.setMyDouble(UPDATED_MY_DOUBLE);
        order.setMyDecimal(UPDATED_MY_DECIMAL);
        order.setMyDate(UPDATED_MY_DATE);
        order.setMyDateTime(UPDATED_MY_DATE_TIME);
        order.setMyBoolean(UPDATED_MY_BOOLEAN);
        order.setMyEnumeration(UPDATED_MY_ENUMERATION);
        

        restOrderMockMvc.perform(put("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orders.get(orders.size() - 1);
        assertThat(testOrder.getMyString()).isEqualTo(UPDATED_MY_STRING);
        assertThat(testOrder.getMyInteger()).isEqualTo(UPDATED_MY_INTEGER);
        assertThat(testOrder.getMyLong()).isEqualTo(UPDATED_MY_LONG);
        assertThat(testOrder.getMyFloat()).isEqualTo(UPDATED_MY_FLOAT);
        assertThat(testOrder.getMyDouble()).isEqualTo(UPDATED_MY_DOUBLE);
        assertThat(testOrder.getMyDecimal()).isEqualTo(UPDATED_MY_DECIMAL);
        assertThat(testOrder.getMyDate()).isEqualTo(UPDATED_MY_DATE);
        assertThat(testOrder.getMyDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_MY_DATE_TIME);
        assertThat(testOrder.getMyBoolean()).isEqualTo(UPDATED_MY_BOOLEAN);
        assertThat(testOrder.getMyEnumeration()).isEqualTo(UPDATED_MY_ENUMERATION);
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

		int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Get the order
        restOrderMockMvc.perform(delete("/api/orders/{id}", order.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
