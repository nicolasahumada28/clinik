package com.timmynet.clinik.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExamenResponse(Long id, String nombre, String descripcion, LocalDateTime createdAt,
                             LocalDateTime updatedAt, Long citaId, List<DocumentoResponse> documentos) {}
