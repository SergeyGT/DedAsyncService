package ded.async.dedservice.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    CREATED,
    @JsonProperty("pending")
    PENDING, 
    @JsonProperty("processing")
    PROCESSING, 
    @JsonProperty("completed")
    COMPLETED, 
    @JsonProperty("pending")
    FAILED
}
