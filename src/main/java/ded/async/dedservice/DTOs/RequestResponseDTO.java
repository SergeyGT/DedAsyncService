package ded.async.dedservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestResponseDTO {
    private Long id;
    private int completedDuplicatesCount; 
}
