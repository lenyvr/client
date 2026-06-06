package com.devsu.fintech.infrastructure.adapter.out.persistence;

import com.devsu.fintech.domain.port.out.IdentificationTypeLookupSPI;
import com.devsu.fintech.infrastructure.adapter.out.persistence.entity.IdentificationTypeEntity;
import com.devsu.fintech.infrastructure.adapter.out.persistence.repository.IdentificationTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IdentificationTypePersistenceAdapter implements IdentificationTypeLookupSPI {

    private final IdentificationTypeJpaRepository repository;

    @Override
    public String findNameById(Integer identificationTypeId) {
        return repository.findById(identificationTypeId)
                .map(IdentificationTypeEntity::getName)
                .orElse("UNKNOWN");
    }
}
