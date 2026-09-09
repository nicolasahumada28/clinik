package com.timmynet.clinik.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {
}
