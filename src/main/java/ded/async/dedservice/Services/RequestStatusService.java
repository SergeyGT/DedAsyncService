package ded.async.dedservice.Services;

import ded.async.dedservice.DTOs.RequestStatusDTO;
import ded.async.dedservice.Entities.Request;
import ded.async.dedservice.Entities.RequestStatus;
import ded.async.dedservice.Entities.Status;
import ded.async.dedservice.Repositories.RequestStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestStatusService {

    private final RequestStatusRepository statusRepository;

    // Добавить новый статус
    public RequestStatusDTO addStatus(Request request, Status status) {
        RequestStatus newStatus = RequestStatus.builder()
            .request(request)
            .status(status)
            .createdAt(LocalDateTime.now())
            .build();

        RequestStatus savedStatus = statusRepository.save(newStatus);
        return RequestStatusDTO.fromEntity(savedStatus);
    }

    // Получить текущий статус
    public Optional<RequestStatusDTO> getLatestStatus(Long requestId) {
        return statusRepository.findLatestByRequestId(requestId)
            .map(RequestStatusDTO::fromEntity);
    }

    // Получить историю статусов
    public List<RequestStatusDTO> getStatusHistory(Long requestId) {
        return statusRepository.findByRequestIdOrderByCreatedAtDesc(requestId)
            .stream()
            .map(RequestStatusDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
