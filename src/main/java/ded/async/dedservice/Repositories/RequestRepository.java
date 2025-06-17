package ded.async.dedservice.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ded.async.dedservice.Entities.Request;
import ded.async.dedservice.Entities.Status;
import jakarta.persistence.LockModeType;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>{

   //@Lock(LockModeType.PESSIMISTIC_WRITE)
   @Query(value = """
      SELECT r.* FROM request r
      JOIN request_status rs ON r.id = rs.request_id
      WHERE rs.id = (
         SELECT rs2.id FROM request_status rs2
         WHERE rs2.request_id = r.id
         ORDER BY rs2.created_at DESC
         LIMIT 1
      )
      AND rs.status NOT IN (:terminalStatuses)
      AND r.request_data::jsonb @> (:requestData)::jsonb
      AND (:requestData)::jsonb @> r.request_data::jsonb
   """, nativeQuery = true)
   Optional<Request> findDuplicate(
      @Param("requestData") String requestData,
      @Param("terminalStatuses") List<String> terminalStatuses
   );

} 

