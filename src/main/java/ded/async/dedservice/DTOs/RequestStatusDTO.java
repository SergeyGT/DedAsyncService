package ded.async.dedservice.DTOs;

import java.time.LocalDateTime;

import ded.async.dedservice.Entities.RequestStatus;
import ded.async.dedservice.Entities.Status;
import lombok.Data;

@Data
public class RequestStatusDTO {
    private Long id;
    private Status status;
    private LocalDateTime createdAt;
    private Long requestId; 

    public static RequestStatusDTO fromEntity(RequestStatus status) {
        RequestStatusDTO dto = new RequestStatusDTO();
        dto.setId(status.getId());
        dto.setStatus(status.getStatus());
        dto.setCreatedAt(status.getCreatedAt());
        dto.setRequestId(status.getRequest().getId());
        return dto;
    }
}
