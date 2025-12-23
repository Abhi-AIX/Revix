package com.revix.persistance.repo;
import com.revix.persistance.entity.AnalysisFindingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnalysisFindingRepository extends JpaRepository<AnalysisFindingEntity, UUID> {
    List<AnalysisFindingEntity> findByJobId(UUID jobId);
}
