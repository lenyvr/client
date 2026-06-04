package com.devsu.fintech.domain.model;

import com.devsu.fintech.domain.exception.InvalidPersonDataException;

/**
 * Domain entity representing a person. {@link Client} is an extension of this entity.
 *
 * <p>Pure domain object: no framework dependencies. Invariants are enforced through
 * {@link #validate()}, invoked on construction.
 */
public class Person {

    private Long personId;
    private String firstName;
    private String lastName;
    private int age;
    private Integer genderId;
    private String identificationNumber;
    private String address;
    private String postalCode;
    private String contactNumber;
    private Integer identificationTypeId;
    private String email; // nullable

    protected Person() {
        // for subclasses that populate fields through setters
    }

    public Person(Long personId, String firstName, String lastName, int age, Integer genderId,
                  String identificationNumber, String address, String postalCode,
                  String contactNumber, Integer identificationTypeId, String email) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.genderId = genderId;
        this.identificationNumber = identificationNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.contactNumber = contactNumber;
        this.identificationTypeId = identificationTypeId;
        this.email = email;
        validate();
    }

    /**
     * Enforces the person invariants. Email is optional; everything else is required.
     */
    private void validate() {
        requireText(firstName, "firstName");
        requireText(lastName, "lastName");
        if (age <= 0) {
            throw new InvalidPersonDataException("age must be greater than 0");
        }
        if (genderId == null) {
            throw new InvalidPersonDataException("genderId is required");
        }
        requireText(identificationNumber, "identificationNumber");
        requireText(address, "address");
        requireText(postalCode, "postalCode");
        requireText(contactNumber, "contactNumber");
        if (identificationTypeId == null) {
            throw new InvalidPersonDataException("identificationTypeId is required");
        }
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidPersonDataException(field + " is required");
        }
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Integer getIdentificationTypeId() {
        return identificationTypeId;
    }

    public String getEmail() {
        return email;
    }
}
