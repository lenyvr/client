package com.devsu.fintech.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ClientIntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:18-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> createPayload(String identification, String clientCode, String firstName) {
        return Map.ofEntries(
                Map.entry("firstName", firstName),
                Map.entry("lastName", "Tester"),
                Map.entry("age", 30),
                Map.entry("genderId", 2),
                Map.entry("identificationNumber", identification),
                Map.entry("identificationTypeId", 1),
                Map.entry("address", "Main street"),
                Map.entry("postalCode", "10001"),
                Map.entry("contactNumber", "0991112233"),
                Map.entry("email", "tester@mail.com"),
                Map.entry("clientCode", clientCode),
                Map.entry("password", "secret123")
        );
    }

    @Test
    void fullClientLifecycle() throws Exception {
        // Create a new client
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload("90001", "CINT1", "Ana"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").exists())
                .andExpect(jsonPath("$.status").value(true));

        // Filter is case-insensitive and partial
        mockMvc.perform(get("/clients").param("filter", "ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.firstName == 'Ana')]", hasSize(1)));

        // Creating again with the same identification (active) -> 409
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload("90001", "CINT2", "Ana"))))
                .andExpect(status().isConflict());
    }

    @Test
    void updateAndDeactivate() throws Exception {
        String response = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload("90002", "CINT3", "Beto"))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long clientId = objectMapper.readTree(response).get("clientId").asLong();

        // Update without password keeps it and updates person data
        Map<String, Object> update = createPayload("90002", "CINT3", "Beto");
        update = new java.util.HashMap<>(update);
        update.put("firstName", "Beto Updated");
        update.remove("password");
        mockMvc.perform(put("/clients/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Beto Updated"));

        // Deactivate -> 204
        mockMvc.perform(delete("/clients/" + clientId))
                .andExpect(status().isNoContent());

        // Deactivated client must not appear in the active list
        mockMvc.perform(get("/clients").param("filter", "Beto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.clientId == " + clientId + ")]", hasSize(0)));

        // Deactivating a missing client -> 404
        mockMvc.perform(delete("/clients/999999"))
                .andExpect(status().isNotFound());
    }
}
