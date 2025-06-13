package ded.async.dedservice.Repositories;

import ded.async.dedservice.Entities.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RequestStatusRepository extends JpaRepository<RequestStatus, Long> {

    // Получить последний статус запроса
    @Query("SELECT rs FROM RequestStatus rs WHERE rs.request.id = :requestId ORDER BY rs.createdAt DESC LIMIT 1")
    Optional<RequestStatus> findLatestByRequestId(@Param("requestId") Long requestId);

    // Получить всю историю статусов (от новых к старым)
    List<RequestStatus> findByRequestIdOrderByCreatedAtDesc(Long requestId);
}
