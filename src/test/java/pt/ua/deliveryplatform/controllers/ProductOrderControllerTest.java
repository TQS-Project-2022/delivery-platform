package pt.ua.deliveryplatform.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pt.ua.deliveryplatform.dto.CreateOrderDto;
import pt.ua.deliveryplatform.model.ProductOrder;
import pt.ua.deliveryplatform.model.Rider;
import pt.ua.deliveryplatform.repositories.ProductOrderRepository;
import pt.ua.deliveryplatform.repositories.RiderRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ProductOrderControllerTest {

    @Container
    public static MySQLContainer mySQLContainer = new MySQLContainer("mysql")
            .withDatabaseName("database")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        productOrderRepository.deleteAll();
        riderRepository.deleteAll();
    }

    @Test
    public void givenOrderDto_whenCreateOrder_thenReturnSavedOrder() throws Exception {
        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .productId(1)
                .build();

        Rider rider = Rider.builder()
                .name("rider1")
                .build();
        riderRepository.save(rider);

        ResultActions response = mockMvc.perform(post("/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderDto)));

        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", is(createOrderDto.getProductId())));
    }

    @Test
    public void givenOrderDto_whenCreateOrderAndNoRiderAvailable_thenReturnBadRequest() throws Exception {
        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .productId(1)
                .build();

        Rider rider = Rider.builder()
                .name("rider1")
                .busy(true)
                .build();
        riderRepository.save(rider);

        ResultActions response = mockMvc.perform(post("/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderDto)));

        response.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void givenOrderId_whenGetOrderById_thenReturnOrder() throws Exception {
        ProductOrder order = ProductOrder.builder()
                .productId(1)
                .build();
        productOrderRepository.save(order);

        ResultActions response = mockMvc.perform(get("/orders/{id}", order.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(order.getProductId())));
    }

    @Test
    public void givenWrongOrderId_whenGetOrderById_thenReturnBadRequest() throws Exception {
        ProductOrder order = ProductOrder.builder()
                .productId(1)
                .build();
        productOrderRepository.save(order);

        ResultActions response = mockMvc.perform(get("/orders/{id}", order.getId() + 123));

        response.andExpect(status().isBadRequest());
    }

    @Test
    public void givenUndeliveredOrder_whenConfirmDelivery_thenReturnDeliveredOrder() throws Exception {
        Rider rider = Rider.builder()
                .name("rider1")
                .build();
        riderRepository.save(rider);

        ProductOrder order = ProductOrder.builder()
                .productId(1)
                .rider(rider)
                .build();
        productOrderRepository.save(order);



        ResultActions response = mockMvc.perform(patch("/orders/confirm/{id}", order.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.delivered", is(true)));
    }



}