package com.timmynet.clinik.dto;

import com.timmynet.clinik.domain.EstadoCita;
import com.timmynet.clinik.domain.TipoCita;
import java.time.LocalDateTime;

public record CitaRequest(
    TipoCita tipoCita,
    Long pacienteId,
    Long profesionalId,
    LocalDateTime scheduledAt,
    EstadoCita estadoCita,
    Boolean asistio,
    String razonCancelacion,
    String notas
) {}
