package com.vecanhac.ddd.domain.token;

public interface TokenProvider {
    String generateToken(String subject);

    String getEmailFromToken(String token);
}