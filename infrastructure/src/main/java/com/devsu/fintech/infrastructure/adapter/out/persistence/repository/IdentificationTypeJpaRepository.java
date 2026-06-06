package com.devsu.fintech.infrastructure.adapter.out.persistence.repository;

import com.devsu.fintech.infrastructure.adapter.out.persistence.entity.IdentificationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificationTypeJpaRepository extends JpaRepository<IdentificationTypeEntity, Integer> {
}
