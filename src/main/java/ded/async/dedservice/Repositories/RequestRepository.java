package ded.async.dedservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ded.async.dedservice.Entities.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>{

    
} 

