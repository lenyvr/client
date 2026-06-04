package com.devsu.fintech.domain.port.out;

/**
 * Output port for password encoding. Implemented in infrastructure (BCrypt).
 */
public interface PasswordEncoderSPI {

    String encode(String rawPassword);
}
