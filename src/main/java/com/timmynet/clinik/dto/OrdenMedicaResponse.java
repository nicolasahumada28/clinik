package com.timmynet.clinik.dto;

import com.timmynet.clinik.domain.TipoOrdenMedica;
import java.time.LocalDateTime;

public record OrdenMedicaResponse(Long id, TipoOrdenMedica tipoOrdenMedica, Long pacienteId,
                                  Long profesionalId, Long citaId, String descripcion,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {}
