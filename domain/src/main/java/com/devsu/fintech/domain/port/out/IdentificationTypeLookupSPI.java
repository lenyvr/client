package com.devsu.fintech.domain.port.out;

public interface IdentificationTypeLookupSPI {

    String findNameById(Integer identificationTypeId);
}
