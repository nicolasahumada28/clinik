package com.timmynet.clinik.dto;

import com.timmynet.clinik.domain.EstadoCita;
import com.timmynet.clinik.domain.TipoCita;
import java.time.LocalDateTime;
import java.util.List;

public record CitaResponse(
    Long id,
    TipoCita tipoCita,
    Long pacienteId,
    Long profesionalId,
    LocalDateTime scheduledAt,
    EstadoCita estadoCita,
    Boolean asistio,
    String razonCancelacion,
    String notas,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<BitacoraResponse> logs
) {}
