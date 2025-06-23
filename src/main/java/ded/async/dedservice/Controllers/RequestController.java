package ded.async.dedservice.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ded.async.dedservice.DTOs.RequestDTO;
import ded.async.dedservice.DTOs.RequestResponseDTO;
import ded.async.dedservice.Entities.Request;
import ded.async.dedservice.Services.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/requests")
@AllArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestResponseDTO> create(@RequestBody RequestDTO requestDTO) {
        Request createdRequest = requestService.create(requestDTO);
        RequestResponseDTO requestResponseDTO = new RequestResponseDTO(
            createdRequest.getId(), 
            createdRequest.getDuplicateCount());
        
        return ResponseEntity.ok(requestResponseDTO);        
    }

    @GetMapping
    public List<Request> read() {
        return requestService.read();
    }
}
