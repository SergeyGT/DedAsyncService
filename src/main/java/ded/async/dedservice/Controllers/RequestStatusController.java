package ded.async.dedservice.Controllers;

import org.springframework.web.bind.annotation.RestController;

import ded.async.dedservice.DTOs.RequestStatusDTO;
import ded.async.dedservice.Entities.Request;
import ded.async.dedservice.Entities.Status;
import ded.async.dedservice.Services.RequestService;
import ded.async.dedservice.Services.RequestStatusService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController("/status")
@AllArgsConstructor
public class RequestStatusController {
    private final RequestStatusService requestStatusService;
    private final RequestService requestService;

    @GetMapping("/current/{id}")
    public ResponseEntity<RequestStatusDTO> getStatus(@PathVariable Long id) {
       return requestStatusService.getLatestStatus(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
       
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<RequestStatusDTO> updateStatus(@PathVariable Long id, @RequestBody Status status) {
        Optional<Request> request = requestService.findById(id);
        
        if(request.isEmpty()) return ResponseEntity.notFound().build();

        RequestStatusDTO updatedStatus  = requestStatusService.addStatus(request.get(), status);

        return ResponseEntity.ok(updatedStatus);
    }

    @GetMapping("/all/{id}")
    public List<RequestStatusDTO> getAllStatus(@PathVariable Long id) {
        return requestStatusService.getStatusHistory(id);
    }
    
    
    
}
