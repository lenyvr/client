package com.devsu.fintech.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * JPA mapping for the {@code person} table. Catalog references (gender, identification type)
 * are mapped as scalar ids; the foreign keys are enforced by the database.
 */
@Setter
@Getter
@Entity
@Table(name = "person")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "gender_id", nullable = false)
    private Integer genderId;

    @Column(name = "identification_number", nullable = false, unique = true)
    private String identificationNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "identification_type_id", nullable = false)
    private Integer identificationTypeId;

    @Column(name = "email")
    private String email;

}
