package ded.async.dedservice.Controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ded.async.dedservice.DTOs.RequestDTO;
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
    public ResponseEntity<Map<String, Object>> create(@RequestBody RequestDTO requestDTO) {
         try {
        Request createdRequest = requestService.create(requestDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("id", createdRequest.getId());
        response.put("completedDuplicatesCount", createdRequest.getDuplicateCount());
        return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Collections.singletonMap("error", "Internal server error"));
        }    
    }

    @GetMapping
    public List<Request> read() {
        return requestService.read();
    }
}
