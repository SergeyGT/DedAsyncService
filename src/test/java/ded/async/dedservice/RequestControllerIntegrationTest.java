package ded.async.dedservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ded.async.dedservice.DTOs.RequestDTO;
import ded.async.dedservice.Repositories.RequestRepository;
import ded.async.dedservice.Repositories.RequestStatusRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class RequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestStatusRepository requestStatusRepository;

    @Test
    @Transactional
    void shouldReturnExistingIdForDuplicateRequest() throws Exception {
        // Создаем тестовый запрос в БД
        RequestDTO testRequest = new RequestDTO();
        testRequest.setRequestData(objectMapper.createObjectNode()
            .put("action", "testAction")
            .put("value", 100));

        // Первый запрос - должен создать новый
        mockMvc.perform(post("/requests")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        // Второй идентичный запрос - должен вернуть существующий ID
        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1)); // предполагая, что ID=1
    }

    @Test
    @Transactional
    void shouldCreateNewRequestForDifferentData() throws Exception {
        RequestDTO request1 = new RequestDTO();
        request1.setRequestData(objectMapper.createObjectNode()
            .put("action", "action1")
            .put("value", 100));

        RequestDTO request2 = new RequestDTO();
        request2.setRequestData(objectMapper.createObjectNode()
            .put("action", "action2") // другое действие
            .put("value", 100));

        // Первый запрос
        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        // Второй запрос с другими данными
        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2)); // должен быть новый ID
    }
}
