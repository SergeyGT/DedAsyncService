package ded.async.dedservice.Controllers;

import org.springframework.web.bind.annotation.RestController;

import ded.async.dedservice.Services.RequestStatusService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@AllArgsConstructor
public class RequestStatusController {
    private final RequestStatusService requestStatusService;

    @GetMapping("/status/{id}")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
}
