package ded.async.dedservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
    void shouldReturnExistingIdForDuplicateRequest() throws Exception {
        // Подготовка тестовых данных
        RequestDTO testRequest = new RequestDTO();
        testRequest.setRequestData(objectMapper.createObjectNode()
            .put("action", "testAction")
            .put("value", 100));

        // Первый запрос - сохраняем ID
        MvcResult firstResponse = mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        
        Long firstId = objectMapper.readTree(firstResponse.getResponse().getContentAsString()).get("id").asLong();

        // Второй запрос - сохраняем ID
        MvcResult secondResponse = mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        
        Long secondId = objectMapper.readTree(secondResponse.getResponse().getContentAsString()).get("id").asLong();

        // Проверяем, что ID совпадают
        assertEquals(firstId, secondId, "Duplicate request should return same ID");
    }

    @Test
    void shouldCreateNewRequestForDifferentData() throws Exception {
        // Подготовка тестовых данных
        RequestDTO request1 = new RequestDTO();
        request1.setRequestData(objectMapper.createObjectNode()
            .put("action", "action1")
            .put("value", 100));

        RequestDTO request2 = new RequestDTO();
        request2.setRequestData(objectMapper.createObjectNode()
            .put("action", "action2")
            .put("value", 100));

        // Первый запрос - сохраняем ID
        MvcResult firstResponse = mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        
        Long firstId = objectMapper.readTree(firstResponse.getResponse().getContentAsString()).get("id").asLong();

        // Второй запрос - сохраняем ID
        MvcResult secondResponse = mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        
        Long secondId = objectMapper.readTree(secondResponse.getResponse().getContentAsString()).get("id").asLong();

        // Проверяем, что ID разные
        assertNotEquals(firstId, secondId, "Different requests should have different IDs");
    }
    @Test
    void shouldReturnBadRequestForNullData() throws Exception {
        RequestDTO nullRequest = new RequestDTO();
        nullRequest.setRequestData(null);

        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Empty Request data!"));
    }

}
