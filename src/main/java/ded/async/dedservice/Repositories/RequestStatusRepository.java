package ded.async.dedservice.Repositories;

import ded.async.dedservice.Entities.RequestStatus;
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RequestStatusRepository extends JpaRepository<RequestStatus, Long> {

    @Query("SELECT rs FROM RequestStatus rs WHERE rs.request.id = :requestId ORDER BY rs.createdAt DESC LIMIT 1")
    Optional<RequestStatus> findLatestByRequestId(@Param("requestId") Long requestId);

    List<RequestStatus> findByRequestIdOrderByCreatedAtDesc(Long requestId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT rs FROM RequestStatus rs " +
           "WHERE rs.status = 'CREATED' " +
           "AND rs.createdAt = (SELECT MAX(rs2.createdAt) FROM RequestStatus rs2 WHERE rs2.request = rs.request)")
    List<RequestStatus> findAllWithLatestStatusCreated();
}
