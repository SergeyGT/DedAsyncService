package ded.async.dedservice.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ded.async.dedservice.DTOs.RequestDTO;
import ded.async.dedservice.Entities.Request;
import ded.async.dedservice.Services.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@AllArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<Request> create(@RequestBody RequestDTO requestDTO){
       Request request = Request.builder()
            .requestData(requestDTO.getRequestData())
            .build();
        requestService.create();
    }

    @GetMapping("/read")
    public List<Request> read() {
        return requestService.read();
    }
    
}
