package ded.async.dedservice.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import ded.async.dedservice.DTOs.RequestDTO;
import ded.async.dedservice.Entities.Request;
import ded.async.dedservice.Repositories.RequestRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public Request create(RequestDTO requestDTO){
        return requestRepository.save(Request.builder()
            .requestData(requestDTO.getRequestData())
            .build());
    }

    public List<Request> read(){
        return requestRepository.findAll();
    }
}
