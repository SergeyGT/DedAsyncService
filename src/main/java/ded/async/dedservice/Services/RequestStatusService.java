package ded.async.dedservice.Services;

import ded.async.dedservice.DTOs.RequestStatusDTO;
import ded.async.dedservice.Entities.Request;
import ded.async.dedservice.Entities.RequestStatus;
import ded.async.dedservice.Entities.Status;
import ded.async.dedservice.Repositories.RequestStatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestStatusService {

    private final RequestStatusRepository statusRepository;
    private final int delayBetweenStatusChanges = 5000;
    private final Semaphore procSemaphore = new Semaphore(5);   
    private Logger log;

    private static final List<Status> STATUS_SEQUENCE = Arrays.asList(
        Status.PENDING,
        Status.PROCESSING,
        Status.COMPLETED
    );

    public RequestStatusDTO addStatus(Request request, Status status) {
        RequestStatus newStatus = RequestStatus.builder()
            .request(request)
            .status(status)
            .createdAt(LocalDateTime.now())
            .build();

        RequestStatus savedStatus = statusRepository.save(newStatus);
        return RequestStatusDTO.fromEntity(savedStatus);
    }

    public Optional<RequestStatusDTO> getLatestStatus(Long requestId) {
        return statusRepository.findLatestByRequestId(requestId)
            .map(RequestStatusDTO::fromEntity);
    }

    public List<RequestStatusDTO> getStatusHistory(Long requestId) {
        return statusRepository.findByRequestIdOrderByCreatedAtDesc(requestId)
            .stream()
            .map(RequestStatusDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Async
    @Transactional
    @Scheduled(fixedDelayString =  "${status.change.delay}")
    public void proccessRequest() throws InterruptedException{
        List<RequestStatus> createdRequests = statusRepository.findAllWithLatestStatusCreated();

        if (createdRequests.isEmpty()) {
            log.info("No requests with CREATED status found");
            return;
        }

        for (RequestStatus createdStatus : createdRequests) {
            procSemaphore.acquire();
            try {
                Request request = createdStatus.getRequest();

                Optional<RequestStatus> currentStatus = statusRepository.findLatestByRequestId(request.getId());

                if (currentStatus.isEmpty() || currentStatus.get().getStatus() != Status.CREATED) {
                    continue;
                }

                for (Status nextStatus : STATUS_SEQUENCE) {
                    Thread.sleep(delayBetweenStatusChanges);
                    addStatus(request, nextStatus);
                }
            } finally {
                procSemaphore.release();
            }
        }
    }
    
}
