package ded.async.dedservice.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ded.async.dedservice.Entities.Request;

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
      AND r.request_hash = :requestHash
   """, nativeQuery = true)
   Optional<Request> findDuplicate(
      @Param("requestHash") String requestHash,
      @Param("terminalStatuses") List<String> terminalStatuses
   );

    // @Query(value = """
    //     SELECT COUNT(r.id) FROM request r
    //     JOIN request_status rs ON r.id = rs.request_id
    //     WHERE rs.id = (
    //         SELECT rs2.id FROM request_status rs2
    //         WHERE rs2.request_id = r.id
    //         ORDER BY rs2.created_at DESC
    //         LIMIT 1
    //     )
    //     AND rs.status = 'COMPLETED'
    //     AND r.request_data::jsonb @> (:requestData)::jsonb
    //     AND (:requestData)::jsonb @> r.request_data::jsonb
    //     AND r.id != :excludeId
    // """, nativeQuery = true)
    // Long countCompletedDuplicates(
    //     @Param("requestData") String requestData,
    //     @Param("excludeId") Long excludeId
    // );

} 

