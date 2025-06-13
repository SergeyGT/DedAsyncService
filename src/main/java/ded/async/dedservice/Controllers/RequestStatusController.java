package ded.async.dedservice.Controllers;

import org.springframework.web.bind.annotation.RestController;

import ded.async.dedservice.DTOs.RequestStatusDTO;
import ded.async.dedservice.Services.RequestStatusService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@AllArgsConstructor
public class RequestStatusController {
    private final RequestStatusService requestStatusService;

    @GetMapping("/status/{id}")
    public ResponseEntity<RequestStatusDTO>  getStatus(@PathVariable Long id) {
       return requestStatusService.getLatestStatus(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
       
    }
    
}
