package com.timmynet.clinik.dto;

import java.time.LocalDateTime;

public record DocumentoResponse(Long id, String filename, String contentType, Long fileSize,
                                LocalDateTime uploadedAt, LocalDateTime updatedAt, Long examenId) {}
