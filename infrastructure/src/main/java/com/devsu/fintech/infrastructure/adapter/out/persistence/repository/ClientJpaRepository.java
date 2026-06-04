package com.devsu.fintech.infrastructure.adapter.out.persistence.repository;

import com.devsu.fintech.infrastructure.adapter.out.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByPerson_IdentificationNumber(String identificationNumber);

    /**
     * Active clients optionally filtered (case-insensitive, partial) by first/last name,
     * identification number or client code. A null/blank filter returns all active clients.
     */
    @Query("""
            SELECT c FROM ClientEntity c
            WHERE c.status = true
              AND (:filter IS NULL OR :filter = ''
                   OR LOWER(c.person.firstName) LIKE LOWER(CONCAT('%', :filter, '%'))
                   OR LOWER(c.person.lastName) LIKE LOWER(CONCAT('%', :filter, '%'))
                   OR LOWER(c.person.identificationNumber) LIKE LOWER(CONCAT('%', :filter, '%'))
                   OR LOWER(c.clientCode) LIKE LOWER(CONCAT('%', :filter, '%')))
            """)
    List<ClientEntity> searchActive(@Param("filter") String filter);
}
