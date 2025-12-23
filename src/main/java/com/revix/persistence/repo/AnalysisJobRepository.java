package com.revix.persistence.repo;

import com.revix.persistence.entity.AnalysisJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnalysisJobRepository extends JpaRepository<AnalysisJobEntity, UUID> {
}

