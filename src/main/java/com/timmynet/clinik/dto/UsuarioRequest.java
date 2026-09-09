package com.timmynet.clinik.dto;

public record UsuarioRequest(String username, String password, String email, Long rolId) {}
