package pt.ua.deliveryplatform.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import pt.ua.deliveryplatform.dto.CreateRiderDto;
import pt.ua.deliveryplatform.model.Rider;
import pt.ua.deliveryplatform.repositories.RiderRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class RiderControllerTest {

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
    private RiderRepository riderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        riderRepository.deleteAll();
    }

    @Test
    public void givenRiderDto_whenCreateRider_thenReturnSavedRider() throws Exception {
        CreateRiderDto createRiderDto = CreateRiderDto.builder()
                .name("Rider1")
                .build();

        ResultActions response = mockMvc.perform(post("/riders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRiderDto)));

        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(createRiderDto.getName())));
    }

    @Test
    public void givenListOfRiders_whenGetALlRiders_thenReturnRiderList() throws Exception {
        Rider rider1 = Rider.builder()
                .name("Rider1")
                .build();
        Rider rider2 = Rider.builder()
                .name("Rider2")
                .build();

        riderRepository.save(rider1);
        riderRepository.save(rider2);

        ResultActions response = mockMvc.perform(get("/riders"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    public void givenRiderId_whenGetRiderById_thenReturnRider() throws Exception {
        Rider rider1 = Rider.builder()
                .name("Rider1")
                .build();
        riderRepository.save(rider1);

        ResultActions response = mockMvc.perform(get("/riders/{id}", rider1.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(rider1.getName())));
    }

    @Test
    public void givenInvalidRiderId_whenGetRiderById_thenReturnBadRequest() throws Exception {
        Rider rider1 = Rider.builder()
                .name("Rider1")
                .build();
        riderRepository.save(rider1);

        ResultActions response = mockMvc.perform(get("/riders/{id}", rider1.getId() + 999));

        response.andExpect(status().isBadRequest());
    }

}