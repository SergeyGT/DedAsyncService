package ded.async.dedservice.Services;

import java.util.List;
import java.util.Optional;

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
        if(requestDTO.getRequestData().isNull()){
            throw new IllegalArgumentException("Empty Request data!");
        }
        return requestRepository.save(Request.builder()
            .requestData(requestDTO.getRequestData())
            .build());
    }

    public List<Request> read(){
        return requestRepository.findAll();
    }

    public Optional<Request> findById(Long id) {
        return requestRepository.findById(id);
    }

}
