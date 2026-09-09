package com.timmynet.clinik.dto;

import java.time.LocalDateTime;

public record BitacoraResponse(Long id, Long citaId, String note, LocalDateTime createdAt, LocalDateTime updatedAt) {}
