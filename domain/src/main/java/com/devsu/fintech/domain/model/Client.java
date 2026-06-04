package com.devsu.fintech.domain.model;

import com.devsu.fintech.domain.exception.InvalidClientDataException;

/**
 * Domain entity representing a client. A client <b>is an extension of a {@link Person}</b>:
 * persisting a client requires its person to be persisted first.
 *
 * <p>The {@code password} held here may be either a raw value (as received before
 * encoding) or an already-encoded value; encoding is an application/infrastructure concern.
 */
public class Client extends Person {

    private Long clientId;
    private String clientCode;
    private String password;
    private boolean status;

    public Client(Long personId, String firstName, String lastName, int age, Integer genderId,
                  String identificationNumber, String address, String postalCode,
                  String contactNumber, Integer identificationTypeId, String email,
                  Long clientId, String clientCode, String password, boolean status) {
        super(personId, firstName, lastName, age, genderId, identificationNumber, address,
                postalCode, contactNumber, identificationTypeId, email);
        this.clientId = clientId;
        this.clientCode = clientCode;
        this.password = password;
        this.status = status;
        validateClient();
    }

    private void validateClient() {
        if (clientCode == null || clientCode.isBlank()) {
            throw new InvalidClientDataException("clientCode is required");
        }
        if (password == null || password.isBlank()) {
            throw new InvalidClientDataException("password is required");
        }
    }

    public void activate() {
        this.status = true;
    }

    public void deactivate() {
        this.status = false;
    }

    public boolean isActive() {
        return status;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientCode() {
        return clientCode;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Replaces the stored password (e.g. with its encoded form before persistence).
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getStatus() {
        return status;
    }
}
