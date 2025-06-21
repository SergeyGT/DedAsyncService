package ded.async.dedservice.Services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ded.async.dedservice.DTOs.RequestDTO;
import ded.async.dedservice.Entities.Request;
import ded.async.dedservice.Entities.Status;
import ded.async.dedservice.Repositories.RequestRepository;
import ded.async.dedservice.exception.ApiRequestException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestStatusService requestStatusService;

    private static final List<String> STATUS_TERMINAL = Arrays.asList(
        Status.COMPLETED.name()
    );

    @Transactional
    public Request create(RequestDTO requestDTO){
        if(requestDTO.getRequestData().isEmpty() || requestDTO.getRequestData().isNull()) {
            throw new ApiRequestException("Empty or Null Request data!");
        }

        String requestHash = requestDTO.getRequestHash();
        String normalizedData = requestDTO.getNormalizedRequestData();

        Optional<Request> searchRequest = requestRepository.findDuplicate(
            requestHash, 
            STATUS_TERMINAL
        );

        if(searchRequest.isPresent()) {
            System.out.println("Found duplicate request with id: " + searchRequest.get().getId());
            searchRequest.get().setDuplicateCount(searchRequest.get().getDuplicateCount()+1);
            return requestRepository.save(searchRequest.get());
        }

        Request createdRequest = Request.builder()
            .normalizedRequestData(normalizedData)
            .requestHash(requestHash)
            .duplicateCount(0)
            .build();

        Request request = requestRepository.save(createdRequest);
        requestStatusService.addStatus(createdRequest, Status.CREATED);
        
        return request;
    }

    public List<Request> read(){
        return requestRepository.findAll();
    }

    public Optional<Request> findById(Long id) {
        return requestRepository.findById(id);
    }

}
