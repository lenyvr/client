package com.devsu.fintech.infrastructure;

import com.devsu.fintech.domain.exception.AccountsServiceUnavailableException;
import com.devsu.fintech.domain.port.out.AccountsValidationSPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
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
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // Replaces the real listener so the SMLC never tries to connect to RabbitMQ on startup.
    @MockitoBean("replyContainer")
    SimpleMessageListenerContainer replyContainer;

    // Replaces AccountsValidationRabbitMQAdapter so no real RabbitMQ connection is needed.
    @MockitoBean
    AccountsValidationSPI accountsValidation;

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

    private Long createClient(String identification, String clientCode, String firstName) throws Exception {
        String response = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload(identification, clientCode, firstName))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("clientId").asLong();
    }

    @Test
    void createReturnsCreatedWithActiveStatus() throws Exception {
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload("90001", "CINT1", "Ana"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").exists())
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void listFilterIsCaseInsensitiveAndPartial() throws Exception {
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload("90002", "CINT2", "Carlos"))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/clients").param("filter", "carl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.firstName == 'Carlos')]", hasSize(1)));
    }

    @Test
    void createDuplicateActiveClientReturnsConflict() throws Exception {
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload("90003", "CINT3", "Diana"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload("90003", "CINT4", "Diana"))))
                .andExpect(status().isConflict());
    }

    @Test
    void createReactivatesInactiveClient() throws Exception {
        Long clientId = createClient("90004", "CINT5", "Eva");
        when(accountsValidation.hasOpenAccounts(clientId)).thenReturn(false);
        mockMvc.perform(delete("/clients/" + clientId)).andExpect(status().isNoContent());

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload("90004", "CINT6", "Eva Renewed"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void updateChangesPersonData() throws Exception {
        Long clientId = createClient("90005", "CINT7", "Franco");

        Map<String, Object> update = new HashMap<>(createPayload("90005", "CINT7", "Franco"));
        update.put("firstName", "Franco Updated");
        update.remove("password");

        mockMvc.perform(put("/clients/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Franco Updated"));
    }

    @Test
    void deactivateReturnsNoContent() throws Exception {
        Long clientId = createClient("90006", "CINT8", "Gina");
        when(accountsValidation.hasOpenAccounts(clientId)).thenReturn(false);

        mockMvc.perform(delete("/clients/" + clientId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deactivatedClientDoesNotAppearInList() throws Exception {
        Long clientId = createClient("90007", "CINT9", "Hugo");
        when(accountsValidation.hasOpenAccounts(clientId)).thenReturn(false);

        mockMvc.perform(delete("/clients/" + clientId)).andExpect(status().isNoContent());

        mockMvc.perform(get("/clients").param("filter", "Hugo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.clientId == " + clientId + ")]", hasSize(0)));
    }

    @Test
    void deactivateNonExistentClientReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/clients/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deactivateClientWithOpenAccountsReturnsConflict() throws Exception {
        Long clientId = createClient("90008", "CINT10", "Iris");
        when(accountsValidation.hasOpenAccounts(clientId)).thenReturn(true);

        mockMvc.perform(delete("/clients/" + clientId))
                .andExpect(status().isConflict());
    }

    @Test
    void deactivateWhenAccountsServiceUnavailableReturnsServiceUnavailable() throws Exception {
        Long clientId = createClient("90009", "CINT11", "Jaime");
        when(accountsValidation.hasOpenAccounts(clientId))
                .thenThrow(new AccountsServiceUnavailableException(clientId));

        mockMvc.perform(delete("/clients/" + clientId))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void createWithMissingRequiredFieldReturnsBadRequest() throws Exception {
        Map<String, Object> payload = new HashMap<>(createPayload("90010", "CINT12", "Karl"));
        payload.remove("firstName");

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateNonExistentClientReturnsNotFound() throws Exception {
        mockMvc.perform(put("/clients/999998")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload("00001", "CINTX", "Ghost"))))
                .andExpect(status().isNotFound());
    }
}
