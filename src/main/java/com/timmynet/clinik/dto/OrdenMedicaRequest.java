package com.timmynet.clinik.dto;

import com.timmynet.clinik.domain.TipoOrdenMedica;

public record OrdenMedicaRequest(TipoOrdenMedica tipoOrdenMedica, Long pacienteId, Long profesionalId,
                                 Long citaId, String descripcion) {}
