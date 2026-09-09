package com.timmynet.clinik.dto;

import java.time.LocalDateTime;

public record RolResponse(Long id, String nombre, int activo, LocalDateTime createdAt, LocalDateTime updatedAt) {}
