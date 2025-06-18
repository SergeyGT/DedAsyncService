package ded.async.dedservice.DTOs;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class RequestDTO {
    private JsonNode requestData;
    private Integer duplicateCount;
}
