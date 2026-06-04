package com.devsu.fintech.infrastructure.adapter.out.security;

import com.devsu.fintech.domain.port.out.PasswordEncoderSPI;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Output adapter implementing {@link PasswordEncoderSPI} on top of Spring Security's
 * {@link PasswordEncoder} (BCrypt).
 */
@Component
public class PasswordEncoderAdapter implements PasswordEncoderSPI {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
