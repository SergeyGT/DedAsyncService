package ded.async.dedservice.DTOs;


import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class RequestDTO {
    private JsonNode requestData;
    private Integer duplicateCount;

    public String getNormalizedRequestData() {
        return JsonUtils.normalizeJson(requestData.toString());
    }
    
    public String getRequestHash() {
        return DigestUtils.sha256Hex(getNormalizedRequestData());
    }
}
