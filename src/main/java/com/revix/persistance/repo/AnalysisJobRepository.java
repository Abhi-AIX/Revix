package com.revix.persistance.repo;

import com.revix.persistance.entity.AnalysisJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnalysisJobRepository extends JpaRepository<AnalysisJobEntity, UUID> {
}

