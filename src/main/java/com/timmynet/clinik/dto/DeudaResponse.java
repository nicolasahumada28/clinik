package com.timmynet.clinik.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DeudaResponse(Long id, Long pacienteId, BigDecimal montoTotal, BigDecimal balance,
                            LocalDateTime createdAt, LocalDateTime updatedAt, String estado,
                            List<PagoResponse> payments) {}
