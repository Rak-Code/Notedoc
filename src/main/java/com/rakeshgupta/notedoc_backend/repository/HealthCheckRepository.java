package com.rakeshgupta.notedoc_backend.repository;

import com.rakeshgupta.notedoc_backend.entity.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long> {
    List<HealthCheck> findTop10ByOrderByCheckTimeDesc();
}
